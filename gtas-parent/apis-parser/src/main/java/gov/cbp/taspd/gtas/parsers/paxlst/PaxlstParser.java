package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.parsers.edifact.EdifactParser;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.util.FileUtils;
import gov.cbp.taspd.gtas.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class PaxlstParser {
    private String filePath;
    private String segmentPackageName;
    
    protected ApisMessage message;
    protected List<Segment> segments;
    protected Flight flight;
    protected Set<Pax> passengers;
    protected int currentGroup;
    
    public PaxlstParser(String filePath, String segmentPackageName) {
        this.filePath = filePath;
        this.segmentPackageName = segmentPackageName;
    }

    public abstract void parseSegments();
    
    public ApisMessage parse() {
        this.segments = new LinkedList<>();
        this.flight = new Flight();
        this.passengers = new HashSet<>();
        this.flight.setPassengers(passengers);
        this.message = new ApisMessage();

        byte[] raw = FileUtils.readSmallFile(this.filePath);
        if (raw == null) {
            return null;
        }
        
        this.message.setRaw(raw);
        String msg = new String(raw, StandardCharsets.US_ASCII);
        processMessageAndGetSegments(msg);
        parseSegments();
        
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
