package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Document;
import gov.cbp.taspd.gtas.model.DocumentCode;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Gender;
import gov.cbp.taspd.gtas.model.Message;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.model.ReportingParty;
import gov.cbp.taspd.gtas.parsers.edifact.EdifactParser;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.parsers.edifact.segments.UNB;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.ATT;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.COM;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.DOC;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.DTM;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.FTX;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.GEI;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.LOC;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.NAD;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.NAT;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.TDT;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.DTM.DtmCode;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.LOC.LocCode;
import gov.cbp.taspd.gtas.util.FileUtils;
import gov.cbp.taspd.gtas.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class PaxlstParser {
    private String filePath;
    private List<Segment> segments;
    private ApisMessage message;
    private Flight flight;
    private Set<Pax> passengers;
    private int currentGroup;
    
    public PaxlstParser(String filePath) {
        this.filePath = filePath;
    }
    
    public Message parse() {
        byte[] raw = FileUtils.readSmallFile(this.filePath);
        String msg = new String(raw, StandardCharsets.US_ASCII);
        
        this.segments = new LinkedList<>();
        this.flight = new Flight();
        this.passengers = new HashSet<>();
        this.flight.setPassengers(passengers);

        this.message = new ApisMessage();
        this.message.setRaw(raw);

        processRawAndGetSegments(msg);
        processSegments();
        
        for (Pax x : this.passengers) {
            System.out.println(x.getLastName() + ", " + x.getFirstName() + " " + x.getMiddleName());
        }
        
        return this.message;
    }
    
    private void processRawAndGetSegments(String raw) {
        String txt = ParseUtils.stripHeaderAndFooter(raw);
        txt = txt.toUpperCase();
        txt = txt.replaceAll("\\n|\\r|\\t", "");
        
        SegmentFactory factory = new SegmentFactory();
        EdifactParser p = new EdifactParser();
        LinkedList<Segment> edifactSegments = p.parse(txt);
        for (Segment s: edifactSegments) {
            Segment paxlstSegment = factory.build(s);
            segments.add(paxlstSegment);
        }
    }

    private void processSegments() {
        currentGroup = 0;
        
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
            System.out.println(s);
            switch (s.getName()) {
            case "UNB":
                processUnb(s);
                break;
            case "UNH":
                break;
            case "NAD":
                if (currentGroup == 0) {
                    currentGroup = 1;
                    processReportingParty(s, i);
                } else {
                    currentGroup = 4;
                    processPax(s, i);
                }
                break;
            case "TDT":
                currentGroup = 2;
//                processFlight(s, i);
                break;
            case "UNZ":
                // done
                return;
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
                currentGroup = 5;
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
                currentGroup = 4;
                return;
            }
        }
    }

    private void processFlight(Segment seg, ListIterator<Segment> i) {
        TDT tdt = (TDT)seg;
        this.flight.setFlightNumber(tdt.getC_journeyIdentifier());
    }
    
    private void processUnb(Segment seg) {
        UNB unb = (UNB)seg;
//        message.setSender(unb.getSenderIdentification());
//        message.setReceiver(unb.getRecipientIdentification());
    }
    
    public static void main(String[] argv) {        
        if (argv.length < 1) {
            System.out.println("usage: EdifactParser [filename]");
            System.exit(0);
        }

        PaxlstParser parser = new PaxlstParser(argv[0]);
        Message m = parser.parse();     
    }
}
