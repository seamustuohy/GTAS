package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

public class SegmentFactory {
    private String segmentPackageName;
    
    public SegmentFactory(String segmentPackageName) { 
        this.segmentPackageName = segmentPackageName;
    }
    
    public Segment build(Segment s) {
        String segmentName = s.getName();
        try {
            Class<?> c = Class.forName(this.segmentPackageName + "." + segmentName);
            Object[] args = {s.getComposites()};
            return (Segment)c.getDeclaredConstructor(Composite[].class).newInstance(args);
        } catch (Exception e) {
            return s;
        }
    }
}
