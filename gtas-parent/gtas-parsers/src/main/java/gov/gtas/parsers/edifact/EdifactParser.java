package gov.gtas.parsers.edifact;

import java.util.Arrays;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdifactParser {
    private static final Logger logger = LoggerFactory.getLogger(EdifactParser.class);

    private SegmentParser segmentParser;
    
    private UNA una;
    
   public LinkedList<Segment> parse(String txt) {
        int unaIndex = txt.indexOf("UNA");
        if (unaIndex != -1) {
            int endIndex = unaIndex + "UNA".length() + 6;
            String delims = txt.substring(unaIndex, endIndex);
            una = new UNA(delims);
        } else {
            una = new UNA();
        }
        
        this.segmentParser = new SegmentParser(una);

        int unbIndex = txt.indexOf("UNB");
        if (unbIndex == -1) {
            logger.error("no UNB segment");
            // TODO: throw exception
        }
        txt = txt.substring(unbIndex);
        
        LinkedList<Segment> segments = new LinkedList<>();
        String segmentRegex = String.format("\\%c", una.getSegmentTerminator());
        String[] stringSegments = txt.split(segmentRegex);
        for (String s : stringSegments) {
            s = s.trim();
            Composite[] parsed = this.segmentParser.parseSegment(s);
            if (parsed.length == 0) { 
                continue;
            }
            
            String segmentType = parsed[0].getValue().trim();
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
