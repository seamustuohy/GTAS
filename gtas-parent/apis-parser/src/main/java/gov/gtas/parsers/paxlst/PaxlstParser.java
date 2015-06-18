package gov.gtas.parsers.paxlst;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.ReportingParty;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.util.FileUtils;
import gov.gtas.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class PaxlstParser {
    private String filePath;
    private String segmentPackageName;

    protected enum GROUP {
        NONE,
        HEADER,
        REPORTING_PARTY,
        FLIGHT,
        PAX
    }
    
    protected GROUP currentGroup;

    protected ApisMessage message;
    protected List<Segment> segments;
    protected Set<Flight> flights;
    protected Set<Pax> passengers;
    
    public PaxlstParser(String filePath, String segmentPackageName) {
        this.filePath = filePath;
        this.segmentPackageName = segmentPackageName;
    }

    public abstract void parseSegments();
    
    public ApisMessage parse() {
        this.segments = new LinkedList<>();
        this.flights = new HashSet<>();
        this.passengers = new HashSet<>();
        this.message = new ApisMessage();

        byte[] raw = FileUtils.readSmallFile(this.filePath);
        if (raw == null) {
            return null;
        }
        
        this.message.setRaw(raw);
        String msg = new String(raw, StandardCharsets.US_ASCII);
        processMessageAndGetSegments(msg);
        parseSegments();
        
        this.message.setFlights(this.flights);
        
        for (ReportingParty rp : this.message.getReportingParties()) {
            System.out.println(rp);
        }
        for (Flight f : this.flights) {
            System.out.println(f);
        }
        for (Pax p : this.passengers) {
            System.out.println(p);
        }

        return this.message;
    }
    
    private void processMessageAndGetSegments(String msg) {
        String txt = ParseUtils.stripHeaderAndFooter(msg);
        txt = txt.toUpperCase();
        txt = txt.replaceAll("\\n|\\r|\\t", "");
        
        SegmentFactory factory = new SegmentFactory(segmentPackageName);
        EdifactParser p = new EdifactParser();
        LinkedList<Segment> edifactSegments = p.parse(txt);
        for (Segment s: edifactSegments) {
            Segment paxlstSegment = factory.build(s);
            segments.add(paxlstSegment);
        }
    }
}
