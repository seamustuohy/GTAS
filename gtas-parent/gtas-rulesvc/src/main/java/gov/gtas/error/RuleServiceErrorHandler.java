package gov.gtas.error;

import javax.annotation.PostConstruct;

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
public class RuleServiceErrorHandler extends BasicErrorHandler{
	/*
	 * The logger for the Rule Engine Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(RuleServiceErrorHandler.class);
	
	@PostConstruct
	protected void initHandler(){
		ErrorHandlerFactory.registerErrorHandler(this);
		super.addErrorCodeToHandlerMap(RuleServiceConstants.RULE_COMPILE_ERROR_CODE, RuleServiceConstants.RULE_COMPILE_ERROR_MESSAGE);
		super.addErrorCodeToHandlerMap(RuleServiceConstants.INCOMPLETE_TREE_ERROR_CODE, RuleServiceConstants.INCOMPLETE_TREE_ERROR_MESSAGE);
	}	
}
