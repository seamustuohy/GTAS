package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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

    protected abstract void parseSegments();
    
    public ApisMessageVo parse() throws ParseException {
        this.segments = new LinkedList<>();
        this.message = new ApisMessageVo();

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
    
    private void processMessageAndGetSegments(String msg) throws ParseException {
        String txt = ParseUtils.stripApisHeaderAndFooter(msg);
        txt = txt.toUpperCase();
        txt = txt.replaceAll("\\n|\\r", "");
        
        SegmentFactory factory = new SegmentFactory(segmentPackageName);
        EdifactParser p = new EdifactParser();
        LinkedList<Segment> edifactSegments = p.parse(txt);
        for (Segment s: edifactSegments) {
            Segment paxlstSegment = factory.build(s);
            segments.add(paxlstSegment);
        }
    }
}
