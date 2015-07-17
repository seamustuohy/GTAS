package gov.gtas.parsers.edifact;

import gov.gtas.parsers.util.ParseUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

/**
 * The class takes as input any Edifact file
 * (https://en.wikipedia.org/wiki/EDIFACT) and parses the file into a series of
 * 'Segments'. Segments have three-letter names, such as UNA, UNB, NAD, etc.
 * Each segment is further broken down into an array of composites, and each
 * composite has an array of elements.
 * 
 * This parser is very simple -- performing only basic string manipulations and
 * splits based on the delimiters contained in the UNA segment. It does not
 * check for edifact message structure; e.g., for example it will not check if a
 * UNH segment has a corresponding UNT segment at the end. The only purpose is
 * to parse the input text into segments and return them.
 */
public class EdifactParser {
    
    public LinkedList<Segment> parse(String txt) throws ParseException {
        if (StringUtils.isEmpty(txt)) return null;
        txt = preprocessMessage(txt);
        
        UNA una = null;
        int unaIndex = txt.indexOf("UNA");
        if (unaIndex != -1) {
            int endIndex = unaIndex + "UNA".length() + UNA.NUM_UNA_CHARS;
            String delims = txt.substring(unaIndex, endIndex);
            una = new UNA(delims);
        } else {
            una = new UNA();
        }
        
        SegmentParser segmentParser = new SegmentParser(una);

        // start with the UNB segment
        int unbIndex = txt.indexOf("UNB");
        if (unbIndex == -1) {
            throw new ParseException("No UNB segment found", -1);
        }
        txt = txt.substring(unbIndex);
        
        LinkedList<Segment> segments = new LinkedList<>();
        
        String[] stringSegments = ParseUtils.splitWithEscapeChar(txt, 
                una.getSegmentTerminator(), 
                una.getReleaseCharacter());

        for (String s : stringSegments) {
            Composite[] parsed = segmentParser.parseSegment(s);
            if (parsed == null) { 
                throw new ParseException("Could not parse segment " + s, -1);
            }
            
            String segmentType = parsed[0].getValue();
            Composite[] composites = null;
            if (parsed.length > 1) {
                composites = Arrays.copyOfRange(parsed, 1, parsed.length);
            }
            Segment newSegment = new Segment(segmentType, composites);
            segments.add(newSegment);
        }
        
        return segments;
    }
    
    /**
     * Messages must be transmitted as a continuous bit stream. "Lines" have no
     * meaning; there is no such thing as a "maximum" or "minimum" segment
     * length, other than that specified in the segment definitions.
     */
    private String preprocessMessage(String txt) {
        return ParseUtils.convertToSingleLine(txt).toUpperCase();
    }
}
