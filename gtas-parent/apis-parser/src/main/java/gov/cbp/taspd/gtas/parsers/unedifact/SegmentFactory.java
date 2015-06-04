package gov.cbp.taspd.gtas.parsers.unedifact;

import gov.cbp.taspd.gtas.parsers.paxlst.segments.NAD;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNA;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNB;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNG;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SegmentFactory {
    private UNA serviceStrings;
    public SegmentFactory(UNA serviceStrings) {
        this.serviceStrings = serviceStrings;
    }
    
    public Segment build(String txt) {
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
        
        String segmentType = tokens.get(0);
        tokens.remove(0);
        
        Composite[] composites = null;
        if (numTokens >= 1) {
            composites = new Composite[tokens.size()];
            for (int i=0; i<tokens.size(); i++) {
                composites[i] = new Composite(tokens.get(i), serviceStrings);
            }
        }
        
        Segment rv = null;
        switch (segmentType) {
        case "UNB":            
            return new UNB(composites);
        case "UNG":
            return new UNG(composites);
        case "NAD":
            return new NAD(composites);
        default:
            rv = new Segment(segmentType, new Composite[0]);
        }
        
        return rv;
    }

}
