package gov.gtas.parsers.edifact;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.exception.ParseException;

public class SegmentFactory {
    private static final Logger logger = LoggerFactory.getLogger(SegmentFactory.class);
    
    public <T extends Segment> T build(Segment s, Class<?> clazz) throws ParseException {
        try {
            logger.debug(s.getName() + " " + s.getComposites());
            Object[] args = {s.getComposites()};
            return (T)clazz.getDeclaredConstructor(List.class).newInstance(args);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t != null) {
                throw new ParseException(ExceptionUtils.getStackTrace(t));
            }
            throw new ParseException(ExceptionUtils.getStackTrace(e));
            
        } catch (Exception e) {
            throw new ParseException(ExceptionUtils.getStackTrace(e));
        }
    }
}
