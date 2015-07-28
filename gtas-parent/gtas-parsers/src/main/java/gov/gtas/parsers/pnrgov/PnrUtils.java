package gov.gtas.parsers.pnrgov;

import java.util.Date;

import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.ParseUtils;

public class PnrUtils {
    public static Date parseDateTime(String dt) throws ParseException {
        final String DATE_ONLY_FORMAT = "ddMMyy";
        final String DATE_TIME_FORMAT = "ddMMyyhhmm";

        if (dt.length() == DATE_ONLY_FORMAT.length()) {
            return ParseUtils.parseDateTime(dt, DATE_ONLY_FORMAT);
        } else if (dt.length() == DATE_TIME_FORMAT.length()) {
            return ParseUtils.parseDateTime(dt, DATE_TIME_FORMAT);
        }

        return null;
    }
}
