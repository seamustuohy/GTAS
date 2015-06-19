package gov.gtas.error;

import gov.gtas.error.CommonErrorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
    /**
     * Creates the exception message for the indicated error.
     * @param errorCode the error code.
     * @param args the error arguments providing context for the error.
     * @return the error exception object.
     */
	public CommonServiceException createException(final String errorCode,
			final Object... args) {
		CommonServiceException ret = null;
		switch (errorCode) {
		case CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE:
			ret = createExceptionAndLog(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE, args);
			break;
		case CommonErrorConstants.INVALID_USER_ID:
			ret = createExceptionAndLog(
					CommonErrorConstants.INVALID_USER_ID,
					CommonErrorConstants.INVALID_USER_ID_ERROR_MESSAGE, args[0]);
			break;
		default:
			break;
		}

		return ret;
	}
    /**
     * Creates the exception for the indicated error and also logs the error.
     * @param errorCode the error code.
     * @param errorMessageTemplate string template for the error message.
     * @param errorMessageArgs the arguments for the error message template.
     * @return the exception object.
     */
	private CommonServiceException createExceptionAndLog(String errorCode,
			String errorMessageTemplate, Object... errorMessageArgs) {
		String message = String.format(errorMessageTemplate, errorMessageArgs);
		logger.error(message);
		return new CommonServiceException(errorCode, message);

	}
}
