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
    
    private String edifactSegmentPackageName;
    private String segmentPackageName;
    
    public SegmentFactory(String edifactSegmentPackageName, String segmentPackageName) {
        this.edifactSegmentPackageName = edifactSegmentPackageName;
        this.segmentPackageName = segmentPackageName;
    }

    public Segment build(Segment s) throws ParseException {
        String segmentName = s.getName();
        String pkg = null;
        switch(segmentName) {
        case "UNA":
        case "UNB":
        case "UNG":
        case "UNH":
        case "UNT":
        case "UNE":
        case "UNZ":
            pkg = this.edifactSegmentPackageName;
            break;
        default:
            pkg = this.segmentPackageName;
        }

        try {
            logger.debug(s.getName() + " " + Arrays.toString(s.getComposites()));
            Class<?> c = Class.forName(pkg + "." + segmentName);
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

//        logger.error("Could not create " + pkg + "." + segmentName);
//        return s;
    }
}
