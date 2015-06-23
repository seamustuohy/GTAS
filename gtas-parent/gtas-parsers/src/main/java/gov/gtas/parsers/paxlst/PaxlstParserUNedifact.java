package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.unedifact.ATT;
import gov.gtas.parsers.paxlst.unedifact.COM;
import gov.gtas.parsers.paxlst.unedifact.DOC;
import gov.gtas.parsers.paxlst.unedifact.DTM;
import gov.gtas.parsers.paxlst.unedifact.DTM.DtmCode;
import gov.gtas.parsers.paxlst.unedifact.FTX;
import gov.gtas.parsers.paxlst.unedifact.GEI;
import gov.gtas.parsers.paxlst.unedifact.LOC;
import gov.gtas.parsers.paxlst.unedifact.LOC.LocCode;
import gov.gtas.parsers.paxlst.unedifact.NAD;
import gov.gtas.parsers.paxlst.unedifact.NAT;
import gov.gtas.parsers.paxlst.unedifact.TDT;
import gov.gtas.parsers.paxlst.unedifact.UNB;
import gov.gtas.parsers.paxlst.vo.DocumentVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.paxlst.vo.ReportingPartyVo;

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PaxlstParserUNedifact extends PaxlstParser {
    private static final Logger logger = LoggerFactory.getLogger(EdifactParser.class);

    public PaxlstParserUNedifact(String filePath) {
        super(filePath, UNB.class.getPackage().getName());
    }
    
    public void parseSegments() {
        currentGroup = GROUP.NONE;
        
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
//            System.out.println(s);

            switch (s.getName()) {
            case "UNB":
                if (currentGroup == GROUP.NONE) {
                    currentGroup = GROUP.HEADER;
//                    processHeader(s, i);
                } else {
                    handleUnexpectedSegment(s);
                    return;
                }
                break;
                
            case "NAD":
                if (currentGroup == GROUP.HEADER || currentGroup == GROUP.REPORTING_PARTY) {
                    currentGroup = GROUP.REPORTING_PARTY;
                    processReportingParty(s, i);
                } else if (currentGroup == GROUP.FLIGHT || currentGroup == GROUP.PAX) {
                    currentGroup = GROUP.PAX;
                    processPax(s, i);
                } else {
                    handleUnexpectedSegment(s);
                    return;                    
                }
                break;
            
            case "TDT":
                if (currentGroup == GROUP.HEADER 
                    || currentGroup == GROUP.REPORTING_PARTY
                    || currentGroup == GROUP.FLIGHT) {
                    
                    currentGroup = GROUP.FLIGHT;
                    processFlight(s, i);
                } else {
                    handleUnexpectedSegment(s);
                    return;                    
                }
                
                break;
                
            case "UNZ":
                currentGroup = GROUP.NONE;
                break;
            }
        }       
    }
    
    private void handleUnexpectedSegment(Segment s) {
        System.err.println("unexpected segment " + s);
    }

    private void processReportingParty(Segment seg, ListIterator<Segment> i) {
        NAD nad = (NAD)seg;        
        ReportingPartyVo rp = new ReportingPartyVo();
        message.addReportingParty(rp);
        rp.setPartyName(nad.getPartyName());

        Segment nextSeg = i.next();
        if (nextSeg.getName().equals("COM")) {
            // optional COM segment
            COM com = (COM)nextSeg;
            rp.setTelephone(com.getPhoneNumber());
            rp.setFax(com.getFaxNumber());
        } else {
            i.previous();
        }
    }
    
    private void processPax(Segment seg, ListIterator<Segment> i) {
        PaxVo p = new PaxVo();
        message.addPax(p);

        NAD nad = (NAD)seg;
        p.setFirstName(nad.getFirstName());
        p.setLastName(nad.getLastName());
        p.setMiddleName(nad.getMiddleName());
        p.setPaxType(nad.getPartyFunctionCodeQualifier().toString());
        
        while (i.hasNext()) {
            Segment s = i.next();
//            System.out.println("\t" + s);
            switch (s.getName()) {
            
            case "ATT":
                ATT att = (ATT)s;
                p.setGender(att.getAttributeDescriptionCode());
                break;
                
            case "DTM":
                DTM dtm = (DTM)s;
                DtmCode dtmCode = dtm.getDtmCodeQualifier();
                if (dtmCode == DtmCode.DATE_OF_BIRTH) {
                    p.setDob(dtm.getDtmValue());
                }
                break;
                
            case "GEI":
                GEI gei = (GEI)s;
                break;
            case "FTX":
                FTX ftx = (FTX)s;
                break;
            case "LOC":
                LOC loc = (LOC)s;
                LocCode locCode = loc.getFunctionCode();
                String val = loc.getLocationNameCode();
                if (locCode == LocCode.PORT_OF_DEBARKATION) {
                    p.setDebarkation(val);
                } else if (locCode == LocCode.PORT_OF_EMBARKATION) {
                    p.setEmbarkation(val);
                }
                break;
            case "COM":
                COM com = (COM)s;
            case "EMP":
                break;
            case "NAT":
                NAT nat = (NAT)s;
                break;
            case "RFF":
                break;
                
            case "DOC":
                processDocument(p, s, i);
                // should be done with pax
                return;
    
            default:
                i.previous();                
                return;
            }
        }
    }
    
    private void processDocument(PaxVo p, Segment seg, ListIterator<Segment> i) {
        DOC doc = (DOC)seg;
        DocumentVo d = new DocumentVo();
        p.addDocument(d);
        d.setDocumentType(doc.getDocCode());
        d.setDocumentNumber(doc.getDocumentIdentifier());

        while (i.hasNext()) {
            Segment s = i.next();
//            System.out.println("\t" + "\t" + s);
            switch (s.getName()) {
            case "DTM":
                DTM dtm = (DTM)s;
                DtmCode dtmCode = dtm.getDtmCodeQualifier();
                if (dtmCode == DtmCode.PASSPORT_EXPIRATION_DATE) {
                    d.setExpirationDate(dtm.getDtmValue());
                }
                break;
            case "LOC":
                LOC loc = (LOC)s;
                LocCode locCode = loc.getFunctionCode();
                if (locCode == LocCode.PLACE_OF_DOCUMENT_ISSUE) {
                    d.setIssuanceCountry(loc.getLocationNameCode());
                }
                break;
            default:
                i.previous();
                return;
            }
        }
    }

    private void processFlight(Segment seg, ListIterator<Segment> i) {
        TDT tdt = (TDT)seg;
        FlightVo f = new FlightVo();
        message.addFlight(f);
        f.setFlightNumber(tdt.getC_journeyIdentifier());
        
        String tmp = tdt.getC_carrierIdentifier();
        if (tmp != null) {
            f.setCarrier(tmp);
        } else {
            // assume first 2 letters of flight number is carrier iata code
            String iata = tdt.getC_journeyIdentifier().substring(0, 2);
            f.setCarrier(iata);
        }
        
        while (i.hasNext()) {
            Segment s = i.next();
//            System.out.println("\t" + s);
            switch (s.getName()) {
            case "LOC":
                LOC loc = (LOC)s;
                LocCode locCode = loc.getFunctionCode();
                String airport = loc.getLocationNameCode();
                if (locCode == LocCode.DEPARTURE_AIRPORT) {
                    f.setOrigin(airport);
                } else if (locCode == LocCode.ARRIVAL_AIRPORT) {
                    f.setDestination(airport);
                } else if (locCode == LocCode.BOTH_DEPARTURE_AND_ARRIVAL_AIRPORT) {
                    f.setOrigin(airport);
                    f.setDestination(airport);
                }
                break;
            case "DTM":
                DTM dtm = (DTM)s;
                DtmCode dtmCode = dtm.getDtmCodeQualifier();
                if (dtmCode == DtmCode.DEPARTURE) {
                    f.setEtd(dtm.getDtmValue());
                } else if (dtmCode == DtmCode.ARRIVAL) {
                    f.setEta(dtm.getDtmValue());
                }
                break;
            default:
                i.previous();
                return;
            }
        }
    }
    
    private void processHeader(Segment seg, ListIterator<Segment> i) {
        UNB unb = (UNB)seg;
        
        while (i.hasNext()) {
            Segment s = i.next();
//            System.out.println("\t" + s);
            switch (s.getName()) {
            case "UNG":
                break;
            case "UNH":
                break;
            case "BGM":
                break;
            case "RFF":
                break;
            default:
                i.previous();
                return;
            }
        }
    }
}
