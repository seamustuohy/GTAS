package gov.gtas.parsers.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseUtils {
    /**
     * Some telecommunications transmission protocols require various
     * communication type headers and trailers to facilitate addressing,
     * routing, security, and other purposes.
     * 
     * These headers and trailers are typically delimited by special control
     * characters STX and ETX. This method removes the header and trailer from
     * the message. See https://en.wikipedia.org/wiki/Control_characters
     * 
     * @param text
     * @return message text without header or footer
     */
    public static String stripStxEtxHeaderAndFooter(String text) {
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
            rv[i] = tmpSplit[i].replaceAll(sentinel, "\\" + delimiter).trim();
        }
        
        return rv;
    }
    
    /**
     * @param str input string
     * @return a concatenation of all the lines in 'str'.  Trim each line of
     * leading and trailing whitespace.  Empty lines get clobbered.
     */
    public static String convertToSingleLine(String str) {
        if (str == null) return null;
        String[] lines = str.split("[\r\n]+");
        StringBuilder sb = new StringBuilder();
        for (String s : lines) {
            sb.append(s.trim());
        }
        return sb.toString();
    }
    
    /**
     * @param txt input string
     * @param encoding character encoding of the input string
     * @return an md5 hash of the input string
     */
    public static String getMd5Hash(String txt, Charset encoding) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(txt.getBytes(encoding));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
