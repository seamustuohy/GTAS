package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.Status;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.mappings.QueryBuilderMapping;
import gov.gtas.querybuilder.mappings.QueryBuilderMappingFactory;
import gov.gtas.querybuilder.model.IQueryResponse;
import gov.gtas.querybuilder.model.IQueryResult;
import gov.gtas.querybuilder.model.QueryErrorResponse;
import gov.gtas.querybuilder.model.QueryFlightsResult;
import gov.gtas.querybuilder.model.QueryPassengersResult;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.QueryResponse;
import gov.gtas.querybuilder.model.QueryResult;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.service.QueryBuilderService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author GTAS5
 *
 */

@RestController
@RequestMapping(Constants.QUERY_SERVICE)
public class QueryBuilderController {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilderController.class);
	private SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	
	@Autowired
	QueryBuilderService queryService;

	@RequestMapping(value = Constants.INIT, method = RequestMethod.GET)
	public Map<String, QueryBuilderMapping> initQueryBuilder() {
		
		logger.debug("Getting query builder model");
		return getQueryBuilderMapping();
	}
	
	@RequestMapping(value = Constants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.POST)
	public IQueryResponse runFlightQuery(@RequestBody QueryObject queryObject) throws InvalidQueryException {
		IQueryResponse response = new QueryResponse();
		
		List<Flight> flights = queryService.runFlightQuery(queryObject);
		response = createQueryResponse(Status.SUCCESS, flights != null ? flights.size() + " record(s)" : "flight is null" , mapToQueryFlightResult(flights));
		
		return response;
	}
	
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.POST)
	public IQueryResponse runPassengerQuery(@RequestBody QueryObject queryObject) throws InvalidQueryException {
		IQueryResponse response = new QueryResponse();
		
		List<Passenger> passengers = queryService.runPassengerQuery(queryObject);
		response = createQueryResponse(Status.SUCCESS, passengers != null ? passengers.size() + " record(s)" : "passenger is null", mapToQueryPassengerResult(passengers));
		logger.info("returning response");
		return response;
	}
	
	@RequestMapping(value = Constants.SAVE_QUERY_URI, method = RequestMethod.POST)
	public IQueryResponse saveQuery(@RequestBody QueryRequest queryRequest) throws InvalidQueryException, QueryAlreadyExistsException {
		IQueryResponse response = new QueryResponse();
		List<IQueryResult> resultList = new ArrayList<>();

		resultList.add(mapQueryToQueryResult(queryService.saveQuery(queryRequest)));
		
		response = createQueryResponse(Status.SUCCESS, Constants.QUERY_SAVED_SUCCESS_MSG, resultList);
		
		return response;
	}

	@RequestMapping(value = Constants.EDIT_QUERY_URI, method = RequestMethod.PUT)
	public IQueryResponse editQuery(@RequestBody QueryRequest queryRequest) throws InvalidQueryException, QueryAlreadyExistsException, QueryDoesNotExistException  {
		IQueryResponse response = new QueryResponse();
		List<IQueryResult> resultList = new ArrayList<>();
			
		resultList.add(mapQueryToQueryResult(queryService.editQuery(queryRequest)));
		
		response = createQueryResponse(Status.SUCCESS, Constants.QUERY_EDITED_SUCCESS_MSG, resultList);
		
		return response;
	}
	
	@RequestMapping(value = Constants.LIST_QUERY_URI, method = RequestMethod.GET)
	public IQueryResponse listQueryByUser(@RequestParam("userId") String userId) throws InvalidQueryException {
		IQueryResponse response = new QueryResponse();
		
		if(userId != null) {
			List<IQueryResult> resultList = new ArrayList<>();
			
			resultList = mapQueryListToResultList(queryService.listQueryByUser(userId));
			
			response = createQueryResponse(Status.SUCCESS, resultList != null ? resultList.size() + " record(s)" : "resultList is null", resultList);
		}
		
		return response;
	}
	
	@RequestMapping(value = Constants.DELETE_QUERY_URI, method = RequestMethod.DELETE)
	public IQueryResponse deleteQuery(@RequestParam("userId") String userId, @RequestParam("id") int id) throws QueryDoesNotExistException {
		IQueryResponse response = new QueryResponse();
		
		if(userId != null && id > 0) {
			queryService.deleteQuery(userId, id);
			response = createQueryResponse(Status.SUCCESS, Constants.QUERY_DELETED_SUCCESS_MSG, null);
		}
		
		return response;
	}
	
	private IQueryResponse createQueryResponse(Status status, String message, List<IQueryResult> resultList) {
		QueryResponse response = new QueryResponse();
		
		response.setStatus(status);
		response.setMessage(message);
		response.setResult(resultList);
		
		return response;
	}
	
	private IQueryResponse createQueryErrorResponse(Status status, String message, Object request) {
		QueryErrorResponse response = new QueryErrorResponse();
		
		response.setStatus(status);
		response.setMessage(message);
		response.setRequest(request);
		
		return response;
	}
	
	private Map<String, QueryBuilderMapping> getQueryBuilderMapping() {
		Map<String, QueryBuilderMapping> qbMap = new LinkedHashMap<>();
		
		qbMap.put(EntityEnum.ADDRESS.toString(), getMapping(EntityEnum.ADDRESS));
		qbMap.put(EntityEnum.CREDIT_CARD.toString(), getMapping(EntityEnum.CREDIT_CARD));
		qbMap.put(EntityEnum.DOCUMENT.toString(), getMapping(EntityEnum.DOCUMENT));
		qbMap.put(EntityEnum.EMAIL.toString(), getMapping(EntityEnum.EMAIL));
		qbMap.put(EntityEnum.FLIGHT.toString(), getMapping(EntityEnum.FLIGHT));
		qbMap.put(EntityEnum.FREQUENT_FLYER.toString(), getMapping(EntityEnum.FREQUENT_FLYER));
		qbMap.put(EntityEnum.HITS.toString(), getMapping(EntityEnum.HITS));
		qbMap.put(EntityEnum.PASSENGER.toString(), getMapping(EntityEnum.PASSENGER));
		qbMap.put(EntityEnum.PHONE.toString(), getMapping(EntityEnum.PHONE));
		qbMap.put(EntityEnum.PNR.toString(), getMapping(EntityEnum.PNR));
		qbMap.put(EntityEnum.TRAVEL_AGENCY.toString(), getMapping(EntityEnum.TRAVEL_AGENCY));
		
		return qbMap;
	}
	
	private QueryBuilderMapping getMapping(EntityEnum entityType) {
		QueryBuilderMappingFactory factory = new QueryBuilderMappingFactory();
		
		QueryBuilderMapping model = factory.getQueryBuilderMapping(entityType);
		
		return model;
	}
	
	private List<IQueryResult> mapToQueryFlightResult(List<Flight> flights) {
		List<IQueryResult> qbFlights = new ArrayList<>();
		
		if(flights != null && flights.size() > 0) {
			for(Flight flight : flights) {
				if(flight != null) {
					QueryFlightsResult qbFlight = new QueryFlightsResult();
					
					qbFlight.setId(flight.getId());
					qbFlight.setFlightNumber(flight.getFlightNumber());
					qbFlight.setCarrierCode(flight.getCarrier() != null ? flight.getCarrier() : "");
					qbFlight.setOrigin(flight.getOrigin() != null ? flight.getOrigin() : "");
					qbFlight.setOriginCountry(flight.getOriginCountry() != null ? flight.getOriginCountry() : "");
					qbFlight.setDepartureDt(dtFormat.format(flight.getEtd()));
					qbFlight.setDestination(flight.getDestination() != null ? flight.getDestination() : "");
					qbFlight.setDestinationCountry(flight.getDestinationCountry() != null ? flight.getDestinationCountry() : "");
					qbFlight.setArrivalDt(dtFormat.format(flight.getEta()));
					
					qbFlights.add(qbFlight);
				}
			}
		}
		
		return qbFlights;
	}
	
	private List<IQueryResult> mapToQueryPassengerResult(List<Passenger> passengers) {
		List<IQueryResult> qbPassengers = new ArrayList<>();
		SimpleDateFormat dobFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		if(passengers != null && passengers.size() > 0) {
			logger.info("mapping passenger to query response object...");
			long startTime =System.nanoTime();
			for(Passenger p : passengers) {
				if(p != null) {
					QueryPassengersResult qbPassenger = new QueryPassengersResult();
					String docNumber = "";
					String docType = "";
					String docIssuanceCountry = "";
					String carrierCode = "";
					String flightNumber = "";
					String origin = "";
					String destination = "";
					String departureDt = "";
					String arrivalDt = "";
					String seat = "Not available";
					
					qbPassenger.setId(p.getId());
					qbPassenger.setRuleHit(false);
					qbPassenger.setOnWatchList(false);
					qbPassenger.setFirstName(p.getFirstName());
					qbPassenger.setLastName(p.getLastName());
					// Passenger type
                    qbPassenger.setPassengerType(p.getPassengerType());				
					qbPassenger.setGender(p.getGender() != null ? p.getGender() : "");
					qbPassenger.setDob(dobFormat.format(p.getDob()));
					qbPassenger.setCitizenship(p.getCitizenshipCountry() != null ? p.getCitizenshipCountry() : "");
					
					// Document information
					Set<Document> docs = p.getDocuments();
					if(docs != null) {
						if(docs.iterator().hasNext()) {
							Document doc = docs.iterator().next();
							
							docNumber = doc.getDocumentNumber();
							docType = doc.getDocumentType();
							docIssuanceCountry = doc.getIssuanceCountry();
						}
					}
					qbPassenger.setDocumentNumber(docNumber);
					qbPassenger.setDocumentType(docType);
					qbPassenger.setDocumentIssuanceCountry(docIssuanceCountry);
					
					// flight information
					Set<Flight> flights = p.getFlights();
					if(flights != null) {
						if(flights.iterator().hasNext()) {
							Flight flight = flights.iterator().next();
							
							carrierCode = flight.getCarrier() != null ? flight.getCarrier() : "";
							flightNumber = flight.getFlightNumber();
							origin = flight.getOrigin() != null ? flight.getOrigin() : "";
							destination  = flight.getDestination() != null ? flight.getDestination() : "";
							departureDt = dtFormat.format(flight.getEtd());
							arrivalDt = dtFormat.format(flight.getEta());
						}
					}
					qbPassenger.setCarrierCode(carrierCode);
					qbPassenger.setFlightNumber(flightNumber);
					qbPassenger.setOrigin(origin);
					qbPassenger.setDestination(destination);
					qbPassenger.setDepartureDt(departureDt);
					qbPassenger.setArrivalDt(arrivalDt);
					qbPassenger.setSeat(seat);
					
					qbPassengers.add(qbPassenger);
				}
			}
			//TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
			logger.info("done. Time elapsed: " + (System.nanoTime() - startTime));
		}
		
		return qbPassengers;
	}
	
	private QueryResult mapQueryToQueryResult(UserQuery query) throws InvalidQueryException {
		QueryResult result = new QueryResult();
		ObjectMapper mapper = new ObjectMapper();
		
		result.setId(query.getId());
		result.setTitle(query.getTitle());
		result.setDescription(query.getDescription());
		try {
			result.setQuery(mapper.readValue(query.getQueryText(), QueryObject.class));
		} catch (IOException e) {
			throw new InvalidQueryException(e.getMessage(), query);
		}
		
		return result;
	}
	
	private List<IQueryResult> mapQueryListToResultList(List<UserQuery> queryList) throws InvalidQueryException {
		List<IQueryResult> resultList = new ArrayList<>();
		
		if(queryList != null && queryList.size() > 0) {
			for(UserQuery query : queryList) {
				try {
					resultList.add(mapQueryToQueryResult(query));
				} catch (InvalidQueryException e) {
					throw new InvalidQueryException(e.getMessage(), queryList);
				}
			}
		}
		
		return resultList;
	}
		
	@ExceptionHandler({QueryAlreadyExistsException.class, QueryDoesNotExistException.class, InvalidQueryException.class,
		ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public IQueryResponse handleExceptions(Exception exception) {
		
		if(exception instanceof QueryAlreadyExistsException) {
			QueryAlreadyExistsException qae = (QueryAlreadyExistsException) exception;
		
			return createQueryErrorResponse(Status.FAILURE, qae.getMessage(), qae.getQueryRequest());
		}
		else if(exception instanceof QueryDoesNotExistException) {
			QueryDoesNotExistException qdne = (QueryDoesNotExistException) exception;
			
			return createQueryErrorResponse(Status.FAILURE, qdne.getMessage(), qdne.getQueryRequest());
		}
		else if(exception instanceof InvalidQueryException) {
			InvalidQueryException iqe = (InvalidQueryException) exception;
		
			return createQueryErrorResponse(Status.FAILURE, iqe.getMessage(), iqe.getObject());
		}
		
		return createQueryErrorResponse(Status.FAILURE, exception.getMessage(), null);
    }
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public IQueryResponse handleAllOtherExceptions(Exception e) {
		
		logger.info("An error occurred", e);
		
		return createQueryErrorResponse(Status.FAILURE, e.getMessage(), null);
	}
}
