package gov.cbp.taspd.gtas.svc;

import gov.cbp.taspd.gtas.constant.RuleServiceConstants;
import gov.cbp.taspd.gtas.error.RuleServiceErrorHandler;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.rule.RuleService;
import gov.cbp.taspd.gtas.rule.RuleServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Implementation of the Targeting Service API.
 * @author GTAS3 (AB)
 *
 */
@Service
public class TargetingServiceImpl implements TargetingService {
	/* The spring context supplied error handler component. */
	@Autowired
	private RuleServiceErrorHandler errorHandler;
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
	 * gov.cbp.taspd.gtas.svc.TargetingService#analyzeApisMessage(gov.cbp.taspd
	 * .gtas.model.ApisMessage)
	 */
	@Override
	public RuleServiceResult analyzeApisMessage(ApisMessage message) {
		if (null == message) {
			throw errorHandler.createException(
					RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE,
					"ApisMessage", "TargetingServiceImpl.analyzeApisMessage()");
		}

		RuleServiceResult res = ruleService.invokeRuleset(ruleService
				.createRuleServiceRequest(message));
		return res;
	}

}
