/**
 * 
 */
package gov.gtas.svc;

import java.util.List;
import java.util.Set;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Pnr;
import gov.gtas.svc.util.RuleExecutionContext;

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
	List<RuleHitDetail> analyzeLoadedPnr();

	/**
	 * Invokes the Targeting service for all unprocessed PNR and APIS messages.
	 * 
	 * @param updateProcesssedMessageStatus
	 *            it true, then the Targeting Service will update the status of
	 *            each processed message.
	 * @param statusToLoad
	 *            status of the APIS/PNR messages to load.
	 * @param statusAfterProcesssing
	 *            status to update after processing.
	 * @return the result of the invocation.
	 */
	RuleExecutionContext analyzeLoadedMessages(MessageStatus statusToLoad,
			MessageStatus statusAfterProcesssing,
			boolean updateProcesssedMessageStatus);

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
	 * Running Rule Engine through Scheduler.
	 * @return list of unique flight id's that were updated
	 */
	public Set<Long> runningRuleEngine();
	
	/**
	 * Update the rule and watchlist hit counts for a set of flight id's.
	 * 
	 * Initially I did not want to create a separate method for this and
	 * just include the logic in the rule runner, but after some testing on
	 * large amounts of data, it seems that hibernate should flush the
	 * hits to the hits_summary table prior to computing the updated hit
	 * counts; otherwise we get inaccurate results. 
	 * @param flights set of flight id's to update
	 */
    public void updateFlightHitCounts(Set<Long> flights);

	/**
	 * retrieve ApisMessage from db
	 */
	public List<ApisMessage> retrieveApisMessage(MessageStatus messageStatus);

	/**
	 * update ApisMessage with message status
	 */
	public void updateApisMessage(ApisMessage message, MessageStatus messageStatus);

	/**
	 * retrieve ApisMessage from db
	 */
	public List<Pnr> retrievePnr(MessageStatus messageStatus);

	/**
	 * update Pnr with message status
	 */
	public void updatePnr(Pnr message, MessageStatus messageStatus);
}
