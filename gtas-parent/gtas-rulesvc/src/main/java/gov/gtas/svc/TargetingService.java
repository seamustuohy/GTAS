/**
 * 
 */
package gov.gtas.svc;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.rule.RuleServiceResult;

import java.util.List;

/**
 * The API for the Targeting Service.
 * 
 * @author GTAS3 (AB)
 *
 */

public interface TargetingService {
	/**
	 * Targeting service invocation for API messages.
	 * 
	 * @param message
	 *            the API message.
	 * @return the result of the invocation.
	 */
	RuleServiceResult analyzeApisMessage(ApisMessage message);

	/**
	 * Targeting service invocation for API messages.
	 * 
	 * @param request
	 *            The rule request.
	 * @param drlRules
	 *            The DROOLS rules to apply on the request objects.
	 * @return the result of the invocation.
	 */
	RuleServiceResult applyRules(RuleServiceRequest request, String drlRules);

	/**
	 * 
	 * @param messageStatus
	 * @return the retrieved ApisMessage
	 */
	public List<ApisMessage> retrieveApisMessage(MessageStatus messageStatus);
}
