package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.segment.SSR;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.vo.air.DocumentVo;
import gov.gtas.parsers.vo.air.PassengerVo;

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
    
    public static void createDocument(SSR ssr, PassengerVo pax) {
        // /P/GBR/123456789/GBR/12JUL64/M/23AUG19/SMITH JR/JONATHON/ROBERT
        String[] tmp = ssr.getFreeText().split("/");
        List<String> strs = new ArrayList<>(Arrays.asList(tmp)); 
        DocumentVo doc = new DocumentVo();
        doc.setDocumentType(safeGet(strs, 1));
        doc.setIssuanceCountry(safeGet(strs, 2));
        doc.setDocumentNumber(safeGet(strs, 3));
        pax.getDocuments().add(doc);

        pax.setGender(safeGet(strs, 6));
        pax.setLastName(safeGet(strs, 8));
        pax.setFirstName(safeGet(strs, 9));
        pax.setMiddleName(safeGet(strs, 10));
    }
    
    public static <T> T safeGet(List<T> list, int i) {
        if (i < 0 || i >= list.size()) {
            return null;
        }
        return list.get(i);
    }
}
