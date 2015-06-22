package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

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

    protected ApisMessageVo message;
    protected List<Segment> segments;
    
    public PaxlstParser(String filePath, String segmentPackageName) {
        this.filePath = filePath;
        this.segmentPackageName = segmentPackageName;
    }

    public abstract void parseSegments();
    
    public ApisMessageVo parse() {
        this.segments = new LinkedList<>();
        this.message = new ApisMessageVo();

        byte[] raw = FileUtils.readSmallFile(this.filePath);
        if (raw == null) {
            return null;
        }
        
//        this.message.setRaw(raw);
        String msg = new String(raw, StandardCharsets.US_ASCII);
        processMessageAndGetSegments(msg);
        parseSegments();
        
//        for (ReportingParty rp : this.message.getReportingParties()) {
//            System.out.println(rp);
//        }
//        for (Flight f : this.flights) {
//            System.out.println(f);
//        }
//        for (Pax p : this.passengers) {
//            System.out.println(p);
//        }
        System.out.println(this.message);

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
