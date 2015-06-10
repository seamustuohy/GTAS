package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class SegmentFactory {
    private String segmentPackageName;
    
    public SegmentFactory(String segmentPackageName) { 
        this.segmentPackageName = segmentPackageName;
    }
    
    public Segment build(Segment s) {
        String segmentName = s.getName().trim();
        switch (segmentName) {
        case "CNT":
        case "UNT":
        case "UNE":
        case "UNZ":
        case "":
            return s;
            
        default:
            try {
                Class<?> c = Class.forName(this.segmentPackageName + "." + segmentName);
                Object[] args = {s.getComposites()};
                return (Segment)c.getDeclaredConstructor(Composite[].class).newInstance(args);
            } catch (Exception e) {
                System.err.println("unrecognized segment: " + segmentName);
                e.printStackTrace();
                return null;
            }
        }
    }
}
