package gov.gtas.parsers.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseUtils {
    /**
     * Strip the header of APIS messages
     * @param text
     * @return
     */
    public static String stripApisHeaderAndFooter(String text) {
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
        }
        return null;
    }
    
    /**
     * Split a string 's' using 'delimiter' but don't split on any delimiters
     * escaped with 'escape' character.  For example, if we call this method
     * with s = "mc?'foo'bar", delimiter = '\'', escape = '?'  the method
     * should return ["mc'foo", "bar"].  Note as a side-effect, the escape
     * characters are removed from the final output.
     */
    public static String[] splitWithEscapeChar(String s, char delimiter, char escape) {
        if (s == null) return null;
        
        String escapedDelimiter = String.format("\\%c\\%c", escape, delimiter);
        final String sentinel = "~XYZ~";
        String tmp = s.replaceAll(escapedDelimiter, sentinel);
        
        String regex = String.format("\\%c", delimiter);
        String[] tmpSplit = tmp.split(regex);
        String[] rv = new String[tmpSplit.length];
        for (int i=0; i<tmpSplit.length; i++) {
            rv[i] = tmpSplit[i].replaceAll(sentinel, delimiter + "");
        }
        
        return rv;
    }
}
