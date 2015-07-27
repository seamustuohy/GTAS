package gov.gtas.parsers.edifact;

import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;

import gov.gtas.parsers.edifact.segment.UNB;
import gov.gtas.parsers.edifact.segment.UNG;
import gov.gtas.parsers.edifact.segment.UNH;

public abstract class EdifactParser <T extends MessageVo> {
    /** factory for creating segment classes */
    protected SegmentFactory segmentFactory;

    /** output from the edifact parser */ 
    protected List<Segment> segments;
    
    /** iterator for segment list */
    private ListIterator<Segment> iter;

    /** the final parsed message we ultimately return */
    protected T parsedMessage;

    private EdifactLexer edifactParser = new EdifactLexer();

    public EdifactParser() { }

    public T parse(String message) throws ParseException {
        this.segmentFactory = new SegmentFactory();
        this.segments = edifactParser.parse(message);
        iter = segments.listIterator();

        parseHeader();
        parsePayload();
        parseTrailer();
        
        return this.parsedMessage;
    }

    private void parseHeader() throws ParseException {
        UNB unb = getMandatorySegment(UNB.class);
        parsedMessage.setTransmissionSource(unb.getSenderIdentification());
        parsedMessage.setTransmissionDate(unb.getDateAndTimeOfPreparation());

        getConditionalSegment(UNG.class);

        UNH unh = getMandatorySegment(UNH.class);
        parsedMessage.setMessageType(unh.getMessageType());
        parsedMessage.setVersion(unh.getMessageTypeVersion());
    }

    private void parseTrailer() throws ParseException {
        // TBD
    }
    
    /**
     * Subclasses implement this method to parse the message payload/body
     * that's specific to the message type.
     * @throws ParseException
     */
    protected abstract void parsePayload() throws ParseException;
    
    /**
     * Throws an exception if the given segmentName is not valid.
     * @param segmentName
     * @throws ParseException
     */
    protected abstract void validateSegmentName(String segmentName) throws ParseException;
    
    protected <S extends Segment> S getMandatorySegment(Class<?> clazz) throws ParseException {
        if (iter.hasNext()) {
            Segment s = iter.next();
            validateSegmentName(s.getName());
            return segmentFactory.build(s, clazz);
        }

        throw new ParseException("No segments left! ", -1);
    }
    
    protected <S extends Segment> S getConditionalSegment(Class<?> clazz, String segmentName) throws ParseException {
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

    protected <S extends Segment> S getConditionalSegment(Class<?> clazz) throws ParseException {
        return getConditionalSegment(clazz, null);
    }
}
