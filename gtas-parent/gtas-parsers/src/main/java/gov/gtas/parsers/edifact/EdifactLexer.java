package gov.gtas.parsers.edifact;

import java.util.LinkedList;
import java.util.List;

import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.TextUtils;

/**
 * Utilities for tokenizing Edifact files
 * (https://en.wikipedia.org/wiki/EDIFACT).
 */
public final class EdifactLexer {
    private EdifactLexer() { }
    
    /**
     * Return the UNA segment for the given edifact message.  If we can't
     * find one, return the default UNA segment.
     * @param msg
     * @return
     */
    public static UNA getUnaSegment(String msg) {
        String regex = String.format("UNA.{%d}\\s*UNB", UNA.NUM_UNA_CHARS);
        int unaIndex = TextUtils.indexOfRegex(regex, msg);

        if (unaIndex != -1) {
            int endIndex = unaIndex + "UNA".length() + UNA.NUM_UNA_CHARS;
            String delims = msg.substring(unaIndex, endIndex);
            return new UNA(delims);
        }   
        
        return new UNA();
    }
    
    /**
     * @return the starting index of the 'segmentName' in 'msg'.  Return
     * -1 if does not exist.
     */
    public static int getStartOfSegment(String segmentName, String msg, UNA una) {
        String format = "%s\\s*\\%c";
        String regex = String.format(format, segmentName, una.getDataElementSeparator());
        int i = TextUtils.indexOfRegex(regex, msg);
        if (i != -1) {
            return i;
        }

        // handle case with segment name by itself
        regex = String.format(format, segmentName, una.getSegmentTerminator());
        return TextUtils.indexOfRegex(regex, msg);
    }
    
    /**
     * Return everything from the start of the 'startSegment' to the
     * start of the 'endSegment' trailing header segment.
     */
    public static String getMessagePayload(String msg, String startSegment, String endSegment) {
        UNA una = getUnaSegment(msg);
        int start = getStartOfSegment(startSegment, msg, una);
        if (start == -1) {
            return null;
        }

        int end = getStartOfSegment(endSegment, msg, una);
        if (end == -1) {
            return null;
        }
        
        return msg.substring(start, end);
    }
    
    /**
     * Create a formatted string of segments, putting a carriage
     * return at the end of each segment.
     * @param segments
     * @return
     */
    public static String prettyPrint(List<Segment> segments) {
        StringBuffer buff = new StringBuffer();
        for (Segment s : segments) {
            buff.append(s.getText()).append("\n");
        }
        return buff.toString();
    }
    
    /**
     * Create a list of {@code Segments} from an input Edifact file.
     * @param msg
     * @return
     * @throws ParseException
     */
    public static LinkedList<Segment> tokenize(String msg) throws ParseException {
        msg = TextUtils.convertToSingleLine(msg).toUpperCase();
        UNA una = getUnaSegment(msg);
        SegmentTokenizer segmentTokenizer = new SegmentTokenizer(una);

        // start parsing with the UNB segment
        int unbIndex = getStartOfSegment("UNB", msg, una);
        if (unbIndex == -1) {
            throw new ParseException("Required UNB segment not found");
        }
        
        List<String> stringSegments = TextUtils.splitWithEscapeChar(
                msg.substring(unbIndex), 
                una.getSegmentTerminator(), 
                una.getReleaseCharacter());

        LinkedList<Segment> segments = new LinkedList<>();
        for (String s : stringSegments) {
            Segment newSeg = segmentTokenizer.buildSegment(s);
            segments.add(newSeg);
        }
        
        return segments;
    }
}
