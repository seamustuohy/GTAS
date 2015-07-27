package gov.gtas.parsers.edifact;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentFactory {
    private static final Logger logger = LoggerFactory.getLogger(SegmentFactory.class);
    
    public <T extends Segment> T build(Segment s, Class<?> clazz) throws ParseException {
        try {
            logger.debug(s.getName() + " " + Arrays.toString(s.getComposites()));
            Object[] args = {s.getComposites()};
            return (T)clazz.getDeclaredConstructor(Composite[].class).newInstance(args);
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
