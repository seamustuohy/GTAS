package gov.gtas.svc;

import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.BasicErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.ApisMessage;
import gov.gtas.rule.RuleService;
import gov.gtas.rule.RuleServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Implementation of the Targeting Service API.
 * @author GTAS3 (AB)
 *
 */
@Service
public class TargetingServiceImpl implements TargetingService {
    /* The rule engine to be used. */
	private final RuleService ruleService;
    /**
     * Constructor obtained from the spring context by auto-wiring.
     * @param rulesvc the auto-wired rule engine instance.
     */
	@Autowired
	public TargetingServiceImpl(final RuleService rulesvc) {
		ruleService = rulesvc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.TargetingService#analyzeApisMessage(gov.gtas.model.ApisMessage)
	 */
	@Override
	public RuleServiceResult analyzeApisMessage(ApisMessage message) {
		if (null == message) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE,
					"ApisMessage", "TargetingServiceImpl.analyzeApisMessage()");
		}

		RuleServiceResult res = ruleService.invokeRuleset(ruleService
				.createRuleServiceRequest(message));
		return res;
	}

}
