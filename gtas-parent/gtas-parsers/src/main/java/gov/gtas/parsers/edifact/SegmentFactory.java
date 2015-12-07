package gov.gtas.parsers.edifact;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.ErrorUtils;

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
                throw new ParseException(ErrorUtils.getStacktrace(t));
            }
            throw new ParseException(ErrorUtils.getStacktrace(e));
            
        } catch (Exception e) {
            throw new ParseException(ErrorUtils.getStacktrace(e));
        }
    }
}
