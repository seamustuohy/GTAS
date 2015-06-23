package gov.gtas.error;

import gov.gtas.constant.RuleServiceConstants;

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
public class RuleServiceErrorHandler {
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
	public RuleServiceException createException(final String errorCode,
			final Object... args) {
		RuleServiceException ret = null;
		switch (errorCode) {
		case RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE:
			ret = createExceptionAndLog(
					RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE,
					RuleServiceConstants.NULL_ARGUMENT_ERROR_MESSAGE, args);
			break;
		case RuleServiceConstants.RULE_COMPILE_ERROR_CODE:
			ret = createExceptionAndLog(
					RuleServiceConstants.RULE_COMPILE_ERROR_CODE,
					RuleServiceConstants.RULE_COMPILE_ERROR_MESSAGE, args[0]);
			break;
		case RuleServiceConstants.INCOMPLETE_TREE_ERROR_CODE:
			ret = createExceptionAndLog(
					RuleServiceConstants.INCOMPLETE_TREE_ERROR_CODE,
					RuleServiceConstants.INCOMPLETE_TREE_ERROR_MESSAGE, args[0]);
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
	private RuleServiceException createExceptionAndLog(String errorCode,
			String errorMessageTemplate, Object... errorMessageArgs) {
		String message = String.format(errorMessageTemplate, errorMessageArgs);
		logger.error(message);
		return new RuleServiceException(errorCode, message);

	}
}
