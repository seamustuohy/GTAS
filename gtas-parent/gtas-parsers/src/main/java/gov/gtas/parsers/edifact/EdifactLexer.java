package gov.gtas.parsers.edifact;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.ParseUtils;

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
        int unaIndex = ParseUtils.indexOfRegex(regex, msg);

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
        String regex = String.format("%s\\s*\\%c", segmentName, una.getDataElementSeparator());
        return ParseUtils.indexOfRegex(regex, msg);
    }
    
    /**
     * Return everything from the start of the 'startSegment' to the
     * start of the 'endSegment' trailing header segment.
     */
    public static String getMessagePayload(String msg, String startSegment, String endSegment) {
        UNA una = getUnaSegment(msg);
        int bgmIndex = getStartOfSegment(startSegment, msg, una);
        if (bgmIndex == -1) {
            return null;
        }

        int untIndex = getStartOfSegment(endSegment, msg, una);
        if (untIndex == -1) {
            return null;
        }
        
        return msg.substring(bgmIndex, untIndex);
    }
    
    public LinkedList<Segment> tokenize(String msg) throws ParseException {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        msg = preprocessMessage(msg);
        
        UNA una = getUnaSegment(msg);
        SegmentTokenizer segmentTokenizer = new SegmentTokenizer(una);

        // start parsing with the UNB segment
        int unbIndex = getStartOfSegment("UNB", msg, una);
        if (unbIndex == -1) {
            throw new ParseException("No UNB segment found");
        }
        msg = msg.substring(unbIndex);
        
        LinkedList<Segment> segments = new LinkedList<>();
        
        String[] stringSegments = ParseUtils.splitWithEscapeChar(msg, 
                una.getSegmentTerminator(), 
                una.getReleaseCharacter());

        for (String s : stringSegments) {
            Segment newSeg = segmentTokenizer.buildSegment(s);
            segments.add(newSeg);
        }
        
        return segments;
    }
    
    /**
     * Strip any extraneous header or trailer info, make sure the file is
     * upper case text only.
     * 
     * Messages must be transmitted as a continuous bit stream. "Lines" have no
     * meaning; there is no such thing as a "maximum" or "minimum" segment
     * length, other than that specified in the segment definitions.
     */
    private String preprocessMessage(String msg) {
        String txt = ParseUtils.stripStxEtxHeaderAndFooter(msg);
        return ParseUtils.convertToSingleLine(txt).toUpperCase();
    }
}
