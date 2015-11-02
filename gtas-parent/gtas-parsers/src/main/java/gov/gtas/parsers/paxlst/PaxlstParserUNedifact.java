package gov.gtas.parsers.paxlst;

import java.util.Date;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.paxlst.segment.unedifact.ATT;
import gov.gtas.parsers.paxlst.segment.unedifact.BGM;
import gov.gtas.parsers.paxlst.segment.unedifact.CNT;
import gov.gtas.parsers.paxlst.segment.unedifact.COM;
import gov.gtas.parsers.paxlst.segment.unedifact.CPI;
import gov.gtas.parsers.paxlst.segment.unedifact.CTA;
import gov.gtas.parsers.paxlst.segment.unedifact.DOC;
import gov.gtas.parsers.paxlst.segment.unedifact.DTM;
import gov.gtas.parsers.paxlst.segment.unedifact.DTM.DtmCode;
import gov.gtas.parsers.paxlst.segment.unedifact.EMP;
import gov.gtas.parsers.paxlst.segment.unedifact.FTX;
import gov.gtas.parsers.paxlst.segment.unedifact.GEI;
import gov.gtas.parsers.paxlst.segment.unedifact.LOC;
import gov.gtas.parsers.paxlst.segment.unedifact.LOC.LocCode;
import gov.gtas.parsers.paxlst.segment.unedifact.MEA;
import gov.gtas.parsers.paxlst.segment.unedifact.NAD;
import gov.gtas.parsers.paxlst.segment.unedifact.NAT;
import gov.gtas.parsers.paxlst.segment.unedifact.QTY;
import gov.gtas.parsers.paxlst.segment.unedifact.RFF;
import gov.gtas.parsers.paxlst.segment.unedifact.TDT;
import gov.gtas.parsers.paxlst.segment.unedifact.TDT.TdtType;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.vo.passenger.DocumentVo;
import gov.gtas.vo.passenger.FlightVo;
import gov.gtas.vo.passenger.PassengerVo;
import gov.gtas.vo.passenger.ReportingPartyVo;
import gov.gtas.vo.ApisMessageVo;

public final class PaxlstParserUNedifact extends EdifactParser<ApisMessageVo> {   
    public PaxlstParserUNedifact() {
        this.parsedMessage = new ApisMessageVo();
    }
    
    protected String getPayloadText(String message) throws ParseException {
        return EdifactLexer.getMessagePayload(message, "BGM", "UNT");
    }

    @Override
    protected void parsePayload() throws ParseException {
        BGM bgm = getMandatorySegment(BGM.class);
        parsedMessage.setMessageCode(bgm.getCode());

        getConditionalSegment(RFF.class);

        for (;;) {
            DTM dtm = getConditionalSegment(DTM.class);
            if (dtm == null) {
                break;
            }
        }

        for (;;) {
            NAD nad = getConditionalSegment(NAD.class);
            if (nad == null) {
                break;
            }
            processReportingParty(nad);
        }

        // at least one TDT is mandatory
        TDT tdt = getMandatorySegment(TDT.class);
        processFlight(tdt);
        for (;;) {
            tdt = getConditionalSegment(TDT.class);
            if (tdt == null) {
                break;
            }
            processFlight(tdt);
        }
        
        for (;;) {
            NAD nad = getConditionalSegment(NAD.class);
            if (nad == null) {
                break;
            }
            processPax(nad);
        }
        
        getMandatorySegment(CNT.class);
    }

    /**
     * Segment group 1: reporting party
     */
    private void processReportingParty(NAD nad) throws ParseException {
        ReportingPartyVo rp = new ReportingPartyVo();
        parsedMessage.addReportingParty(rp);
        String partyName = nad.getProfileName();
        if (partyName == null) {
            partyName = nad.getFirstName() + " " + nad.getLastName();
        }
        rp.setPartyName(partyName);

        getConditionalSegment(CTA.class);

        for (;;) {
            COM com = getConditionalSegment(COM.class);
            if (com == null) {
                break;
            }
            rp.setTelephone(ParseUtils.prepTelephoneNumber(com.getPhoneNumber()));
            rp.setFax(ParseUtils.prepTelephoneNumber(com.getFaxNumber()));
        }
    }

