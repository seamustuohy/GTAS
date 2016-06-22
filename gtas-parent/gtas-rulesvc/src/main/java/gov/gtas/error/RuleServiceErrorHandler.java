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
        super.addErrorCodeToHandlerMap(
                RuleServiceConstants.KB_CREATION_IO_ERROR_CODE,
                RuleServiceConstants.KB_CREATION_IO_ERROR_MESSAGE);
        super.addErrorCodeToHandlerMap(
                RuleServiceConstants.KB_NOT_FOUND_ERROR_CODE,
                RuleServiceConstants.KB_NOT_FOUND_ERROR_MESSAGE);
        super.addErrorCodeToHandlerMap(
                RuleServiceConstants.KB_INVALID_ERROR_CODE,
                RuleServiceConstants.KB_INVALID_ERROR_MESSAGE);
        super.addErrorCodeToHandlerMap(
                RuleServiceConstants.MESSAGE_NOT_FOUND_ERROR_CODE,
                RuleServiceConstants.MESSAGE_NOT_FOUND_ERROR_MESSAGE);
        super.addErrorCodeToHandlerMap(
                RuleServiceConstants.RULE_ENGINE_RUNNER_ERROR_CODE,
                RuleServiceConstants.RULE_ENGINE_RUNNER_ERROR_MESSAGE);
    }
}
