package gov.gtas.parsers.edifact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.TextUtils;

/**
 * Parses a segment text into composites and elements.
 */
public final class SegmentTokenizer {
    private final UNA una;
    
    public SegmentTokenizer(UNA una) {
        this.una = una;
    }
    
    public Segment buildSegment(final String segmentText) throws ParseException {
        if (StringUtils.isBlank(segmentText)) {
            return null;
        }
        
        String[] tokens = TextUtils.splitWithEscapeChar(
                segmentText, 
                una.getDataElementSeparator(), 
                una.getReleaseCharacter()); 
        
        if (ArrayUtils.isEmpty(tokens)) { 
            throw new ParseException("Error tokenizing segment text " + segmentText);
        }

        String segmentName = tokens[0];
        if (StringUtils.isBlank(segmentName)) {
            throw new ParseException("Illegal segment name " + segmentName);                
        }

        List<Composite> composites = new ArrayList<>(tokens.length - 1);
        for (int i=1; i<tokens.length; i++) {
            String cText = tokens[i];
            String[] elements = TextUtils.splitWithEscapeChar(
                    cText, 
                    una.getComponentDataElementSeparator(),
                    una.getReleaseCharacter());
            composites.add(new Composite(Arrays.asList(elements)));
        }

        return new Segment(segmentName, segmentText, composites);
    }
}
