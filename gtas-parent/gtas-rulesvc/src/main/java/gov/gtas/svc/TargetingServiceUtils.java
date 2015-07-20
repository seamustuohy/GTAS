package gov.gtas.svc;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceRequestType;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TargetingServiceUtils {
	/**
	 * Create a rule engine request message from a message object from the
	 * domain model.
	 * 
	 * @param requestMessage
	 *            the message object.
	 * @return the constructed rule engine request suitable for Rule Engine
	 *         invocation.
	 */
	public static RuleServiceRequest createRuleServiceRequest(
			final gov.gtas.model.Message requestMessage) {
		RuleServiceRequest ret = null;
		if (requestMessage instanceof ApisMessage) {
			ret = createApisRequest((ApisMessage) requestMessage);
		} else {
			// arbitrary Message object
			ret = createRequest(requestMessage);
		}
		return ret;
	}

	/**
	 * Creates a request from an arbitrary object.
	 * 
	 * @param req
	 *            the input object.
	 * @return RuleServiceRequest object.
	 */
	public static RuleServiceRequest createRequest(
			final gov.gtas.model.Message req) {
		final List<gov.gtas.model.Message> requestList = new ArrayList<gov.gtas.model.Message>();
		requestList.add(req);
		return new RuleServiceRequest() {
			public List<?> getRequestObjects() {
				return requestList;
			}

			public RuleServiceRequestType getRequestType() {
				return RuleServiceRequestType.ANY_MESSAGE;
			}

		};
	}

	/**
	 * Creates a request from a API message.
	 * 
	 * @param req
	 *            the API message.
	 * @return RuleServiceRequest object.
	 */
	public static RuleServiceRequest createApisRequest(final ApisMessage req) {
		//add flights
		final List<Object> requestList = new ArrayList<Object>(req.getFlights());
		//add Passengers and documents
		addPassengersAndDocuments(req.getFlights(), requestList);
		
		return new RuleServiceRequest() {
			public List<?> getRequestObjects() {
				return requestList;
			}

			public RuleServiceRequestType getRequestType() {
				return RuleServiceRequestType.APIS_MESSAGE;
			}

		};
	}
    private static void addPassengersAndDocuments(Set<Flight> flights, List<Object> requestList){
    	for(Flight flight:flights){
    		Set<Traveler> travelers = flight.getPassengers();
    		requestList.addAll(travelers);
    		addDocuments(travelers, requestList);
    	}
    }

    private static void addDocuments(Set<Traveler> travelers, List<Object> requestList){
    	for(Traveler traveler:travelers){
    		requestList.addAll(traveler.getDocuments());
    	}
    }
}
