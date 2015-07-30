package gov.gtas.parsers.edifact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.ParseUtils;

/**
 * Parses a segment text into composites and elements.
 */
public class SegmentTokenizer {
    public static final int MAX_SEGMENT_NAME_LENG = 3;

    private UNA una;
    
    @SuppressWarnings("unused")
    private SegmentTokenizer() { }
    
    public SegmentTokenizer(UNA una) {
        this.una = una;
    }
    
    public Segment buildSegment(String segmentText) throws ParseException {
        if (StringUtils.isBlank(segmentText)) {
            return null;
        }
        
        String[] tokens = ParseUtils.splitWithEscapeChar(
                segmentText, 
                una.getDataElementSeparator(), 
                una.getReleaseCharacter()); 
        
        if (ArrayUtils.isEmpty(tokens)) { 
            throw new ParseException("Error tokenizing segment text " + segmentText);
        }

        String segmentName = tokens[0];
        if (StringUtils.isBlank(segmentName) || segmentName.length() > MAX_SEGMENT_NAME_LENG) {
            throw new ParseException("Illegal segment name " + segmentName);                
        }

        List<Composite> composites = new ArrayList<>(tokens.length - 1);
        for (int i=1; i<tokens.length; i++) {
            String cText = tokens[i];
            String[] elements = ParseUtils.splitWithEscapeChar(
                    cText, 
                    una.getComponentDataElementSeparator(),
                    una.getReleaseCharacter());
            composites.add(new Composite(Arrays.asList(elements)));
        }

        return new Segment(segmentName, composites);        
    }
}
