package gov.gtas.svc.util;

import gov.gtas.bo.BasicRuleServiceResult;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.enumtype.HitTypeEnum;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Message;
import gov.gtas.model.Pnr;
import gov.gtas.svc.request.builder.RuleEngineRequestBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetingServiceUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(TargetingServiceUtils.class);

	/**
	 * Creates a request from a API message.
	 * 
	 * @param req
	 *            the API message.
	 * @return RuleServiceRequest object.
	 */
	public static RuleExecutionContext createApisRequest(final ApisMessage req) {
		Collection<ApisMessage> apisMessages = new LinkedList<ApisMessage>();
		apisMessages.add(req);
		return createPnrApisRequestContext(apisMessages, null);
	}

	/**
	 * Creates a request from a PNR message.
	 * 
	 * @param req
	 *            the PNR message.
	 * @return RuleServiceRequest object.
	 */
	public static RuleExecutionContext createPnrRequestContext(final Pnr req) {
		Collection<Pnr> pnrs = new LinkedList<Pnr>();
		pnrs.add(req);
		return createPnrApisRequestContext(null, pnrs);
	}

	/**
	 * Create a rule engine request message from a list of message objects from
	 * the domain model.
	 * 
	 * @param reqList
	 *            the message objects.
	 * @return the constructed rule engine request suitable for Rule Engine
	 *         invocation.
	 */
	public static RuleExecutionContext createApisRequestContext(
			final List<ApisMessage> reqList) {
		return createPnrApisRequestContext(reqList, null);
	}

	/**
	 * Create a rule engine request message from a list of message objects from
	 * the domain model.
	 * 
	 * @param reqList
	 *            the message objects.
	 * @return the constructed rule engine request suitable for Rule Engine
	 *         invocation.
	 */
	public static RuleExecutionContext createPnrRequestContext(
			final List<Pnr> reqList) {
		return createPnrApisRequestContext(null, reqList);
	}

	/**
	 * Creates a Rule Engine request containing data from a collection of APIS
	 * and PNR messages.
	 * 
	 * @param apisMessages
	 * @param pnrs
	 * @return the rule engine request object.
	 */
	public static RuleExecutionContext createPnrApisRequestContext(
			final Collection<ApisMessage> apisMessages,
			final Collection<Pnr> pnrs) {
		RuleEngineRequestBuilder bldr = new RuleEngineRequestBuilder();
		if (pnrs != null) {
			for (Pnr msg : pnrs) {
				bldr.addPnr(msg);
			}
		}
		if (apisMessages != null) {
			for (ApisMessage msg : apisMessages) {
				bldr.addApisMessage(msg);
			}
		}
		RuleExecutionContext context = new RuleExecutionContext();
		context.setPaxFlightTuples(bldr.getPassengerFlightSet());
		context.setRuleServiceRequest(bldr.build());
		return context;
	}

	/**
	 * Creates a Rule Engine request containing data from a List of Messages.
	 * 
	 * @param loadedMessages
	 *            List of Messages
	 * @return the rule engine request object.
	 */
	public static RuleExecutionContext createPnrApisRequestContext(
			final List<Message> loadedMessages) {
		RuleEngineRequestBuilder bldr = new RuleEngineRequestBuilder();

		if (loadedMessages != null) {
			for (Message message : loadedMessages) {
				if (message instanceof ApisMessage) {
					bldr.addApisMessage((ApisMessage) message);
				} else if (message instanceof Pnr) {
					bldr.addPnr((Pnr) message);
				}
			}
		}
		RuleExecutionContext context = new RuleExecutionContext();
		context.setPaxFlightTuples(bldr.getPassengerFlightSet());
		context.setRuleServiceRequest(bldr.build());
		return context;
	}

	/**
	 * Eliminates duplicates and adds flight id, if missing.
	 * 
	 * @param result
	 * @return
	 */
	public static RuleServiceResult ruleResultPostProcesssing(
			RuleServiceResult result) {
		// get the list of RuleHitDetail objects returned by the Rule Engine
		List<RuleHitDetail> resultList = result.getResultList();

		// create a Map to eliminate duplicates
		Map<RuleHitDetail, RuleHitDetail> resultMap = new HashMap<>();

		for (RuleHitDetail rhd : resultList) {
			//RuleHitDetail hitDetail = rhd;
			if (rhd.getFlightId() == null) {
				// get all the flights for the passenger
				// and replicate the RuleHitDetail object, for each flight id
				// Note that the RuleHitDetail key is (UdrId, EngineRuleId,
				// PassengerId, FlightId)
				Collection<Flight> flights = rhd.getPassenger().getFlights();
				if (flights != null && flights.size() > 0) {
					try {
						for (Flight flight : flights) {
							RuleHitDetail newrhd = rhd.clone();
                            processPassengerFlight(newrhd, flight.getId(), resultMap);
						}
					} catch (CloneNotSupportedException cnse) {
						cnse.printStackTrace();
					}
				} else {
					//ERROR we do not have flights for this passenger
					logger.error("TargetingServiceUtils.ruleResultPostProcesssing() no flight information for passenger  with ID:"+rhd.getPassenger().getId());
				}
			} else{
				rhd.setPassenger(null);
				processPassengerFlight(rhd, rhd.getFlightId(), resultMap);				
			}
		}
		// Now create the return list from the set, thus eliminating duplicates.
		RuleServiceResult ret = new BasicRuleServiceResult(
				new LinkedList<RuleHitDetail>(resultMap.values()),
				result.getExecutionStatistics());
		return ret;
	}
	
	private static void processPassengerFlight(RuleHitDetail rhd, Long flightId, Map<RuleHitDetail, RuleHitDetail> resultMap){
		
		rhd.setFlightId(flightId);

		// set the passenger object to null
		// since its only purpose was to provide flight
		// details.
		rhd.setPassenger(null);
		RuleHitDetail resrhd = resultMap.get(rhd);
		if(resrhd != null && resrhd.getRuleId() != rhd.getRuleId()){
			resrhd.incrementHitCount();
			if(resrhd.getUdrRuleId() != null){
				//this is a rule hit
				resrhd.incrementRuleHitCount();
			} else {
				//this is a watch list hit
				if(resrhd.getHitType() != rhd.getHitType()){
					resrhd.setHitType(HitTypeEnum.PD.toString());
				}
			}
		} else if (resrhd == null){
		    resultMap.put(rhd, rhd);
		}

	}
}
