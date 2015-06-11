package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Document;
import gov.cbp.taspd.gtas.model.DocumentCode;
import gov.cbp.taspd.gtas.model.Gender;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.model.ReportingParty;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.ATT;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.COM;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.DOC;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.DTM;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.DTM.DtmCode;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.FTX;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.GEI;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.LOC;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.LOC.LocCode;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.NAD;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.NAT;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.TDT;
import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.UNB;

import java.util.ListIterator;

public final class PaxlstParserUNedifact extends PaxlstParser {

    public PaxlstParserUNedifact(String filePath) {
        super(filePath, UNB.class.getPackage().getName());
    }
    
    public void parseSegments() {
        currentGroup = GROUP.NONE;
        
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
            System.out.println(s);

            switch (s.getName()) {
            case "UNB":
                if (currentGroup == GROUP.NONE) {
                    currentGroup = GROUP.HEADER;
                    processHeader(s, i);
                } else {
                    System.err.println("unexpected segment ordering");
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
                    System.err.println("unexpected segment ordering");
                    return;                    
                }
                break;
            
            case "TDT":
                if (currentGroup == GROUP.HEADER || currentGroup == GROUP.REPORTING_PARTY) {
                    currentGroup = GROUP.FLIGHT;
                    processFlight(s, i);
                } else {
                    System.err.println("unexpected segment ordering");
                    return;                    
                }
                
                break;
                
            case "UNZ":
                currentGroup = GROUP.NONE;
                break;
            }
        }       
    }
    
    private void processReportingParty(Segment seg, ListIterator<Segment> i) {
        NAD nad = (NAD)seg;        
        ReportingParty rp = new ReportingParty();
        message.getReportingParties().add(rp);
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
        Pax p = new Pax();
        passengers.add(p);

        NAD nad = (NAD)seg;
        p.setFirstName(nad.getFirstName());
        p.setLastName(nad.getLastName());
        p.setMiddleName(nad.getMiddleName());
        
        for (;;) {
            Segment s = i.next();
            if (s == null) return;
            System.out.println("\t" + s);
            switch (s.getName()) {
            
            case "ATT":
                ATT att = (ATT)s;
                p.setGender(Gender.valueOf(att.getAttributeDescriptionCode()));
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
    
    private void processDocument(Pax p, Segment seg, ListIterator<Segment> i) {
        DOC doc = (DOC)seg;
        Document d = new Document();
        p.getDocuments().add(d);
        d.setDocumentType(DocumentCode.valueOf(doc.getDocCode()));
        d.setNumber(doc.getDocumentIdentifier());

        for (;;) {
            Segment s = i.next();
            if (s == null) return;
            System.out.println("\t" + "\t" + s);
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
//                    d.setIssuanceCountry(loc.getLocationNameCode());
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
        this.flight.setFlightNumber(tdt.getC_journeyIdentifier());
    }
    
    private void processHeader(Segment seg, ListIterator<Segment> i) {
        UNB unb = (UNB)seg;
        
        for (;;) {
            Segment s = i.next();
            if (s == null) return;
            System.out.println("\t" + s);
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
