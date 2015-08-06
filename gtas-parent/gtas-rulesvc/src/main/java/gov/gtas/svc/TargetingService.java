/**
 * 
 */
package gov.gtas.svc;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.ApisMessage;
import gov.gtas.rule.RuleServiceResult;

/**
 * The API for the Targeting Service.
 * 
 * @author GTAS3 (AB)
 *
 */

public interface TargetingService {
	/**
	 * Invokes the Targeting service for an API message.
	 * 
	 * @param message
	 *            the API message.
	 * @return the result of the invocation.
	 */
	RuleServiceResult analyzeApisMessage(ApisMessage message);

	/**
	 * Invokes the Targeting service for an API message.
	 * 
	 * @param message
	 *            the API message.
	 * @return the result of the invocation.
	 */
	RuleServiceResult analyzeApisMessage(long messageId);

	/**
	 * Invokes the Rule Engine on an arbitrary list of objects using the
	 * specified DRL rules string
	 * 
	 * @param request
	 *            The rule request containing an arbitrary list of request
	 *            objects to be inserted into the Rule Engine context.
	 * @param drlRules
	 *            The DROOLS rules to apply on the request objects.
	 * @return the result of the invocation.
	 */
	RuleServiceResult applyRules(RuleServiceRequest request, String drlRules);

	/**
	 * Running Rule Engine through Scheduler
	 */
	public void runningRuleEngine();
}
