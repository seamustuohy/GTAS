package gov.gtas.parsers.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

/**
 */
public class DateUtils {
	/**
	 * set the time portion of a Date to all 0's
	 */
    public static Date stripTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static Integer calculateAge(Date dob) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
         
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(year, month + 1, day);  // cal is 0-based. yuck
        Period p = Period.between(birthday, today);
        return p.getYears();
    }    
}
