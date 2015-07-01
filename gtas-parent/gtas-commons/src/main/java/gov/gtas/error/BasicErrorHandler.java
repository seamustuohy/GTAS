package gov.gtas.error;

import java.util.HashMap;
import java.util.Map;

//import gov.gtas.error.CommonErrorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static gov.gtas.error.CommonErrorConstants.*;

/**
 * Error Handler for the Rule Engine related functionality.
 * 
 * @author GTAS3 (AB)
 *
 */
@Component
public class BasicErrorHandler {
	/*
	 * The logger for the Rule Engine Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BasicErrorHandler.class);

	private final Map<String, String> errorMap;

	public BasicErrorHandler() {
		errorMap = new HashMap<String, String>();
		errorMap.put(NULL_ARGUMENT_ERROR_CODE, NULL_ARGUMENT_ERROR_MESSAGE);
		errorMap.put(INVALID_USER_ID_ERROR_CODE, INVALID_USER_ID_ERROR_MESSAGE);
		errorMap.put(UPDATE_RECORD_MISSING_ERROR_CODE,
				UPDATE_RECORD_MISSING_ERROR_MESSAGE);
		errorMap.put(QUERY_RESULT_EMPTY_ERROR_CODE,
				QUERY_RESULT_EMPTY_ERROR_MESSAGE);
	}

	/**
	 * Creates the exception message for the indicated error.
	 * 
	 * @param errorCode
	 *            the error code.
	 * @param args
	 *            the error arguments providing context for the error.
	 * @return the error exception object.
	 */
	public CommonServiceException createException(final String errorCode,
			final Object... args) {

		CommonServiceException ret = null;
		final String errorMessage = errorMap.get(errorCode);
		if (errorMessage == null) {
			ret = createExceptionAndLog(
					CommonErrorConstants.UNKNOWN_ERROR_CODE,
					CommonErrorConstants.UNKNOWN_ERROR_CODE_MESSAGE, errorCode);
		} else {
			ret = createExceptionAndLog(errorCode, errorMessage, args);
		}
		// switch (errorCode) {
		// case CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE:
		// ret = createExceptionAndLog(
		// CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
		// CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE, args);
		// break;
		// case CommonErrorConstants.INVALID_USER_ID_ERROR_CODE:
		// ret = createExceptionAndLog(
		// CommonErrorConstants.INVALID_USER_ID_ERROR_CODE,
		// CommonErrorConstants.INVALID_USER_ID_ERROR_MESSAGE, args[0]);
		// break;
		// case CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_CODE:
		// ret = createExceptionAndLog(
		// CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_CODE,
		// CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_MESSAGE, args);
		// break;
		// case CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE:
		// ret = createExceptionAndLog(
		// CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE,
		// CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_MESSAGE, args);
		// break;
		// default:
		// ret = createExceptionAndLog(
		// CommonErrorConstants.UNKNOWN_ERROR_CODE,
		// CommonErrorConstants.UNKNOWN_ERROR_CODE_MESSAGE, errorCode);
		// break;
		// }

		return ret;
	}

	public void addErrorCodeToHandlerMap(String errCode, String errMessage) {
		String msg = errorMap.get(errCode);
		if (msg == null) {
			errorMap.put(errCode, errMessage);
		} else {
			// message already exists - log error
			logger.error(String
					.format("BasicErrorHandler.addErrorCodeToHandlerMap() - Duplicate errorCode '%s' (messsage = '%s').",
							errCode, errMessage));
		}
	}

	/**
	 * Creates the exception for the indicated error and also logs the error.
	 * 
	 * @param errorCode
	 *            the error code.
	 * @param errorMessageTemplate
	 *            string template for the error message.
	 * @param errorMessageArgs
	 *            the arguments for the error message template.
	 * @return the exception object.
	 */
	private CommonServiceException createExceptionAndLog(String errorCode,
			String errorMessageTemplate, Object... errorMessageArgs) {
		String message = String.format(errorMessageTemplate, errorMessageArgs);
		logger.error(message);
		return new CommonServiceException(errorCode, message);

	}
}
