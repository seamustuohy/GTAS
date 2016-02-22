package gov.gtas.parsers.edifact;

import java.util.LinkedList;
import java.util.List;

import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.EdifactUtils;
import gov.gtas.parsers.util.TextUtils;

/**
 * Utilities for tokenizing Edifact files
 * (https://en.wikipedia.org/wiki/EDIFACT).
 */
public final class EdifactLexer {
    private final UNA una;
    
    private final String message;
    
    public EdifactLexer(String message) { 
        this.message = message;
        this.una = EdifactUtils.getUnaSegment(message);
    }
    
    /**
     * Find the starting index of a segment. There are two possibilities: first,
     * the segment name followed by the data element separator, e.g., NAD+.
     * Second, the segment name followed by the segment terminator, .e.g, SRC'.
     * 
     * @return the starting index of the 'segmentName' in 'msg'. Return -1 if
     *         does not exist.
     */
    public int getStartOfSegment(String segmentName) {
        String format = "%s\\s*(\\%c|\\%c)";
        String regex = String.format(format, segmentName, this.una.getDataElementSeparator(),  this.una.getSegmentTerminator());
        return TextUtils.indexOfRegex(regex, this.message);
    }
    
    /**
     * Return everything from the start of the 'startSegment' to the
     * start of the 'endSegment' trailing header segment.
     */
    public String getMessagePayload(String startSegment, String endSegment) {
        int start = getStartOfSegment(startSegment);
        if (start == -1) {
            return null;
        }

        int end = getStartOfSegment(endSegment);
        if (end == -1) {
            return null;
        }
        
        return this.message.substring(start, end);
    }
    
    /**
     * Tokenize the input message into a list of {@code Segments} 
     */
    public List<Segment> tokenize() throws ParseException {
        String msg = TextUtils.convertToSingleLine(this.message).toUpperCase();
        SegmentTokenizer segmentTokenizer = new SegmentTokenizer(this.una);

        // start parsing with the UNB segment
        int unbIndex = getStartOfSegment("UNB");
        if (unbIndex == -1) {
            throw new ParseException("Required UNB segment not found");
        }
        
        List<String> stringSegments = TextUtils.splitWithEscapeChar(
                msg.substring(unbIndex), 
                this.una.getSegmentTerminator(), 
                this.una.getReleaseCharacter());

        List<Segment> segments = new LinkedList<>();
        for (String s : stringSegments) {
            segments.add(segmentTokenizer.buildSegment(s));
        }
        
        return segments;
    }

    public UNA getUna() {
        return una;
    }

    public String getMessage() {
        return message;
    }
}
