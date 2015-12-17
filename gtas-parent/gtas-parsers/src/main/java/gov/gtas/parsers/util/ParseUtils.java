package gov.gtas.parsers.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ParseUtils {
    private static final Logger logger = LoggerFactory.getLogger(ParseUtils.class);

    private ParseUtils() { }
    
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
        try {
            DateFormat timeFormat = new SimpleDateFormat(format, Locale.ENGLISH);
            return timeFormat.parse(dt);
        } catch (java.text.ParseException pe) {
            logger.warn(String.format("Could not parse date %s using format %s", dt, format));
        }
        
        return null;
    }
    
    /**
     * assumptions:
     *   - carrier must be at least 2 chars
     *   - carrier can end in number
     *   - flight number can be between 1 and 4 numbers
     * TODO: flight numbers ending in letters?  or with letters?
     * TODO: can we assume carrier is always 2 letter?
     * @param s
     * @return
     */
    public static String[] separateCarrierAndFlightNumber(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        final int MAX_FLIGHT_NUM_LENG = 4;
        final int MIN_CARRIER_LENG = 2;
        
        StringBuffer fn = new StringBuffer();
        int j;
        for (j = s.length() - 1; j >= 0; j--) {
            char c = s.charAt(j);
            if (Character.isDigit(c)) {
                fn.append(c);
                if (s.length() - fn.length() == MIN_CARRIER_LENG) {
                    break;
                } else if (fn.length() == MAX_FLIGHT_NUM_LENG) {
                    break;
                }
            } else {
                break;
            }
        }
        
        String carrier = s.substring(0, s.length() - fn.length());
        return new String[] { carrier, fn.reverse().toString() };
    }
    
    public static String prepTelephoneNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return number.replaceAll("[^0-9]", "");
    }
       
    public static Integer returnNumberOrNull(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * rules for setting calculated field 'flightDate'
     */
    public static Date determineFlightDate(Date etd, Date eta, Date transmissionDate) {
        Date d = null;
        if (etd != null) {
            d = etd;
        } else if (eta != null) {
            d = eta;
        } else {
            d = transmissionDate;
        }

        if (d != null) {
            return DateUtils.stripTime(d);
        }
        
        return null;
    }
    
    public static String padFlightNumberWithZeroes(String fn) {
        StringBuffer buff = new StringBuffer();
        for (int j=0; j<4 - fn.length(); j++) {
            buff.append("0");
        }
        buff.append(fn);
        return buff.toString();
    }
}
