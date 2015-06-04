package gov.cbp.taspd.gtas.parsers.unedifact;

import gov.cbp.taspd.gtas.parsers.paxlst.segments.ATT;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.BGM;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.COM;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.DTM;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.FTX;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.GEI;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.LOC;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.NAD;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.RFF;
import gov.cbp.taspd.gtas.parsers.paxlst.segments.TDT;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNA;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNB;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNG;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNH;

import java.util.Arrays;
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
        Composite[] parsed = parseSegmentSimple(txt);
        String segmentType = parsed[0].getValue();
        Composite[] composites = null;
        if (parsed.length > 1) {
            composites = Arrays.copyOfRange(parsed, 1, parsed.length);
        }
        
        switch (segmentType) {
        case "UNB":            
            return new UNB(composites);
        case "UNG":
            return new UNG(composites);
        case "UNH":
            return new UNH(composites);
        case "BGM":
            return new BGM(composites);
        case "RFF":
            return new RFF(composites);
        case "NAD":
            return new NAD(composites);
        case "COM":
            return new COM(composites);
        case "TDT":
            return new TDT(composites);
        case "LOC":
            return new LOC(composites);
        case "DTM":
            return new DTM(composites);
        case "ATT":
            return new ATT(composites);
        case "GEI":
            return new GEI(composites);
        case "FTX":
            return new FTX(composites);
            
        default:
            return new Segment(segmentType, new Composite[0]);
        }
    }

    private Composite[] parseSegmentSimple(String txt) {
        String regex = String.format("\\%c", serviceStrings.getDataElementSeparator());
        String[] tmp = txt.split(regex);

        Composite[] composites = null;
        if (tmp.length >= 1) {
            composites = new Composite[tmp.length];
            for (int i=0; i<tmp.length; i++) {
                composites[i] = new Composite(tmp[i], serviceStrings);
            }
        }
        return composites;        
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
                composites[i] = new Composite(tokens.get(i), serviceStrings);
            }
        }
        return composites;
    }
    
}
