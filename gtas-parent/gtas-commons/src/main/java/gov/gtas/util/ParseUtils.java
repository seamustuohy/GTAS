package gov.gtas.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseUtils {
    public static String stripHeaderAndFooter(String text) {
        String rv = text;
        final int STX_CODEPOINT = 2;
        final int ETX_CODEPOINT = 3;
        
        int stxIndex = rv.indexOf(STX_CODEPOINT);
        if (stxIndex != -1) {
            rv = rv.substring(stxIndex + 1);
        }
        int etxIndex = rv.indexOf(ETX_CODEPOINT);
        if (etxIndex != -1) {
            rv = rv.substring(0, etxIndex);
        }       
        
        return rv;
    }
    
    public static Date parseDateTime(String dt, String format) {
        DateFormat timeFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return timeFormat.parse(dt);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
        
    }
}
