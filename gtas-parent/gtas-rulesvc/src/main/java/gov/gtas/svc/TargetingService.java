/**
 * 
 */
package gov.gtas.svc;

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
	 * 
	 * @param messageStatus
	 * @return the retrieved ApisMessage
	 */
	public List<ApisMessage> retrieveApisMessage(MessageStatus messageStatus);
}
