package gov.gtas.error;

import static gov.gtas.error.CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.INVALID_ARGUMENT_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.INVALID_USER_ID_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.INVALID_USER_ID_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_MESSAGE;
import static gov.gtas.error.CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_CODE;
import static gov.gtas.error.CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_MESSAGE;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common Error Handler for the Rule Engine related functionality.
 * 
 * @author GTAS3 (AB)
 *
 */
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
	 * The map of all exception processors used by this handler.
	 */
	private final Map<String, Function<Exception, ErrorDetails>> exceptionProcessorMap;
	
	/*
	 * The first handler in the delegate chain for this error handler;
	 */
	private  ErrorHandler delegate;
	
	public BasicErrorHandler() {
		errorMap = new HashMap<String, String>();
		errorMap.put(NULL_ARGUMENT_ERROR_CODE, NULL_ARGUMENT_ERROR_MESSAGE);
		errorMap.put(INVALID_ARGUMENT_ERROR_CODE, INVALID_ARGUMENT_ERROR_MESSAGE);
		errorMap.put(INVALID_USER_ID_ERROR_CODE, INVALID_USER_ID_ERROR_MESSAGE);
		errorMap.put(INPUT_JSON_FORMAT_ERROR_CODE, INPUT_JSON_FORMAT_ERROR_MESSAGE);
		errorMap.put(UPDATE_RECORD_MISSING_ERROR_CODE,
				UPDATE_RECORD_MISSING_ERROR_MESSAGE);
		errorMap.put(QUERY_RESULT_EMPTY_ERROR_CODE,
				QUERY_RESULT_EMPTY_ERROR_MESSAGE);
		
		exceptionProcessorMap = new HashMap<String, Function<Exception,ErrorDetails>>();
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
		Function<Exception, ErrorDetails> processor = exceptionProcessorMap.get(exception.getClass().getName());
		if(processor != null){
			return processor.apply(exception);
		} else if(this.delegate != null){
			return delegate.processError(exception);
		} else {
			logger.error(exception.getMessage());
			return new BasicErrorDetails(exception);
		}
	}

    /**
     * Adds a custom exception handler using a lambda.
     * @param exceptionClass the exception class to handle.
     * @param processor the lambda.
     */
	protected void addCustomErrorProcesssor(
			Class<? extends Exception> exceptionClass,
			Function<Exception, ErrorDetails> processor) {
		exceptionProcessorMap.put(exceptionClass.getName(), processor);		
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
