package gov.gtas.parsers.edifact;

import gov.gtas.parsers.util.ParseUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

public class EdifactParser {
    
    public LinkedList<Segment> parse(String txt) throws ParseException {
        if (StringUtils.isEmpty(txt)) return null;
        txt = ParseUtils.convertToSingleLine(txt);
        txt = txt.toUpperCase();
        
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
            if (parsed.length == 0) { 
                continue;
            }
            
            String segmentType = parsed[0].getValue();
            if (segmentType == null || segmentType.equals("")) {
                continue;
            }
            
            Composite[] composites = null;
            if (parsed.length > 1) {
                composites = Arrays.copyOfRange(parsed, 1, parsed.length);
            }
            Segment newSegment = new Segment(segmentType, composites);
            segments.add(newSegment);
        }
        
        return segments;
    }
}
