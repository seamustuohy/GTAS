package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.util.ParseUtils;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * A transmitted message to DHS may include only one instance of a PAXLST
 * message. Batching of multiple PAXLST messages into a single envelope (UNB-
 * UNZ) or batching of multiple envelopes containing PAXLST messages into a
 * single message transmission will result in a rejection of the message(s).
 */
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
        this.currentGroup = GROUP.NONE;    
        this.parsedMessage = new ApisMessageVo();
        this.segments = new LinkedList<>();
        processMessageAndGetSegments();
        parseSegments();
        return this.parsedMessage;
    }
    
    private void processMessageAndGetSegments() throws ParseException {
        String txt = ParseUtils.stripStxEtxHeaderAndFooter(message);               
        SegmentFactory factory = new SegmentFactory(segmentPackageName);
        EdifactParser p = new EdifactParser();
        LinkedList<Segment> edifactSegments = p.parse(txt);
        for (Segment s: edifactSegments) {
            Segment paxlstSegment = factory.build(s);
            segments.add(paxlstSegment);
        }
    }
}