    /**
     * Segment group 2: flight details
     */
    private void processFlight(TDT tdt) throws ParseException {
        if (tdt.isMasterCrewList()) {
            // Master crew lists (MCLs) are part of TSA regulations
            // and not something we handle.
            throw new ParseException("Master crew lists (MCLs) not handled at this time");
        }
        
        String dest = null;
        String origin = null;
        Date eta = null;
        Date etd = null;
        boolean loc92Seen = false;

        for (;;) {
            DTM dtm = getConditionalSegment(DTM.class);
            if (dtm == null) {
                break;
            }
        }       
        
        // Segment group 3: loc-dtm loop
        for (;;) {
            LOC loc = getConditionalSegment(LOC.class);
            if (loc == null) {
                break;
            }

            LocCode locCode = loc.getFunctionCode();
            String airport = loc.getLocationNameCode();

            switch (locCode) {
            case DEPARTURE_AIRPORT:
                origin = airport;
                break;
            case ARRIVAL_AIRPORT:
                dest = airport;
                break;
            case BOTH_DEPARTURE_AND_ARRIVAL_AIRPORT:
                if (loc92Seen) {
                    dest = airport;
                    loc92Seen = false;
                } else {
                    origin = airport;
                    loc92Seen = true;
                }
                break;
            case FINAL_DESTINATION:
                if (loc92Seen) {
                    dest = airport;
                    loc92Seen = false;
                } else {
                    throw new ParseException("LOC+" + LocCode.FINAL_DESTINATION + " found but no corresponding LOC+"
                            + LocCode.BOTH_DEPARTURE_AND_ARRIVAL_AIRPORT);
                }
                break;
            }

            // get corresponding DTM, if it exists
            DTM dtm = getConditionalSegment(DTM.class);
            if (dtm != null) {
                Date d = dtm.getDtmValue();
                DtmCode dtmCode = dtm.getDtmCode();
                if (dtmCode == DtmCode.DEPARTURE) {
                    etd = d;
                } else if (dtmCode == DtmCode.ARRIVAL) {
                    eta = d;
                }
            }

            if (origin != null && dest != null) {
                FlightVo f = new FlightVo();
                parsedMessage.addFlight(f);
                TdtType flightType = tdt.getTransportStageQualifier();
                f.setOverFlight(flightType.equals(TdtType.OVER_FLIGHT));
                f.setFlightNumber(ParseUtils.padFlightNumberWithZeroes(tdt.getFlightNumber()));
                f.setCarrier(tdt.getC_carrierIdentifier());
                f.setOrigin(origin);
                f.setDestination(dest);
                f.setEta(eta);
                f.setEtd(etd);
                ParseUtils.initEtaEtdDate(f);
                Date flightDate = ParseUtils.determineFlightDate(etd, eta, parsedMessage.getTransmissionDate());
                if (flightDate == null) {
                    throw new ParseException("Could not determine flight date");
                }
                f.setFlightDate(flightDate);

                dest = null;
                origin = null;
                eta = null;
                etd = null;
                loc92Seen = false;
            }
        }
    }
    
