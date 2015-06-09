package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.parsers.edifact.segments.UNB;
import gov.cbp.taspd.gtas.parsers.edifact.segments.UNG;
import gov.cbp.taspd.gtas.parsers.edifact.segments.UNH;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.ATT;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.BGM;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.COM;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.DOC;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.DTM;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.FTX;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.GEI;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.LOC;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.NAD;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.NAT;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.RFF;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.TDT;

public class SegmentFactory {
    public SegmentFactory() { }
    
    public Segment build(Segment s) {
        Composite[] composites = s.getComposites();
        
        switch (s.getName()) {
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
        case "NAT":
            return new NAT(composites);
        case "DOC":
            return new DOC(composites);

        case "CNT":
        case "UNT":
        case "UNE":
        case "UNZ":
            return s;
            
        default:
            System.err.println("unrecognized segment: " + s.getName());
            return s;
        }
    }

    
}
