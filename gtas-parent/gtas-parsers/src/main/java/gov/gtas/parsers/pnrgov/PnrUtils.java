package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.segment.ADD;
import gov.gtas.parsers.pnrgov.segment.SSR;
import gov.gtas.parsers.pnrgov.segment.TIF;
import gov.gtas.parsers.pnrgov.segment.TIF.TravelerDetails;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.vo.passenger.AddressVo;
import gov.gtas.parsers.vo.passenger.DocumentVo;
import gov.gtas.parsers.vo.passenger.PassengerVo;
import gov.gtas.parsers.vo.passenger.PhoneVo;

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
    
    /**
     * TODO: was not handling the 2nd example below b/c of extra field.
     * check whether it's an error in the message or not.
     * 
     * example:
     * /P/GBR/123456789/GBR/12JUL64/M/23AUG19/SMITHJR/JONATHON/ROBERT
     * / /   /         /   /GBR/12JUL64/M//JONES/WILLIAMNEVELL
     */
    public static PassengerVo createPassenger(SSR ssr, TIF tif) throws ParseException {
        final String dateFormat = "ddMMMyy";
        List<String> strs = splitSsrFreeText(ssr);
        if (strs == null) {
            return null;
        }
        
        PassengerVo p = new PassengerVo();
        p.setPassengerType("P");
        DocumentVo doc = new DocumentVo();
        p.addDocument(doc);
   
        doc.setDocumentType(safeGet(strs, 1));
        doc.setIssuanceCountry(safeGet(strs, 2));
        doc.setDocumentNumber(safeGet(strs, 3));
        p.setCitizenshipCountry(safeGet(strs, 4));
        String d = safeGet(strs, 5);
        if (StringUtils.isNotBlank(d)) {
            Date dob = ParseUtils.parseDateTime(d, dateFormat);
            p.setDob(dob);
            p.setAge(ParseUtils.calculateAge(dob));
        }
        p.setGender(safeGet(strs, 6));
        d = safeGet(strs, 7);
        if (StringUtils.isNotBlank(d)) {
            doc.setExpirationDate(ParseUtils.parseDateTime(d, dateFormat));
        }
        
        processNames(p, safeGet(strs, 8), safeGet(strs, 9), safeGet(strs, 10));
        
        if (tif.getTravelerDetails().size() > 0) {
            TravelerDetails td = tif.getTravelerDetails().get(0);
            p.setTravelerReferenceNumber(td.getTravelerReferenceNumber());            
        }
        
        return p;
    }
    
    public static PassengerVo createPassenger(TIF tif) throws ParseException {
        if (tif.getTravelerDetails().size() > 0) {
            PassengerVo p = new PassengerVo();
            p.setPassengerType("P");
            TravelerDetails td = tif.getTravelerDetails().get(0);
            p.setLastName(tif.getTravelerSurname());
            p.setFirstName(td.getTravelerGivenName());
            p.setTravelerReferenceNumber(td.getTravelerReferenceNumber());
            return p;
        }
        
        return null;
    }
    
    public static AddressVo createAddress(ADD add) {
        AddressVo rv = new AddressVo();
        rv.setType(add.getAddressType());
        rv.setLine1(add.getStreetNumberAndName());
        rv.setCity(add.getCity());
        rv.setState(add.getStateOrProvinceCode());
        rv.setCountry(add.getCountryCode());
        rv.setPostalCode(add.getPostalCode());
        rv.setPhoneNumber(ParseUtils.prepTelephoneNumber(add.getTelephone()));
        return rv;
    }

    /**
     * SSR+DOCA:HK:1:TZ:::::/D/AUS/13 SHORE AVENUE/BROADBEACH/QLD/4215+::43577'
     */
    public static AddressVo createAddress(SSR ssr) {
        List<String> strs = splitSsrFreeText(ssr);
        if (strs == null) {
            return null;
        }

        AddressVo rv = new AddressVo();
        rv.setCountry(safeGet(strs, 2));
        rv.setLine1(safeGet(strs, 3));
        rv.setLine2(safeGet(strs, 4));
        rv.setCity(safeGet(strs, 5));
        rv.setPostalCode(safeGet(strs, 6));
        return rv;
    }
    
    public static PhoneVo createPhone(String number) {
        PhoneVo rv = new PhoneVo();
        rv.setNumber(ParseUtils.prepTelephoneNumber(number));
        return rv;
    }
    
    public static <T> T safeGet(List<T> list, int i) {
        if (i < 0 || i >= list.size()) {
            return null;
        }
        return list.get(i);
    }
    
    private static final String[] SUFFIXES = { "JR", "SR", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV" };
    private static final String[] PREFIXES = { "MR", "MRS", "MS", "DR", "MISS", "SIR", "MADAM", "MAYOR", "PRESIDENT" };
    
    private static void processNames(PassengerVo p, String last, String first, String middle) {
        p.setFirstName(first);
        p.setMiddleName(middle);
        p.setLastName(last);
        
        if (first != null) {
            for (String prefix : PREFIXES) {
                if (first.startsWith(prefix)) {
                    p.setTitle(prefix);
                    p.setFirstName(first.substring(prefix.length()).trim());
                    break;
                } else if (first.endsWith(prefix)) {
                    p.setTitle(prefix);
                    String firstName = first.substring(0, first.length() - prefix.length()).trim();
                    p.setFirstName(firstName);
                }
            }
        }
        
        if (last != null) {
            for (String suffix : SUFFIXES) {
                if (last.endsWith(suffix)) {
                    p.setSuffix(suffix);
                    String lastName = last.substring(0, last.length() - suffix.length()).trim();
                    p.setLastName(lastName);
                    break;
                }
            }
        }
    }
    
    private static List<String> splitSsrFreeText(SSR ssr) {
        if (ssr.getFreeText() != null) {
            List<String> strs = new ArrayList<>();
            for (String s : ssr.getFreeText().split("/")) {
                strs.add(s.trim());
            }
            return strs;
        }
        return null;
    }
}
