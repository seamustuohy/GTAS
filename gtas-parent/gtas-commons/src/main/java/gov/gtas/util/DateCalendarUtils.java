package gov.gtas.util;

import gov.gtas.model.udr.UdrConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	// TODO create utility methods using Java 8 java.time

	private static final long MILLIS_IN_ONE_DAY = 86400000L;
	public static final String addOneDayToJsondateString(String jsonDateString) throws ParseException{
		Date dt = parseJsonDate(jsonDateString);
		dt = new Date(dt.getTime()+MILLIS_IN_ONE_DAY);
		return formatJsonDate(dt);
	}
	/**
	 * Calculates the offset a date time offset from GMT.
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
			return DateUtils.truncatedCompareTo(dt1, dt2, granularity) > 0 ? true
					: false;
		} else {
			return false;
		}
	}

	public static boolean dateRoundedLess(Date dt1, Date dt2, int granularity) {
		if (dt1 != null && dt2 != null) {
			return DateUtils.truncatedCompareTo(dt1, dt2, granularity) < 0 ? true
					: false;
		} else {
			return false;
		}
	}

	public static Date parseJsonDate(final String dateString)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(
				UdrConstants.UDR_DATE_FORMAT);
		return format.parse(dateString);
	}
	public static Date parseJsonDateTime(final String dateString)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(
				UdrConstants.UDR_DATETIME_FORMAT);
		return format.parse(dateString);
	}

	public static String formatJsonDate(final Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				UdrConstants.UDR_DATE_FORMAT);
		return format.format(date);
	}
	public static Date parseRuleEngineDate(final String dateString)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(
				UdrConstants.RULE_ENGINE_DATE_FORMAT);
		return format.parse(dateString);
	}

	public static String formatRuleEngineDate(final Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				UdrConstants.RULE_ENGINE_DATE_FORMAT);
		return format.format(date);
	}
	public static String formatRuleEngineDateTime(final Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				UdrConstants.RULE_ENGINE_DATETIME_FORMAT);
		return format.format(date);
	}
}
