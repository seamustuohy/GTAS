package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.Date;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.edifact.segment.UNB;
import gov.gtas.parsers.edifact.segment.UNH;
import gov.gtas.parsers.paxlst.segment.unedifact.ATT;
import gov.gtas.parsers.paxlst.segment.unedifact.BGM;
import gov.gtas.parsers.paxlst.segment.unedifact.COM;
import gov.gtas.parsers.paxlst.segment.unedifact.CTA;
import gov.gtas.parsers.paxlst.segment.unedifact.DOC;
import gov.gtas.parsers.paxlst.segment.unedifact.DTM;
import gov.gtas.parsers.paxlst.segment.unedifact.DTM.DtmCode;
import gov.gtas.parsers.paxlst.segment.unedifact.LOC;
import gov.gtas.parsers.paxlst.segment.unedifact.LOC.LocCode;
import gov.gtas.parsers.paxlst.segment.unedifact.NAD;
import gov.gtas.parsers.paxlst.segment.unedifact.RFF;
import gov.gtas.parsers.paxlst.segment.unedifact.TDT;
import gov.gtas.parsers.paxlst.vo.DocumentVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.paxlst.vo.ReportingPartyVo;

public final class PaxlstParserUNedifact extends PaxlstParser {
    private static final Logger logger = LoggerFactory.getLogger(PaxlstParserUNedifact.class);
    private SegmentFactory paxlstFactory;

    public PaxlstParserUNedifact(String message) {
        super(message);
        this.paxlstFactory = new SegmentFactory(NAD.class.getPackage().getName());
    }

    private SegmentFactory getFactory(String segmentName) {
        switch (segmentName) {
        case "UNA":
        case "UNB":
        case "UNG":
        case "UNH":
        case "UNT":
        case "UNE":
        case "UNZ":
            return edifactFactory;
        default:
           return paxlstFactory;
        }
    }
    
    private Segment getMandatorySegment(ListIterator<Segment> i, String segmentName) throws ParseException {
        if (i.hasNext()) {
            Segment s = i.next();
            SegmentFactory factory = getFactory(segmentName);
            if (s.getName().equals(segmentName)) {
                return factory.build(s);
            } else {
                throw new ParseException("Unexpected " + s.getName() + " segment: " + s, -1);
            }
        }

        throw new ParseException("No segments left! ", -1);
    }

    private Segment getConditionalSegment(ListIterator<Segment> i, String segmentName) throws ParseException {
        if (i.hasNext()) {
            Segment s = i.next();
            SegmentFactory factory = getFactory(segmentName);
            if (s.getName().equals(segmentName)) {
                return factory.build(s);
            } else {
                i.previous();
                return null;
            }
        }

        return null;
    }

    @Override
    public void parseSegments() throws ParseException {
        ListIterator<Segment> i = segments.listIterator();
        UNB unb = (UNB) getMandatorySegment(i, "UNB");
        parsedMessage.setTransmissionSource(unb.getSenderIdentification());
        parsedMessage.setTransmissionDate(unb.getDateAndTimeOfPreparation());

        Segment s = getConditionalSegment(i, "UNG");

        UNH unh = (UNH) getMandatorySegment(i, "UNH");
        parsedMessage.setMessageType(unh.getMessageType());
        parsedMessage.setVersion(unh.getMessageTypeVersion());

        BGM bgm = (BGM) getMandatorySegment(i, "BGM");
        parsedMessage.setMessageCode(bgm.getCode());

        s = getConditionalSegment(i, "RFF");
        if (s != null) {
            RFF rff = (RFF) s;
        }

        for (;;) {
            s = getConditionalSegment(i, "DTM");
            if (s == null) {
                break;
            }
            DTM dtm = (DTM) s;
        }

        for (;;) {
            s = getConditionalSegment(i, "NAD");
            if (s == null) {
                break;
            }
            processReportingParty((NAD) s, i);
        }

        // at least one TDT is mandatory
        TDT tdt = (TDT) getMandatorySegment(i, "TDT");
        processFlight(tdt, i);
        for (;;) {
            s = getConditionalSegment(i, "TDT");
            if (s == null) {
                break;
            }
            processFlight((TDT) s, i);
        }
        
        for (;;) {
            s = getConditionalSegment(i, "NAD");
            if (s == null) {
                break;
            }
            processPax((NAD) s, i);
        }       
    }

