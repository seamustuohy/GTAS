package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

/**
 */
public abstract class PaxlstParser {
    private String message;
    protected SegmentFactory segmentFactory;

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
    }

    protected abstract void parseSegments() throws ParseException;
    protected abstract void validateSegmentName(String segmentName) throws ParseException;
    
    public ApisMessageVo parse() throws ParseException {
        this.currentGroup = GROUP.NONE;    
        this.parsedMessage = new ApisMessageVo();
        this.segments = new LinkedList<>();
        this.segmentFactory = new SegmentFactory();
        
        EdifactParser p = new EdifactParser();
        segments = p.parse(message);
        parseSegments();
        
        return this.parsedMessage;
    }
    
    protected Segment getMandatorySegment(ListIterator<Segment> i, Class<?> clazz) throws ParseException {
        if (i.hasNext()) {
            Segment s = i.next();
            validateSegmentName(s.getName());
            return segmentFactory.build(s, clazz);
        }

        throw new ParseException("No segments left! ", -1);
    }
    
    protected Segment getConditionalSegment(ListIterator<Segment> i, Class<?> clazz, String segmentName) throws ParseException {
        if (i.hasNext()) {
            Segment s = i.next();
            validateSegmentName(s.getName());
            String myName = (segmentName != null) ? segmentName : clazz.getSimpleName();
            if (s.getName().equals(myName)) {
                return segmentFactory.build(s, clazz);
            } else {
                i.previous();
                return null;
            }
        }

        return null;
    }

    protected Segment getConditionalSegment(ListIterator<Segment> i, Class<?> clazz) throws ParseException {
        return getConditionalSegment(i, clazz, null);
    }
}
