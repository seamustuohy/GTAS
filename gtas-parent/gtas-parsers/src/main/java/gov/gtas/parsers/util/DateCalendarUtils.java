package gov.gtas.parsers.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Basic adaptation of commons DateUtils.
 * 
 * @author GTAS3 (AB)
 *
 */
public class DateCalendarUtils {
	private static final long MILLIS_IN_ONE_DAY = 86400000L;

    public static Date addOneDayToDate(Date date) {
        return new Date(date.getTime() + MILLIS_IN_ONE_DAY);
    }

    public static Date subtractOneDayFromDate(Date date) {
        return new Date(date.getTime() - MILLIS_IN_ONE_DAY);
    }
	
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
	
	/**
	 * return starting and ending dates within a day 
	 */
	public static Date[] getStartAndEndDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();    
	    
	    cal.set(Calendar.HOUR_OF_DAY, 23);
	    cal.set(Calendar.MINUTE, 59);
	    cal.set(Calendar.SECOND, 59);
	    Date endDate = cal.getTime();
	    
	    return new Date[] { startDate, endDate };
	}
	
	/**
	 * Calculates a date time offset from GMT.
	 * 
	 * @param gmtDate
	 *            the input GMT date.
	 * @return the local date.
	 */
	public static Long calculateOffsetFromGMT(Date date) {
		Calendar cal = Calendar.getInstance();
		/*
		 * get the default time zone object and then calculate the signed offset
		 * from GMT.
		 */
		long offset = cal.getTimeZone().getOffset(date.getTime());
		return offset;
	}

	public static boolean dateRoundedEquals(Date dt1, Date dt2) {
		return dateRoundedEquals(dt1, dt2, Calendar.SECOND);
	}

	public static boolean dateRoundedEquals(Date dt1, Date dt2, int granularity) {
		if (dt1 != null && dt2 != null) {
			return DateUtils.truncatedEquals(dt1, dt2, granularity);
		} else {
			return dt1 == dt2;
		}
	}

	public static boolean dateRoundedGreater(Date dt1, Date dt2, int granularity) {
		if (dt1 != null && dt2 != null) {
            return DateUtils.truncatedCompareTo(dt1, dt2, granularity) > 0 ? true : false;
		} else {
			return false;
		}
	}

	public static boolean dateRoundedLess(Date dt1, Date dt2, int granularity) {
		if (dt1 != null && dt2 != null) {
            return DateUtils.truncatedCompareTo(dt1, dt2, granularity) < 0 ? true : false;
		} else {
			return false;
		}
	}
}
