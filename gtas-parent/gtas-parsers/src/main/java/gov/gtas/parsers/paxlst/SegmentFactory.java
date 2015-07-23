package gov.gtas.parsers.paxlst;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
        return build(s, null);
    }
    
    public Segment build(Segment s, Class<?> clazz) throws ParseException {
        try {
            logger.debug(s.getName() + " " + Arrays.toString(s.getComposites()));
            
            Class<?> c = null;
            if (clazz != null) {
                c = clazz;
            } else {
                c = Class.forName(this.segmentPackageName + "." + s.getName());
            }
            
            Object[] args = {s.getComposites()};
            return (Segment)c.getDeclaredConstructor(Composite[].class).newInstance(args);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t != null) {
                throw new ParseException(ExceptionUtils.getStackTrace(t), -1);
            }
            throw new ParseException(ExceptionUtils.getStackTrace(e), -1);
            
        } catch (Exception e) {
            throw new ParseException(ExceptionUtils.getStackTrace(e), -1);
        }
    }
}
