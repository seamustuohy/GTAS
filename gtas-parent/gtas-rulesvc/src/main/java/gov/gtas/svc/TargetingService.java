/**
 * 
 */
package gov.gtas.svc;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.PnrMessage;
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
	 * Invokes the Targeting service for all unprocessed API messages.
	 * 
	 * @return the result of the invocation.
	 */
	List<RuleHitDetail> analyzeLoadedApisMessage();
	/**
	 * Invokes the Targeting service for all unprocessed PNR messages.
	 * 
	 * @return the result of the invocation.
	 */
	List<RuleHitDetail> analyzeLoadedPnrMessage();
	
	/**
	 * Invokes the Targeting service for all unprocessed PNR and APIS messages.
	 * 
	 * @return the result of the invocation.
	 */
	List<RuleHitDetail> analyzeLoadedMessages();

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

	/**
	 * retrieve ApisMessage from db
	 */
	public List<ApisMessage> retrieveApisMessage(MessageStatus messageStatus);

	/**
	 * update ApisMessage with message status
	 */
	public void updateApisMessage(ApisMessage message,
			MessageStatus messageStatus);

	/**
	 * retrieve ApisMessage from db
	 */
	public List<PnrMessage> retrievePnrMessage(MessageStatus messageStatus);

	/**
	 * update PnrMessage with message status
	 */
	public void updatePnrMessage(PnrMessage message, MessageStatus messageStatus);
}
