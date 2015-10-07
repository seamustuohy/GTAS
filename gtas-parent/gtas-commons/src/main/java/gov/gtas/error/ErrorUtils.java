package gov.gtas.error;

import gov.gtas.constant.CommonErrorConstants;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Error processing utility functions.
 * 
 * @author GTAS3 (AB)
 *
 */
public class ErrorUtils {
	/**
	 * Searches the exception cause chain to determine if any cause is of the
	 * given type.
	 * 
	 * @param exception
	 *            the exception to search.
	 * @param typeName
	 *            the name of the exception class to search for.
	 * @return true if the exception type is found.
	 */
	public static boolean isExceptionOfType(Throwable exception, String typeName) {
		boolean ret = false;
		Throwable cause = exception;
		while (cause != null) {
			String name = cause.getClass().getName();
			ret = name.indexOf(typeName) >= 0;
			if (ret) {
				break;
			} else {
				cause = cause.getCause();
			}
		}
		return ret;
	}

	/**
	 * Searches the exception cause chain to determine if any cause is of the
	 * given type.
	 * 
	 * @param exception
	 *            the exception to search.
	 * @param typeName
	 *            the name of the exception class to search for.
	 * @return true if the exception type is found.
	 */
	public static boolean isConstraintViolationException(Throwable exception,
			String constraintName) {
		boolean ret = false;
		Throwable cause = exception;
		while (cause != null) {
			if (cause instanceof ConstraintViolationException
					&& ((ConstraintViolationException) cause)
							.getConstraintName()
							.equals(constraintName)) {
				ret = true;
				break;
			} else {
				cause = cause.getCause();
			}
		}
		return ret;
	}
    public static ErrorDetails createErrorDetails(CommonServiceException exception){
    	BasicErrorDetails ret = createErrorDetails(exception.getErrorCode(), exception, false);
     	return ret;
    }
    public static BasicErrorDetails createErrorDetails(Exception exception){
    	return createErrorDetails(CommonErrorConstants.SYSTEM_ERROR_CODE, exception, true);
    }
    public static BasicErrorDetails createErrorDetails(String errorCode, Exception exception, boolean addStackTrace){
    	Long id = System.currentTimeMillis();
     	BasicErrorDetails ret = new BasicErrorDetails(id, errorCode, exception.getMessage(), null);
     	if(addStackTrace){
     		List<String> details = new LinkedList<String>();
     		constructDetails(exception, details);
     		ret.setErrorDetails(details);
     	}
    	return ret;
    }
    private static void constructDetails(Throwable exception, List<String> details){
    	details.add("Exception class:"+exception.getClass().getSimpleName());
    	details.add("Exception messsage:"+exception.getMessage());
    	for(StackTraceElement el:exception.getStackTrace()){
    		details.add(el.toString());
    	}
    	if(exception.getCause() != null){
    		details.add(">>>>>>>> Caused by:");
    		constructDetails(exception.getCause(), details);
    	}
    }

	private static final int MAX_ERROR_LENG = 4000;

	public static String getStacktrace(Exception e) {
		String stacktrace = ExceptionUtils.getStackTrace(e);
		if (stacktrace.length() > MAX_ERROR_LENG) {
			stacktrace = stacktrace.substring(0, MAX_ERROR_LENG);
		}
		return stacktrace;
	}
}
