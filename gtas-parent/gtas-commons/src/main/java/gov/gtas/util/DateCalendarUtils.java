package gov.gtas.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Basic adaptation of commons DateUtils.
 * @author GTAS3
 *
 */
public class DateCalendarUtils {
	public static boolean dateRoundedEquals(Date dt1, Date dt2){
		return dateRoundedEquals(dt1, dt2, Calendar.SECOND);
	}
	public static boolean dateRoundedEquals(Date dt1, Date dt2, int granularity){
		if(dt1 != null && dt2 != null){
			return DateUtils.truncatedEquals(dt1,  dt2, granularity);
		} else {
			return dt1 == dt2;
		}
	}

}
