package gov.gtas.error;

import static gov.gtas.error.CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.INVALID_USER_ID_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.INVALID_USER_ID_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_MESSAGE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import gov.gtas.error.CommonErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Error Handler for the Rule Engine related functionality.
 * 
 * @author GTAS3 (AB)
 *
 */
//@Component
public class BasicErrorHandler implements ErrorHandler {
	
	/*
	 * The logger for the Rule Engine Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BasicErrorHandler.class);

	/*
	 * The map of all error codes handled by this handler.
	 */
	private final Map<String, String> errorMap;

	/*
	 * The first handler in the delegate chain for this error handler;
	 */
	private  ErrorHandler delegate;
	
	public BasicErrorHandler() {
		errorMap = new HashMap<String, String>();
		errorMap.put(NULL_ARGUMENT_ERROR_CODE, NULL_ARGUMENT_ERROR_MESSAGE);
		errorMap.put(INVALID_USER_ID_ERROR_CODE, INVALID_USER_ID_ERROR_MESSAGE);
		errorMap.put(INPUT_JSON_FORMAT_ERROR_CODE, INPUT_JSON_FORMAT_ERROR_MESSAGE);
		errorMap.put(UPDATE_RECORD_MISSING_ERROR_CODE,
				UPDATE_RECORD_MISSING_ERROR_MESSAGE);
		errorMap.put(QUERY_RESULT_EMPTY_ERROR_CODE,
				QUERY_RESULT_EMPTY_ERROR_MESSAGE);
	}

	/* (non-Javadoc)
	 * @see gov.gtas.error.GtasErrorHandler#addErrorHandlerDelegate(gov.gtas.error.GtasErrorHandler)
	 */
	@Override
	public void addErrorHandlerDelegate(ErrorHandler errorHandler) {
		if(this.delegate == null){
			this.delegate = errorHandler;
		}else{
			this.delegate.addErrorHandlerDelegate(errorHandler);
		}
	}
	
	/* (non-Javadoc)
	 * @see gov.gtas.error.GtasErrorHandler#createException(java.lang.String, java.lang.Object[])
	 */
	@Override
	public CommonServiceException createException(String errorCode,
			Object... args) {
		CommonServiceException ret = null;
		final String errorMessage = errorMap.get(errorCode);
		if (errorMessage != null) {
			ret = createExceptionAndLog(errorCode, errorMessage, args);
		} else if (this.delegate != null){
			ret = this.delegate.createException(errorCode, args);
		}
		if (ret == null) {
			ret = createExceptionAndLog(
					CommonErrorConstants.UNKNOWN_ERROR_CODE,
					CommonErrorConstants.UNKNOWN_ERROR_CODE_MESSAGE, errorCode);
		}
		return ret;
	}
	
    /* (non-Javadoc)
	 * @see gov.gtas.error.ErrorHandler#processError(java.lang.Exception)
	 */
	@Override
	public ErrorDetails processError(final Exception exception) {
		logger.error(exception.getMessage());
		return new ErrorDetails() {
			
			@Override
			public List<String> getWarningMessages() {
				return null;
			}
			
			@Override
			public String getFatalErrorMessage() {
				if(exception instanceof CommonServiceException){
					return ((CommonServiceException)exception).getMessage();
				}else{
				    return String.format(CommonErrorConstants.SYSTEM_ERROR_MESSAGE,
							System.currentTimeMillis());
				}
			}
			
			@Override
			public String getFatalErrorCode() {
				if(exception instanceof CommonServiceException){
					return ((CommonServiceException)exception).getErrorCode();
				}else{
				    return CommonErrorConstants.SYSTEM_ERROR_CODE;
				}
			}
		};
	}

	/**
     * Adds the error code to the list of errors managed by this handler.
     * @param errCode the error code to add.
     * @param errMessage the corresponding error message.
     */
	protected void addErrorCodeToHandlerMap(String errCode, String errMessage) {
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
	protected CommonServiceException createExceptionAndLog(String errorCode,
			String errorMessageTemplate, Object... errorMessageArgs) {
		String message = String.format(errorMessageTemplate, errorMessageArgs);
		logger.error(message);
		return new CommonServiceException(errorCode, message);

	}
}
