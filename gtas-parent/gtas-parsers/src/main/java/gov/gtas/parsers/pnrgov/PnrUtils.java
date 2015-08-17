package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.segment.REF;
import gov.gtas.parsers.pnrgov.segment.SSR;
import gov.gtas.parsers.pnrgov.segment.TIF;
import gov.gtas.parsers.pnrgov.segment.TIF.TravelerDetails;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.vo.passenger.DocumentVo;
import gov.gtas.parsers.vo.passenger.PassengerVo;

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
     * /P/GBR/123456789/GBR/12JUL64/M/23AUG19/SMITH JR/JONATHON/ROBERT
     * / /   /         /   /GBR/12JUL64/M//JONES/WILLIAMNEVELL
     */
    public static PassengerVo createPassenger(SSR ssr, TIF tif) throws ParseException {
        final String dateFormat = "ddMMMyy";
        String[] tmp = ssr.getFreeText().split("/");
        List<String> strs = new ArrayList<>(Arrays.asList(tmp));
        
        PassengerVo p = new PassengerVo();
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
        p.setLastName(safeGet(strs, 8));
        p.setFirstName(safeGet(strs, 9));
        p.setMiddleName(safeGet(strs, 10));
        p.setPassengerType("P");
        
        TravelerDetails td = tif.getTravelerDetails().get(0);
        p.setReferenceId(td.getTravelerReferenceNumber());
        
        return p;
    }
    
    public static PassengerVo createPassenger(TIF tif) throws ParseException {
        PassengerVo p = new PassengerVo();
        TravelerDetails td = tif.getTravelerDetails().get(0);
        p.setLastName(tif.getTravelerSurname());
        p.setFirstName(td.getTravelerGivenName());
        p.setReferenceId(td.getTravelerReferenceNumber());
        
        return p;
    }
    
    public static <T> T safeGet(List<T> list, int i) {
        if (i < 0 || i >= list.size()) {
            return null;
        }
        return list.get(i);
    }
}