    /**
     * Segment group 4: passenger details
     */
    private void processPax(NAD nad) throws ParseException {
        PassengerVo p = new PassengerVo();
        parsedMessage.addPax(p);

        p.setFirstName(nad.getFirstName());
        p.setLastName(nad.getLastName());
        p.setMiddleName(nad.getMiddleName());
        
        String paxType = null;
        if (nad.getNadCode() == null) {
            paxType = "P";
        } else {
            switch (nad.getNadCode()) {
            case CREW_MEMBER:
            case INTRANSIT_CREW_MEMBER:
                paxType = "C";
                break;
            case INTRANSIT_PASSENGER:
                paxType = "I";
            default:
                paxType = "P";
                break;
            }
        }
        p.setPassengerType(paxType);

        for (;;) {
            ATT att = getConditionalSegment(ATT.class);
            if (att == null) {
                break;
            }
            switch (att.getFunctionCode()) {
            case GENDER:
                p.setGender(att.getAttributeDescriptionCode());
                break;
            }
        }

        for (;;) {
            DTM dtm = getConditionalSegment(DTM.class);
            if (dtm == null) {
                break;
            }
            DtmCode dtmCode = dtm.getDtmCode();
            if (dtmCode == DtmCode.DATE_OF_BIRTH) {
                Date dob = dtm.getDtmValue();
                if (dob != null) {
                    p.setDob(dob);
                    p.setAge(ParseUtils.calculateAge(dob));
                }
            }
        }

        for (;;) {
            MEA mea = getConditionalSegment(MEA.class);
            if (mea == null) {
                break;
            }
        }

        for (;;) {
            GEI gei = getConditionalSegment(GEI.class);
            if (gei == null) {
                break;
            }
        }

        for (;;) {
            FTX ftx = getConditionalSegment(FTX.class);
            if (ftx == null) {
                break;
            }
        }

        for (;;) {
            LOC loc = getConditionalSegment(LOC.class);
            if (loc == null) {
                break;
            }

            String val = loc.getLocationNameCode();
            switch (loc.getFunctionCode()) {
            case PORT_OF_DEBARKATION:
                p.setDebarkation(val);
                break;
            case PORT_OF_EMBARKATION:
                p.setEmbarkation(val);
                break;
            case AIRPORT_OF_FIRST_US_ARRIVAL:
                // TODO: not sure how to handle this.
                break;
            case COUNTRY_OF_RESIDENCE:
                p.setResidencyCountry(val);
                break;
            case PLACE_OF_BIRTH:
                // TODO: we don't have a field for this
                break;
            }
        }

        getConditionalSegment(COM.class);
        
        for (;;) {
            EMP emp = getConditionalSegment(EMP.class);
            if (emp == null) {
                break;
            }
        }

        for (;;) {
            NAT nat = getConditionalSegment(NAT.class);
            if (nat == null) {
                break;
            }
            p.setCitizenshipCountry(nat.getNationalityCode());
        }

        for (;;) {
            RFF rff = getConditionalSegment(RFF.class);
            if (rff == null) {
                break;
            }
        }

        for (;;) {
            DOC doc = getConditionalSegment(DOC.class);
            if (doc == null) {
                break;
            }
            processDocument(p, doc);
        }
        
        // TODO: implement segment group 6
        // TODO: implement segment group 7
    }

    /**
     * Segment group 5: Passenger documents
     */
    private void processDocument(PassengerVo p, DOC doc) throws ParseException {
        DocumentVo d = new DocumentVo();
        p.addDocument(d);
        d.setDocumentType(doc.getDocCode());
        d.setDocumentNumber(doc.getDocumentIdentifier());
        
        for (;;) {
            DTM dtm = getConditionalSegment(DTM.class);
            if (dtm == null) {
                break;
            }
            DtmCode dtmCode = dtm.getDtmCode();
            if (dtmCode == DtmCode.PASSPORT_EXPIRATION_DATE) {
                d.setExpirationDate(dtm.getDtmValue());
            }
        }
        
        for (;;) {
            GEI gei = getConditionalSegment(GEI.class);
            if (gei == null) {
                break;
            }
        }        

        for (;;) {
            RFF rff = getConditionalSegment(RFF.class);
            if (rff == null) {
                break;
            }
        }        

        for (;;) {
            LOC loc = getConditionalSegment(LOC.class);
            if (loc == null) {
                break;
            }
            LocCode locCode = loc.getFunctionCode();
            if (locCode == LocCode.PLACE_OF_DOCUMENT_ISSUE) {
                d.setIssuanceCountry(loc.getLocationNameCode());

                if (p.getCitizenshipCountry() == null) {
                    // wasn't set by NAD:LOC, so derive it here from issuance country
                    if ("P".equals(d.getDocumentType())) {
                        p.setCitizenshipCountry(d.getIssuanceCountry());
                    }
                }
            }
        }
        
        getConditionalSegment(CPI.class);

        for (;;) {
            QTY qty = getConditionalSegment(QTY.class);
            if (qty == null) {
                break;
            }
        }   
    }
}
