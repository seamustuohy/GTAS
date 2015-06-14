/**
 * 
 */
package gov.cbp.taspd.gtas.svc;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.rule.RuleServiceResult;


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
