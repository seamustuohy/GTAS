/**
 * 
 */
package gov.gtas.svc;

import gov.gtas.model.ApisMessage;
import gov.gtas.rule.RuleServiceResult;


/**
 * The API for the Targeting Service.
 * @author GTAS3 (AB)
 *
 */

public interface TargetingService {
	/**
	 * Targeting service invocation for API messages.
	 * @param message the API message.
	 * @return the result of the invocation.
	 */
	RuleServiceResult analyzeApisMessage(ApisMessage message);
}
