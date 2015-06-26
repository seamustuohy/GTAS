package gov.gtas.model.udr.json.util;

import gov.gtas.error.CommonServiceException;
import gov.gtas.error.udr.UdrErrorConstants;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.util.DateCalendarUtils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Validation utilities for the UDR JSON.
 * @author GTAS3 (AB)
 *
 */
public class JsonValidationUtils {
	
	public static void validateMetaData(final MetaData metaData){
		if (metaData == null) {
			throw new CommonServiceException(
					UdrErrorConstants.NO_META_ERROR_CODE,
					UdrErrorConstants.NO_META_ERROR_MESSAGE);
		}
		final String title = metaData.getTitle();
		if (StringUtils.isEmpty(title)) {
			throw new CommonServiceException(
					UdrErrorConstants.NO_TITLE_ERROR_CODE,
					UdrErrorConstants.NO_TITLE_ERROR_MESSAGE);
		}

		final Date startDate = metaData.getStartDate();
		final Date endDate = metaData.getEndDate();
		
		validateDates(startDate, endDate);
	}
    public static void validateDates(final Date startDate, final Date endDate){
    	Date now = new Date();
		if (startDate == null) {
			throw new CommonServiceException(
					UdrErrorConstants.INVALID_START_DATE_ERROR_CODE,
					UdrErrorConstants.INVALID_START_DATE_ERROR_MESSAGE);
		}
        if(DateCalendarUtils.dateRoundedGreater(now, startDate, Calendar.DATE)){
			throw new CommonServiceException(
					UdrErrorConstants.PAST_START_DATE_ERROR_CODE,
					UdrErrorConstants.PAST_START_DATE_ERROR_MESSAGE);
        	
        }
    	if(DateCalendarUtils.dateRoundedLess(endDate, startDate, Calendar.DATE)){
    			throw new CommonServiceException(
    					UdrErrorConstants.END_LESS_START_DATE_ERROR_CODE,
    					UdrErrorConstants.END_LESS_START_DATE_ERROR_MESSAGE);
            	    		
    	}
    }

}