    /**
     * Segment group 1: reporting party
     */
    private void processReportingParty(NAD nad, ListIterator<Segment> i) throws ParseException {
        ReportingPartyVo rp = new ReportingPartyVo();
        parsedMessage.addReportingParty(rp);
        rp.setPartyName(nad.getPartyName());

        Segment nextSeg = getConditionalSegment(i, "CTA");
        if (nextSeg != null) {
            CTA cta = (CTA) nextSeg;
        }

        for (;;) {
            nextSeg = getConditionalSegment(i, "COM");
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
    private void processFlight(TDT tdt, ListIterator<Segment> i) throws ParseException {
        String dest = null;
        String origin = null;
        Date eta = null;
        Date etd = null;
        boolean loc92Seen = false;

        for (;;) {
            Segment s = getConditionalSegment(i, "DTM");
            if (s == null) {
                break;
            }
        }       
        
        // Segment group 3: loc-dtm loop
        for (;;) {
            Segment s = getConditionalSegment(i, "LOC");
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
            Segment nextSeg = getConditionalSegment(i, "DTM");
            if (nextSeg != null) {
                DTM dtm = (DTM) nextSeg;
                Date d = dtm.getDtmValue();
                DtmCode dtmCode = dtm.getDtmCodeQualifier();
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
    private void processPax(NAD nad, ListIterator<Segment> i) throws ParseException {
        PaxVo p = new PaxVo();
        parsedMessage.addPax(p);

        p.setFirstName(nad.getFirstName());
        p.setLastName(nad.getLastName());
        p.setMiddleName(nad.getMiddleName());
        p.setPaxType(nad.getNadCode().getCode());

        Segment s = null;

        for (;;) {
            s = getConditionalSegment(i, "ATT");
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
            s = getConditionalSegment(i, "DTM");
            if (s == null) {
                break;
            }
            DTM dtm = (DTM) s;
            DtmCode dtmCode = dtm.getDtmCodeQualifier();
            if (dtmCode == DtmCode.DATE_OF_BIRTH) {
                p.setDob(dtm.getDtmValue());
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "MEA");
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "GEI");
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "FTX");
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "LOC");
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
            }
        }

        s = getConditionalSegment(i, "COM");
        if (s != null) {
            COM com = (COM) s;
        }
        
        for (;;) {
            s = getConditionalSegment(i, "EMP");
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "NAT");
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "RFF");
            if (s == null) {
                break;
            }
        }

        for (;;) {
            s = getConditionalSegment(i, "DOC");
            if (s == null) {
                break;
            }
            processDocument(p, (DOC) s, i);
        }
        
        // TODO: implement segment group 6
        // TODO: implement segment group 7
    }

    /**
     * Segment group 5: Passenger documents
     */
    private void processDocument(PaxVo p, DOC doc, ListIterator<Segment> i) throws ParseException {
        DocumentVo d = new DocumentVo();
        p.addDocument(d);
        d.setDocumentType(doc.getDocCode());
        d.setDocumentNumber(doc.getDocumentIdentifier());
        
        Segment s = null;
        for (;;) {
            s = getConditionalSegment(i, "DTM");
            if (s == null) {
                break;
            }
            DTM dtm = (DTM) s;
            DtmCode dtmCode = dtm.getDtmCodeQualifier();
            if (dtmCode == DtmCode.PASSPORT_EXPIRATION_DATE) {
                d.setExpirationDate(dtm.getDtmValue());
            }
        }
        
        for (;;) {
            s = getConditionalSegment(i, "GEI");
            if (s == null) {
                break;
            }
        }        

        for (;;) {
            s = getConditionalSegment(i, "RFF");
            if (s == null) {
                break;
            }
        }        

        for (;;) {
            s = getConditionalSegment(i, "LOC");
            if (s == null) {
                break;
            }
            LOC loc = (LOC) s;
            LocCode locCode = loc.getFunctionCode();
            if (locCode == LocCode.PLACE_OF_DOCUMENT_ISSUE) {
                d.setIssuanceCountry(loc.getLocationNameCode());
            }            
        }
        
        s = getConditionalSegment(i, "CPI");
        if (s != null) {
            
        }

        for (;;) {
            s = getConditionalSegment(i, "QTY");
            if (s == null) {
                break;
            }
        }   
    }
}
