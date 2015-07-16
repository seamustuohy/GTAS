package gov.gtas.parsers.paxlst;

import java.util.Arrays;

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
            System.out.println(segmentName + " " + Arrays.toString(s.getComposites()));

            Class<?> c = Class.forName(this.segmentPackageName + "." + segmentName);
            Object[] args = {s.getComposites()};
            return (Segment)c.getDeclaredConstructor(Composite[].class).newInstance(args);
        } catch (Exception e) {
            System.err.println("Could not create " + this.segmentPackageName + "." + segmentName);
            return s;
        }
    }
}
