package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.util.ParseUtils;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public abstract class PaxlstParser {
    private String message;
    private String segmentPackageName;

    protected enum GROUP {
        NONE,
        HEADER,
        REPORTING_PARTY,
        FLIGHT,
        PAX
    }
    
    protected GROUP currentGroup;

    protected ApisMessageVo parsedMessage;
    protected List<Segment> segments;
    
    public PaxlstParser(String message, String segmentPackageName) {
        this.message = message;
        this.segmentPackageName = segmentPackageName;
    }

    protected abstract void parseSegments();
    
    public ApisMessageVo parse() throws ParseException {
        this.segments = new LinkedList<>();
        this.parsedMessage = new ApisMessageVo();
        this.currentGroup = GROUP.NONE;    
        processMessageAndGetSegments();
        parseSegments();
        return this.parsedMessage;
    }
    
    private void processMessageAndGetSegments() throws ParseException {
        String txt = ParseUtils.stripApisHeaderAndFooter(message);
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
