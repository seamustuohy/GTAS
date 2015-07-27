package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

public abstract class PaxlstParser {
    /** factory for creating segment classes */
    protected SegmentFactory segmentFactory;

    /** output from the edifact parser */ 
    protected List<Segment> segments;
    
    /** iterator for segment list */
    private ListIterator<Segment> iter;

    /** the final parsed message we ultimately return */
    protected ApisMessageVo parsedMessage;
    
    public PaxlstParser() { }

    public ApisMessageVo parse(String message) throws ParseException {
        this.parsedMessage = new ApisMessageVo();
        this.segments = new LinkedList<>();
        this.segmentFactory = new SegmentFactory();
        
        EdifactParser p = new EdifactParser();
        segments = p.parse(message);
        iter = segments.listIterator();

        parseSegments();
        
        return this.parsedMessage;
    }

    /**
     * Takes the list of segments, extracts data and fills the ApisMessageVo.
     * @throws ParseException
     */
    protected abstract void parseSegments() throws ParseException;
    
    /**
     * Throws an exception if the given segmentName is not valid.
     * @param segmentName
     * @throws ParseException
     */
    protected abstract void validateSegmentName(String segmentName) throws ParseException;
    
    protected Segment getMandatorySegment(Class<?> clazz) throws ParseException {
        if (iter.hasNext()) {
            Segment s = iter.next();
            validateSegmentName(s.getName());
            return segmentFactory.build(s, clazz);
        }

        throw new ParseException("No segments left! ", -1);
    }
    
    protected Segment getConditionalSegment(Class<?> clazz, String segmentName) throws ParseException {
        if (iter.hasNext()) {
            Segment s = iter.next();
            validateSegmentName(s.getName());
            String myName = (segmentName != null) ? segmentName : clazz.getSimpleName();
            if (s.getName().equals(myName)) {
                return segmentFactory.build(s, clazz);
            } else {
                iter.previous();
                return null;
            }
        }

        return null;
    }

    protected Segment getConditionalSegment(Class<?> clazz) throws ParseException {
        return getConditionalSegment(clazz, null);
    }
}
