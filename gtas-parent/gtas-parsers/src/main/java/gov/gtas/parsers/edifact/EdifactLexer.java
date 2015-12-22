package gov.gtas.parsers.edifact;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.TextUtils;

/**
 * The class takes as input any Edifact file
 * (https://en.wikipedia.org/wiki/EDIFACT) and breaks the file up into
 * 'Segments'. Segments have three-letter names, such as UNA, UNB, NAD, etc.
 * Each segment is further broken down into an array of composites, and each
 * composite has an array of elements.
 */
public class EdifactLexer {
    private static final String[] SEGMENT_NAMES = { "UNA", "UNB", "UNG", "UNH", "UNT", "UNE", "UNZ" };
    public static final Set<String> EDIFACT_SEGMENT_INDEX = new HashSet<>(Arrays.asList(SEGMENT_NAMES));
    
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
     * @return the starting index of the 'segmentName' in 'msg'.
     */
    public static int getStartOfSegment(String segmentName, String msg, UNA una) {
        String format = "%s\\s*\\%c";
        String regex = String.format(format, segmentName, una.getDataElementSeparator());
        int i = TextUtils.indexOfRegex(regex, msg);
        if (i != -1) {
            return i;
        }

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
    
    public static String prettyPrint(List<Segment> segments) {
        StringBuffer buff = new StringBuffer();
        for (Segment s : segments) {
            buff.append(s.getText()).append("\n");
        }
        return buff.toString();
    }
    
    public LinkedList<Segment> tokenize(String msg) throws ParseException {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        msg = TextUtils.convertToSingleLine(msg).toUpperCase();
        
        UNA una = getUnaSegment(msg);
        SegmentTokenizer segmentTokenizer = new SegmentTokenizer(una);

        // start parsing with the UNB segment
        int unbIndex = getStartOfSegment("UNB", msg, una);
        if (unbIndex == -1) {
            throw new ParseException("No UNB segment found");
        }
        msg = msg.substring(unbIndex);
        
        LinkedList<Segment> segments = new LinkedList<>();
        
        List<String> stringSegments = TextUtils.splitWithEscapeChar(
                msg, 
                una.getSegmentTerminator(), 
                una.getReleaseCharacter());

        for (String s : stringSegments) {
            Segment newSeg = segmentTokenizer.buildSegment(s);
            segments.add(newSeg);
        }
        
        return segments;
    }
}
