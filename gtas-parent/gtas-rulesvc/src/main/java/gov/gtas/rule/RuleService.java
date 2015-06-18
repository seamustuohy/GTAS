package gov.gtas.rule;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.Message;

/**
 * The interface for the Rule Engine.
 * 
 * @author GTAS3 (AB)
 *
 */
public interface RuleService {
	/**
	 * Execute the rule engine on the specified request for the specified rule
	 * set.
	 * 
	 * @param ruleSetName
	 *            the name of the rule set to invoke the engine on.
	 * @param req
	 *            the rule request message.
	 * @return the result of the rule engine invocation.
	 */
	RuleServiceResult invokeRuleset(String ruleSetName, RuleServiceRequest req);

	/**
	 * Execute the rule engine on the specified request for the default rule
	 * set.
	 * 
	 * @param req
	 *            the rule request message.
	 * @return the result of the rule engine invocation.
	 */
	RuleServiceResult invokeRuleset(RuleServiceRequest req);

	/**
	 * Create a rule engine request message from a message object from the
	 * domain model.
	 * 
	 * @param requestMessage
	 *            the message object.
	 * @return the constructed rule engine request suitable for Rule Engine
	 *         invocation.
	 */
	RuleServiceRequest createRuleServiceRequest(Message requestMessage);
}
