package gov.gtas.svc.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import gov.gtas.bo.BasicRuleServiceResult;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Message;
import gov.gtas.model.Pnr;
import gov.gtas.svc.request.builder.PassengerFlightTuple;
import gov.gtas.svc.request.builder.RuleEngineRequestBuilder;

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
	public static RuleServiceRequest createPnrRequest(final Pnr req) {
		Collection<Pnr> pnrs = new LinkedList<Pnr>();
		pnrs.add(req);
		return createPnrApisRequest(null, pnrs);
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
	public static RuleServiceRequest createApisRequest(
			final List<ApisMessage> reqList) {
		return createPnrApisRequest(reqList, null);
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
	public static RuleServiceRequest createPnrRequest(
			final List<Pnr> reqList) {
		return createPnrApisRequest(null, reqList);
	}

	private static Set<PassengerFlightTuple> paxFlightTuples;

	/**
	 * Creates a Rule Engine request containing data from a collection of APIS
	 * and PNR messages.
	 * 
	 * @param apisMessages
	 * @param pnrs
	 * @return the rule engine request object.
	 */
	public static RuleServiceRequest createPnrApisRequest(
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
		setPaxFlightTuples(bldr.getPassengerFlightSet());
		return bldr.build();
	}

	/**
	 * Creates a Rule Engine request containing data from a List of Messages.
	 * 
	 * @param loadedMessages
	 *            List of Messages
	 * @return the rule engine request object.
	 */
	public static RuleServiceRequest createPnrApisRequest(
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
		setPaxFlightTuples(bldr.getPassengerFlightSet());

		return bldr.build();
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

		// create a set to eliminate duplicates
		Set<RuleHitDetail> resultSet = new HashSet<RuleHitDetail>();

		for (RuleHitDetail rhd : resultList) {
			RuleHitDetail hitDetail = rhd;
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
							newrhd.setFlightId(flight.getId());

							// set the passenger object to null
							// since its only purpose was to provide flight
							// details.
							newrhd.setPassenger(null);

							resultSet.add(newrhd);

							// set the original RuleHitDetail reference to null
							// so that it it will not get inserted into the
							// resultset.
							hitDetail = null;
						}
					} catch (CloneNotSupportedException cnse) {
						cnse.printStackTrace();
					}
				}
			}
			// Check that the RuleHitDetail was not already cloned and inserted
			// (see line 126 above)
			if (hitDetail != null) {
				// set the passenger object to null
				// since its only purpose was to provide flight details.
				hitDetail.setPassenger(null);

				resultSet.add(hitDetail);
			}
		}
		// Now create the return list from the set, thus eliminating duplicates.
		RuleServiceResult ret = new BasicRuleServiceResult(
				new LinkedList<RuleHitDetail>(resultSet),
				result.getExecutionStatistics());
		return ret;
	}

	public static Set<PassengerFlightTuple> getPaxFlightTuples() {
		return paxFlightTuples;
	}

	public static void setPaxFlightTuples(
			Set<PassengerFlightTuple> paxFlightTuples) {
		TargetingServiceUtils.paxFlightTuples = paxFlightTuples;
	}
}
