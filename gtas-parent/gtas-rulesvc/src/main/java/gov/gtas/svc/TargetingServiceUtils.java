package gov.gtas.svc;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.PnrMessage;
import gov.gtas.svc.request.builder.RuleEngineRequestBuilder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TargetingServiceUtils {
	/**
	 * Creates a request from a API message.
	 * 
	 * @param req
	 *            the API message.
	 * @return RuleServiceRequest object.
	 */
	public static RuleServiceRequest createApisRequest(final ApisMessage req) {
		Collection<ApisMessage> apisMessages = new LinkedList<ApisMessage>();
		apisMessages.add(req);
		return createPnrApisRequest(apisMessages, null);
	}

	/**
	 * Creates a request from a PNR message.
	 * 
	 * @param req
	 *            the PNR message.
	 * @return RuleServiceRequest object.
	 */
	public static RuleServiceRequest createPnrRequest(final PnrMessage req) {
		Collection<PnrMessage> pnrMessages = new LinkedList<PnrMessage>();
		pnrMessages.add(req);
		return createPnrApisRequest(null, pnrMessages);
	}

	/**
	 * Create a rule engine request message from a list of message objects from the
	 * domain model.
	 * 
	 * @param reqList
	 *            the message objects.
	 * @return the constructed rule engine request suitable for Rule Engine
	 *         invocation.
	 */
	public static RuleServiceRequest createApisRequest(
			final List<ApisMessage> reqList) {
		return createPnrApisRequest(reqList, null);
	}

	/**
	 * Create a rule engine request message from a list of message objects from the
	 * domain model.
	 * 
	 * @param reqList
	 *            the message objects.
	 * @return the constructed rule engine request suitable for Rule Engine
	 *         invocation.
	 */
	public static RuleServiceRequest createPnrRequest(
			final List<PnrMessage> reqList) {
		return createPnrApisRequest(null, reqList);
	}

	/**
	 * Creates a Rule Engine request containing data from a collection of APIS
	 * and PNR messages.
	 * 
	 * @param apisMessages
	 * @param pnrMessages
	 * @return the rule engine request object.
	 */
	public static RuleServiceRequest createPnrApisRequest(
			final Collection<ApisMessage> apisMessages,
			final Collection<PnrMessage> pnrMessages) {
		RuleEngineRequestBuilder bldr = new RuleEngineRequestBuilder();
		if (pnrMessages != null) {
			for (PnrMessage msg : pnrMessages) {
				bldr.addPnrMessage(msg);
			}
		}
		if (apisMessages != null) {
			for (ApisMessage msg : apisMessages) {
				bldr.addApisMessage(msg);
			}
		}
		return bldr.build();
	}
    /**
     * Creates an anonymous RuleServiceRequest object.
     * @param requestType
     * @param reqObjects
     * @return
     */
//	public static RuleServiceRequest createRuleServiceRequest(
//			final RuleServiceRequestType requestType, final List<?> reqObjects) {
//		return new RuleServiceRequest() {
//			public List<?> getRequestObjects() {
//				return reqObjects;
//			}
//
//			public RuleServiceRequestType getRequestType() {
//				return requestType;
//			}
//
//		};
//
//	}

	
}
