package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Gender;
import gov.cbp.taspd.gtas.model.Message;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.model.ReportingParty;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.ATT;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.COM;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.DTM;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.LOC;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.NAD;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.TDT;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;
import gov.cbp.taspd.gtas.parsers.unedifact.SegmentFactory;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNA;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNB;
import gov.cbp.taspd.gtas.util.FileUtils;
import gov.cbp.taspd.gtas.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class PaxlstParser {
    private UNA serviceStrings;
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
        
        int unaIndex = txt.indexOf("UNA");
        if (unaIndex != -1) {
            int endIndex = unaIndex + "UNA".length() + 6;
            String delims = txt.substring(unaIndex, endIndex);
            serviceStrings = new UNA(delims);
        } else {
            serviceStrings = new UNA();
        }

        int unbIndex = txt.indexOf("UNB");
        if (unbIndex == -1) {
            System.err.println("no UNB segment");
            System.exit(0);
        }
        txt = txt.substring(unbIndex);
        
        txt = txt.replaceAll("\\n|\\r|\\t", "");

        SegmentFactory factory = new SegmentFactory(serviceStrings);
        segments = new LinkedList<>();
        String segmentRegex = String.format("\\%c", serviceStrings.getSegmentTerminator());
        String[] stringSegments = txt.split(segmentRegex);
        for (String s : stringSegments) {
            segments.add(factory.build(s));
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
                String tmp = att.getAttributeDescriptionCode();
                Gender g;
                if (tmp.equals("M")) {
                    g = Gender.MALE;
                } else {
                    g = Gender.FEMALE;
                }
                p.setGender(g);
                break;
                
            case "DTM":
                DTM dtm = (DTM)s;
                p.setDob(dtm.getDtmValue());
                break;
                
            case "GEI":
            case "FTX":
                break;
                
            case "LOC":
                LOC loc = (LOC)s;
                //if (loc.get)
                break;
                
            case "COM":
            case "EMP":
            case "NAT":
            case "RFF":
            case "DOC":
                break;
            default:
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
