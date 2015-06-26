package gov.gtas.parsers.edifact;

import gov.gtas.parsers.util.ParseUtils;

public class SegmentParser {
    private UNA una;
    
    @SuppressWarnings("unused")
    private SegmentParser() { }
    
    public SegmentParser(UNA una) {
        this.una = una;
    }
    
    public Composite[] parseSegment(String segmentText) {
        if (segmentText == null) return null;
        
        String[] stringComposites = ParseUtils.splitWithEscapeChar(
                segmentText, 
                una.getDataElementSeparator(), 
                una.getReleaseCharacter()); 

        int numComposites = stringComposites.length;
        Composite[] rv = new Composite[numComposites];
        for (int i=0; i<numComposites; i++) {
            String[] elementsText = ParseUtils.splitWithEscapeChar(
                    stringComposites[i], 
                    una.getComponentDataElementSeparator(),
                    una.getReleaseCharacter());
            int numElements = elementsText.length;
            if (numElements == 1) {
                // hold single value in segment value field
                rv[i] = new Composite(elementsText[0].trim());
            } else { 
                // create array of elements
                Element[] elements = new Element[numElements];
                for (int j = 0; j < numElements; j++) {
                    elements[j] = new Element(elementsText[j].trim());
                }
                rv[i] = new Composite(elements);
            }
        }

        return rv;        
    }
}
