package gov.gtas.svc;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceRequestType;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.PnrMessage;
import gov.gtas.svc.request.builder.PnrRuleRequestBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
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
		} else if(requestMessage instanceof PnrMessage){
			ret = createRequest(requestMessage);
		} else {
			// arbitrary Message object
			ret = createRequest(requestMessage);
		}
		return ret;
	}
	public static RuleServiceRequest createRuleServiceRequest(
			final List<ApisMessage> requestMessages) {
				return createApisRequest(requestMessages);
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
		return createRuleServiceRequest(RuleServiceRequestType.APIS_MESSAGE, requestList);
	}
	public static RuleServiceRequest createPnrRequest(final PnrMessage req) {
		PnrRuleRequestBuilder bldr = new PnrRuleRequestBuilder();
		bldr.addPnrMessage(req);
		return bldr.build();
	}
	public static RuleServiceRequest createApisRequest(final List<ApisMessage> reqList) {
		List<Object> reqObjects = new LinkedList<Object>();
		for(ApisMessage msg:reqList){
			Set<Flight> flights = msg.getFlights();
			reqObjects.addAll(flights);
			addPassengersAndDocuments(flights, reqObjects);
		}
		return createRuleServiceRequest(RuleServiceRequestType.APIS_MESSAGE, reqObjects);
	}
	public static RuleServiceRequest createPnrRequest(final List<PnrMessage> reqList) {
		PnrRuleRequestBuilder bldr = new PnrRuleRequestBuilder();
		if(reqList != null){
			for(PnrMessage msg:reqList){
		        bldr.addPnrMessage(msg);
			}
		}
		return bldr.build();
	}
	public static RuleServiceRequest createRuleServiceRequest(final RuleServiceRequestType requestType, final List<?> reqObjects){
		return new RuleServiceRequest() {
			public List<?> getRequestObjects() {
				return reqObjects;
			}

			public RuleServiceRequestType getRequestType() {
				return requestType;
			}

		};
		
	}
    private static void addPassengersAndDocuments(Set<Flight> flights, List<Object> requestList){
    	for(Flight flight:flights){
    		Set<Passenger> passengers = flight.getPassengers();
    		requestList.addAll(passengers);
    		addDocuments(passengers, requestList);
    	}
    }

    private static void addDocuments(Set<Passenger> passengers, List<Object> requestList){
    	for(Passenger p:passengers){
    		requestList.addAll(p.getDocuments());
    	}
    }
}
