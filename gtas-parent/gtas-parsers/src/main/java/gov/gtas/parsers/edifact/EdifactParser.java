package gov.gtas.parsers.edifact;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;

import gov.gtas.parsers.edifact.segment.UNB;
import gov.gtas.parsers.edifact.segment.UNE;
import gov.gtas.parsers.edifact.segment.UNG;
import gov.gtas.parsers.edifact.segment.UNH;
import gov.gtas.parsers.edifact.segment.UNT;
import gov.gtas.parsers.edifact.segment.UNZ;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.ParseUtils;

/**
 * The parser takes the output from the Edifact lexer and starts the process of
 * parsing the individual segments and extracting data. This class implements
 * the template pattern so subclasses can implement specific rules for parsing a
 * particular message payload. The generic Edifact segments -- UNB, UNH, etc. --
 * are parsed in this class.
 * 
 * @param <T>
 *            the specific message class that will be returned after parsing.
 */
public abstract class EdifactParser <T extends MessageVo> {
    /** factory for creating segment classes */
    protected SegmentFactory segmentFactory;

    /** output from the edifact lexer. The first segment will always be UNB */
    protected List<Segment> segments;
    
    /** iterator for segment list */
    private ListIterator<Segment> iter;

    /** the final parsed message we ultimately return */
    protected T parsedMessage;

    private EdifactLexer lexer = new EdifactLexer();

    public EdifactParser() { }

    /**
     * As per ISO 9735, the service segments are sequenced in a message in the
     * following order:
     * <ol>
     * <li>UNA Service String Advice
     * <li>UNB Interchange Header Segment
     * <li>UNG Functional Group Header
     * <li>UNH Message Header
     * <li>(BODY of MESSAGE)
     * <li>UNT Message Trailer
     * <li>UNE Functional Group Trailer
     * <li>UNZ Interchange Trailer
     * </ol>
     * 
     * @param message
     * @return
     * @throws ParseException
     */
    public T parse(String message) throws ParseException {
        this.segmentFactory = new SegmentFactory();
        this.segments = lexer.tokenize(message);
        this.iter = segments.listIterator();

        String payload = getPayloadText(message);
        if (payload == null) {
            throw new ParseException("Could not extract message payload");
        }
        String md5 = ParseUtils.getMd5Hash(payload, StandardCharsets.US_ASCII);
        this.parsedMessage.setHashCode(md5);
        
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
        String ver = String.format("%s.%s", unh.getMessageTypeVersion(), unh.getMessageTypeReleaseNumber());
        parsedMessage.setVersion(ver);
    }

    private void parseTrailer() throws ParseException {
        getMandatorySegment(UNT.class);
        getConditionalSegment(UNE.class);
        getMandatorySegment(UNZ.class);
    }
    
    /**
     * Subclasses implement this method to parse the message payload/body
     * that's specific to the message type.
     * @throws ParseException
     */
    protected abstract void parsePayload() throws ParseException;
    
    protected abstract String getPayloadText(String message) throws ParseException;
    
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
            S rv = segmentFactory.build(s, clazz);
//            System.out.println(rv);
            return rv;
        }

        throw new ParseException("No segments left! ");
    }
    
    protected <S extends Segment> S getConditionalSegment(Class<?> clazz, String segmentName) throws ParseException {
        if (iter.hasNext()) {
            Segment s = iter.next();
            validateSegmentName(s.getName());
            String myName = (segmentName != null) ? segmentName : clazz.getSimpleName();
            if (s.getName().equals(myName)) {
                S rv = segmentFactory.build(s, clazz);
//                System.out.println(rv);
                return rv;
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
