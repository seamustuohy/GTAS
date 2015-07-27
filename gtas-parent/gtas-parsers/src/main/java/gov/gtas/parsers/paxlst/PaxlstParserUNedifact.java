package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.edifact.segment.UNB;
import gov.gtas.parsers.edifact.segment.UNG;
import gov.gtas.parsers.edifact.segment.UNH;
import gov.gtas.parsers.paxlst.segment.unedifact.ATT;
import gov.gtas.parsers.paxlst.segment.unedifact.BGM;
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
import gov.gtas.parsers.paxlst.vo.DocumentVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.paxlst.vo.ReportingPartyVo;

public final class PaxlstParserUNedifact extends PaxlstParser {
    private static final Logger logger = LoggerFactory.getLogger(PaxlstParserUNedifact.class);
    
    private static final String[] SEGMENT_NAMES = new String[] { "ATT", "AUT", "BGM", "CNT", "COM", "CPI", "CTA", "DOC",
            "DTM", "EMP", "FTX", "GEI", "GID", "LOC", "MEA", "NAD", "NAT", "QTY", "RFF", "TDT", "UNH", "UNT" };
    public static final Set<String> UN_EDIFACT_SEGMENT_INDEX = new HashSet<>(Arrays.asList(SEGMENT_NAMES));

    public PaxlstParserUNedifact() {
    }

    protected void validateSegmentName(String segmentName) throws ParseException {
        boolean valid = UN_EDIFACT_SEGMENT_INDEX.contains(segmentName) 
                || EdifactParser.EDIFACT_SEGMENT_INDEX.contains(segmentName);
        if (!valid) {
            throw new ParseException("Invalid segment: " + segmentName, -1);
        }
    }
    
    @Override
    public void parseSegments() throws ParseException {
        UNB unb = (UNB) getMandatorySegment(UNB.class);
        parsedMessage.setTransmissionSource(unb.getSenderIdentification());
        parsedMessage.setTransmissionDate(unb.getDateAndTimeOfPreparation());

        Segment s = getConditionalSegment(UNG.class);

        UNH unh = (UNH) getMandatorySegment(UNH.class);
        parsedMessage.setMessageType(unh.getMessageType());
        parsedMessage.setVersion(unh.getMessageTypeVersion());

        BGM bgm = (BGM) getMandatorySegment(BGM.class);
        parsedMessage.setMessageCode(bgm.getCode());

        s = getConditionalSegment(RFF.class);

        for (;;) {
            s = getConditionalSegment(DTM.class);
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(NAD.class);
            if (s == null) {
                break;
            }
            processReportingParty((NAD) s);
        }

        // at least one TDT is mandatory
        TDT tdt = (TDT) getMandatorySegment(TDT.class);
        processFlight(tdt);
        for (;;) {
            s = getConditionalSegment(TDT.class);
            if (s == null) {
                break;
            }
            processFlight((TDT) s);
        }
        
        for (;;) {
            s = getConditionalSegment(NAD.class);
            if (s == null) {
                break;
            }
            processPax((NAD) s);
        }       
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

        Segment nextSeg = getConditionalSegment(CTA.class);

        for (;;) {
            nextSeg = getConditionalSegment(COM.class);
            if (nextSeg == null) {
                break;
            }
            COM com = (COM) nextSeg;
            rp.setTelephone(com.getPhoneNumber());
            rp.setFax(com.getFaxNumber());
        }
    }

    /**
     * Segment group 2: flight details
     */
    private void processFlight(TDT tdt) throws ParseException {
        String dest = null;
        String origin = null;
        Date eta = null;
        Date etd = null;
        boolean loc92Seen = false;

        for (;;) {
            Segment s = getConditionalSegment(DTM.class);
            if (s == null) {
                break;
            }
        }       
        
        // Segment group 3: loc-dtm loop
        for (;;) {
            Segment s = getConditionalSegment(LOC.class);
            if (s == null) {
                break;
            }

            LOC loc = (LOC) s;
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
                            + LocCode.BOTH_DEPARTURE_AND_ARRIVAL_AIRPORT, -1);
                }
                break;
            }

            // get corresponding DTM, if it exists
            Segment nextSeg = getConditionalSegment(DTM.class);
            if (nextSeg != null) {
                DTM dtm = (DTM) nextSeg;
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
                f.setFlightNumber(tdt.getFlightNumber());
                f.setCarrier(tdt.getC_carrierIdentifier());
                f.setOrigin(origin);
                f.setDestination(dest);
                f.setEta(eta);
                f.setEtd(etd);

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
        PaxVo p = new PaxVo();
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
        p.setPaxType(paxType);

        Segment s = null;

        for (;;) {
            s = getConditionalSegment(ATT.class);
            if (s == null) {
                break;
            }
            ATT att = (ATT) s;
            switch (att.getFunctionCode()) {
            case GENDER:
                p.setGender(att.getAttributeDescriptionCode());
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(DTM.class);
            if (s == null) {
                break;
            }
            DTM dtm = (DTM) s;
            DtmCode dtmCode = dtm.getDtmCode();
            if (dtmCode == DtmCode.DATE_OF_BIRTH) {
                p.setDob(dtm.getDtmValue());
            }
        }

        for (;;) {
            s = getConditionalSegment(MEA.class);
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(GEI.class);
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(FTX.class);
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(LOC.class);
            if (s == null) {
                break;
            }

            LOC loc = (LOC) s;
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

        s = getConditionalSegment(COM.class);
        
        for (;;) {
            s = getConditionalSegment(EMP.class);
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(NAT.class);
            if (s == null) {
                break;
            }
            NAT nat = (NAT)s;
            p.setCitizenshipCountry(nat.getNationalityCode());
        }

        for (;;) {
            s = getConditionalSegment(RFF.class);
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(DOC.class);
            if (s == null) {
                break;
            }
            processDocument(p, (DOC) s);
        }
        
        // TODO: implement segment group 6
        // TODO: implement segment group 7
    }

    /**
     * Segment group 5: Passenger documents
     */
    private void processDocument(PaxVo p, DOC doc) throws ParseException {
        DocumentVo d = new DocumentVo();
        p.addDocument(d);
        d.setDocumentType(doc.getDocCode());
        d.setDocumentNumber(doc.getDocumentIdentifier());
        
        Segment s = null;
        for (;;) {
            s = getConditionalSegment(DTM.class);
            if (s == null) {
                break;
            }
            DTM dtm = (DTM) s;
            DtmCode dtmCode = dtm.getDtmCode();
            if (dtmCode == DtmCode.PASSPORT_EXPIRATION_DATE) {
                d.setExpirationDate(dtm.getDtmValue());
            }
        }
        
        for (;;) {
            s = getConditionalSegment(GEI.class);
            if (s == null) {
                break;
            }
        }        

        for (;;) {
            s = getConditionalSegment(RFF.class);
            if (s == null) {
                break;
            }
        }        

        for (;;) {
            s = getConditionalSegment(LOC.class);
            if (s == null) {
                break;
            }
            LOC loc = (LOC) s;
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
        
        s = getConditionalSegment(CPI.class);
        if (s != null) {
            
        }

        for (;;) {
            s = getConditionalSegment(QTY.class);
            if (s == null) {
                break;
            }
        }   
    }
}
