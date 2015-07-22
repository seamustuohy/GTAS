package gov.gtas.parsers.paxlst;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

public class SegmentFactory {
    private static final Logger logger = LoggerFactory.getLogger(SegmentFactory.class);
    
    private String segmentPackageName;
    
    public SegmentFactory(String segmentPackageName) {
        this.segmentPackageName = segmentPackageName;
    }

    public Segment build(Segment s) throws ParseException {
        try {
            logger.debug(s.getName() + " " + Arrays.toString(s.getComposites()));
            Class<?> c = Class.forName(this.segmentPackageName + "." + s.getName());
            Object[] args = {s.getComposites()};
            return (Segment)c.getDeclaredConstructor(Composite[].class).newInstance(args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Throwable t = e.getCause();
            if (t != null) {
                throw new ParseException(e.getCause().getMessage(), -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
