package gov.gtas.error;

import gov.gtas.constant.RuleServiceConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Error Handler for the Rule Engine related functionality.
 * 
 * @author GTAS3 (AB)
 *
 */
public class RuleServiceErrorHandler extends BasicErrorHandler {
	/*
	 * The logger for the Rule Engine Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(RuleServiceErrorHandler.class);

	public RuleServiceErrorHandler() {
		super();
		logger.info("RuleServiceErrorHandler - initializing handler map");
		super.addErrorCodeToHandlerMap(
				RuleServiceConstants.RULE_COMPILE_ERROR_CODE,
				RuleServiceConstants.RULE_COMPILE_ERROR_MESSAGE);
	}
}
