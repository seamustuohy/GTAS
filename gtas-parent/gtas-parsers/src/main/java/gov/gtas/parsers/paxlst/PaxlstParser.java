package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

/**
 */
public abstract class PaxlstParser {
    private String message;
    protected SegmentFactory edifactFactory;

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

    public PaxlstParser(String message) {
        this.message = message;
        this.edifactFactory = new SegmentFactory(UNA.class.getPackage().getName());
    }

    protected abstract void parseSegments() throws ParseException;
    
    public ApisMessageVo parse() throws ParseException {
        this.currentGroup = GROUP.NONE;    
        this.parsedMessage = new ApisMessageVo();
        this.segments = new LinkedList<>();
        
        EdifactParser p = new EdifactParser();
        segments = p.parse(message);
        parseSegments();
        
        return this.parsedMessage;
    }
}
