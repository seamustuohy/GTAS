package gov.cbp.taspd.gtas.parsers.edifact;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EdifactParser {
    private UNA serviceStrings;
    
    public LinkedList<Segment> parse(String txt) {
        int unaIndex = txt.indexOf("UNA");
        if (unaIndex != -1) {
            int endIndex = unaIndex + "UNA".length() + 6;
            String delims = txt.substring(unaIndex, endIndex);
            serviceStrings = new UNA(delims);
        } else {
            serviceStrings = new UNA();
        }

        int unbIndex = txt.indexOf("UNB");
        if (unbIndex == -1) {
            System.err.println("no UNB segment");
            System.exit(0);
        }
        txt = txt.substring(unbIndex);
        
        txt = txt.replaceAll("\\n|\\r|\\t", "");

        LinkedList<Segment> segments = new LinkedList<>();
        String segmentRegex = String.format("\\%c", serviceStrings.getSegmentTerminator());
        String[] stringSegments = txt.split(segmentRegex);
        for (String s : stringSegments) {
            System.out.println(s);
            Composite[] parsed = parseSegmentSimple(s);
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
            segments.add(new Segment(segmentType, composites));
        }
        
        return segments;
    }

    /**
     * TODO: this doesn't handle escape chars
     */
    private Composite[] parseSegmentSimple(String segmentText) {
        Composite[] rv = null;
        
        String regex = String.format("\\%c", serviceStrings.getDataElementSeparator());
        String[] stringComposites = segmentText.split(regex);
        int numComposites = stringComposites.length;
        if (numComposites == 0) {
            System.err.println("segment has no composites: " + segmentText);
            return null;
        }
        
        rv = new Composite[numComposites];
        for (int i=0; i<numComposites; i++) {
            String[] elementsText = stringComposites[i].split("" + serviceStrings.getComponentDataElementSeparator());
            int numElements = elementsText.length;
            if (numElements == 1) {
                rv[i] = new Composite(elementsText[0]);
            } else if (numElements > 1) {
                Element[] elements = new Element[numElements];
                for (int j = 0; j < numElements; j++) {
                    elements[j] = new Element(elementsText[j]);
                }
                rv[i] = new Composite(elements);
            } else {
                System.err.println("unable to parse segment: " + segmentText);
            }
        }

        return rv;        
    }
    
    private Composite[] parseSegment(String txt) {
        String regex = String.format(
                "[^\\%c]*(\\%c\\%c)+[^\\%c]*|[^\\%c]+",
                serviceStrings.getDataElementSeparator(),
                serviceStrings.getReleaseCharacter(),
                serviceStrings.getDataElementSeparator(),
                serviceStrings.getDataElementSeparator(),
                serviceStrings.getDataElementSeparator());
        Pattern tokenPattern = Pattern.compile(regex);
        Matcher matcher = tokenPattern.matcher(txt);
        List<String> tokens = new LinkedList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        int numTokens = tokens.size();
        if (numTokens == 0) {
            // error?
            return null;
        }
        
        tokens.remove(0);
        
        Composite[] composites = null;
        if (numTokens >= 1) {
            composites = new Composite[tokens.size()];
            for (int i=0; i<tokens.size(); i++) {
//                composites[i] = new Composite(tokens.get(i), serviceStrings);
            }
        }
        return composites;
    }    
}
