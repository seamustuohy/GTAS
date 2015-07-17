package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.model.Crew;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passport;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.Visa;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.parsers.paxlst.usedifact.PDT.PersonStatus;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.enums.Status;
import gov.gtas.querybuilder.exceptions.InvalidQueryObjectException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRequestException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.mappings.QueryBuilderMapping;
import gov.gtas.querybuilder.mappings.QueryBuilderMappingFactory;
import gov.gtas.querybuilder.model.IQueryResponse;
import gov.gtas.querybuilder.model.IQueryResult;
import gov.gtas.querybuilder.model.Query;
import gov.gtas.querybuilder.model.QueryErrorResponse;
import gov.gtas.querybuilder.model.QueryFlightsResult;
import gov.gtas.querybuilder.model.QueryPassengersResult;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.QueryResponse;
import gov.gtas.querybuilder.model.QueryResult;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.querybuilder.validation.QueryObjectValidator;
import gov.gtas.querybuilder.validation.QueryRequestValidator;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	
	@Autowired
	QueryBuilderService queryService;

	@InitBinder(Constants.QUERYOBJECT_OBJECTNAME)
	protected void initQueryObjectBinder(WebDataBinder binder) {
	    binder.setValidator(new QueryObjectValidator());
	}
	
	@InitBinder(Constants.QUERYREQUEST_OBJECTNAME)
	protected void initQueryRequestBinder(WebDataBinder binder) {
	    binder.setValidator(new QueryRequestValidator());
	}
	
	@RequestMapping(value = Constants.INIT, method = RequestMethod.GET)
	public Map<String, QueryBuilderMapping> initQueryBuilder() {
		
		logger.debug("Getting query builder model");
		return getQueryBuilderMapping();
	}
	
	/**
	 * 
	 * @param queryObject
	 * @return
	 * @throws InvalidQueryObjectException 
	 */
	@RequestMapping(value = Constants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.POST)
	public IQueryResponse runFlightQuery(@Valid @RequestBody QueryObject queryObject) throws InvalidQueryObjectException {
		IQueryResponse response = new QueryResponse();
		
		if(queryObject != null) {
			response = createQueryResponse(Status.SUCCESS, "", mapToQueryFlightResult(queryService.runFlightQuery(queryObject, EntityEnum.FLIGHT)));
		}
		
		return response;
	}
	
	/**
	 * 
	 * @param queryObject
	 * @return
	 * @throws InvalidQueryObjectException 
	 */
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.POST)
	public IQueryResponse runPassengerQuery(@Valid @RequestBody QueryObject queryObject) throws InvalidQueryObjectException {
		IQueryResponse response = new QueryResponse();
		
		if(queryObject != null) {
			response = createQueryResponse(Status.SUCCESS, "", mapToQueryPassengerResult(queryService.runPassengerQuery(queryObject, EntityEnum.PAX)));
		}
		
		return response;
	}
	
	/**
	 * 
	 * @param queryRequest
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws IOException
	 * @throws InvalidQueryObjectException
	 * @throws InvalidQueryRequestException 
	 */
	@RequestMapping(value = Constants.SAVE_QUERY_URI, method = RequestMethod.POST)
	public IQueryResponse saveQuery(@Valid @RequestBody QueryRequest queryRequest) throws QueryAlreadyExistsException, IOException, InvalidQueryRequestException {
		IQueryResponse response = new QueryResponse();
		
		if(queryRequest != null) {
			List<IQueryResult> resultList = new ArrayList<>();

			resultList.add(mapQueryToQueryResult(queryService.saveQuery(queryRequest)));
			
			response = createQueryResponse(Status.SUCCESS, Constants.QUERY_SAVED_SUCCESS_MSG, resultList);
		}
		
		return response;
	}

	/**
	 * 
	 * @param queryRequest
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws IOException
	 * @throws InvalidQueryRequestException
	 */
	@RequestMapping(value = Constants.EDIT_QUERY_URI, method = RequestMethod.PUT)
	public IQueryResponse editQuery(@Valid @RequestBody QueryRequest queryRequest) throws QueryAlreadyExistsException, IOException, InvalidQueryRequestException  {
		IQueryResponse response = new QueryResponse();
		
		if(queryRequest != null) {
			List<IQueryResult> resultList = new ArrayList<>();
			
			resultList.add(mapQueryToQueryResult(queryService.editQuery(queryRequest)));
			
			response = createQueryResponse(Status.SUCCESS, Constants.QUERY_EDITED_SUCCESS_MSG, resultList);
		}
		
		return response;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = Constants.LIST_QUERY_URI, method = RequestMethod.GET)
	public IQueryResponse listQueryByUser(@RequestParam("userId") String userId) throws JsonParseException, JsonMappingException, IOException {
		IQueryResponse response = new QueryResponse();
		
		if(userId != null) {
			List<IQueryResult> resultList = new ArrayList<>();
			
			resultList = mapQueryListToResultList(queryService.listQueryByUser(userId));
			
			response = createQueryResponse(Status.SUCCESS, "", resultList);
		}
		
		return response;
	}
	
	/**
	 * 
	 * @param userId
	 * @param id
	 */
	@RequestMapping(value = Constants.DELETE_QUERY_URI, method = RequestMethod.DELETE)
	public IQueryResponse deleteQuery(@RequestParam("userId") String userId, @RequestParam("id") int id) {
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
	
	private IQueryResponse createQueryErrorResponse(Status status, String message, String request) {
		QueryErrorResponse response = new QueryErrorResponse();
		
		response.setStatus(status);
		response.setMessage(message);
		response.setRequest(request);
		
		return response;
	}
	
	/**
	 * 
	 * @return
	 */
	private Map<String, QueryBuilderMapping> getQueryBuilderMapping() {
		Map<String, QueryBuilderMapping> qbMap = new LinkedHashMap<>();
		
		qbMap.put(EntityEnum.ADDRESS.toString(), getMapping(EntityEnum.ADDRESS));
		qbMap.put(EntityEnum.API.toString(), getMapping(EntityEnum.API));
		qbMap.put(EntityEnum.CREDIT_CARD.toString(), getMapping(EntityEnum.CREDIT_CARD));
		qbMap.put(EntityEnum.DOCUMENT.toString(), getMapping(EntityEnum.DOCUMENT));
		qbMap.put(EntityEnum.EMAIL.toString(), getMapping(EntityEnum.EMAIL));
		qbMap.put(EntityEnum.FLIGHT.toString(), getMapping(EntityEnum.FLIGHT));
		qbMap.put(EntityEnum.FREQUENT_FLYER.toString(), getMapping(EntityEnum.FREQUENT_FLYER));
		qbMap.put(EntityEnum.HITS.toString(), getMapping(EntityEnum.HITS));
		qbMap.put(EntityEnum.NAME_ORIGIN.toString(), getMapping(EntityEnum.NAME_ORIGIN));
		qbMap.put(EntityEnum.PAX.toString(), getMapping(EntityEnum.PAX));
		qbMap.put(EntityEnum.PHONE.toString(), getMapping(EntityEnum.PHONE));
		qbMap.put(EntityEnum.PNR.toString(), getMapping(EntityEnum.PNR));
		qbMap.put(EntityEnum.TRAVEL_AGENCY.toString(), getMapping(EntityEnum.TRAVEL_AGENCY));
		
		return qbMap;
	}
	
	/**
	 * 
	 * @param modelType
	 * @return
	 */
	private QueryBuilderMapping getMapping(EntityEnum entityType) {
		QueryBuilderMappingFactory factory = new QueryBuilderMappingFactory();
		
		QueryBuilderMapping model = factory.getQueryBuilderMapping(entityType);
		
		return model;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private List<IQueryResult> mapToQueryFlightResult(List<Flight> flights) {
		List<IQueryResult> qbFlights = new ArrayList<>();
		
		if(flights != null && flights.size() > 0) {
			for(Flight flight : flights) {
				if(flight != null) {
					QueryFlightsResult qbFlight = new QueryFlightsResult();
					
					qbFlight.setId(flight.getId());
					qbFlight.setFlightNumber(flight.getFlightNumber());
					qbFlight.setCarrierCode(flight.getCarrier() != null ? flight.getCarrier().getIata() : "");
					qbFlight.setOrigin(flight.getOrigin() != null ? flight.getOrigin().getIata() : "");
					qbFlight.setOriginCountry(flight.getOriginCountry() != null ? flight.getOriginCountry().getIso2() : "");
					qbFlight.setDepartureDt(dtFormat.format(flight.getEtd()));
					qbFlight.setDestination(flight.getDestination() != null ? flight.getDestination().getIata() : "");
					qbFlight.setDestinationCountry(flight.getDestinationCountry() != null ? flight.getDestinationCountry().getIso2() : "");
					qbFlight.setArrivalDt(dtFormat.format(flight.getEta()));
					
					qbFlights.add(qbFlight);
				}
			}
		}
		
		return qbFlights;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private List<IQueryResult> mapToQueryPassengerResult(List<Traveler> travelers) {
		List<IQueryResult> qbPassengers = new ArrayList<>();
		SimpleDateFormat dobFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		if(travelers != null && travelers.size() > 0) {
			for(Traveler traveler : travelers) {
				if(traveler != null) {
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
					String seatNumber = "Not available";
					
					qbPassenger.setId(traveler.getId());
					qbPassenger.setOnSomethingList(true);
					qbPassenger.setOnWatchList(true);
					qbPassenger.setFirstName(traveler.getFirstName());
					qbPassenger.setLastName(traveler.getLastName());
					// Passenger type
					if(traveler instanceof Pax) {
						qbPassenger.setPassengerType(PersonStatus.PAX.toString());
					}
					else if(traveler instanceof Crew) {
						qbPassenger.setPassengerType(PersonStatus.CREW.toString());
					}
					
					qbPassenger.setGender(traveler.getGender() != null ? traveler.getGender().toString() : "");
					qbPassenger.setDob(dobFormat.format(traveler.getDob()));
					qbPassenger.setCitizenship(traveler.getCitizenshipCountry() != null ? traveler.getCitizenshipCountry().getIso2() : "");
					
					// Document information
					Set<Document> docs = traveler.getDocuments();
					if(docs != null) {
						if(docs.iterator().hasNext()) {
							Document doc = docs.iterator().next();
							
							docNumber = doc.getDocumentNumber();
							
							if(doc instanceof Passport) {
								docType = "P";
							}
							else if(doc instanceof Visa) {
								docType = "V";
							}
							docIssuanceCountry = doc.getIssuanceCountry().getIso2();
						}
					}
					qbPassenger.setDocumentNumber(docNumber);
					qbPassenger.setDocumentType(docType);
					qbPassenger.setDocumentIssuanceContry(docIssuanceCountry);
					
					// flight information
					Set<Flight> flights = traveler.getFlights();
					if(flights != null) {
						if(flights.iterator().hasNext()) {
							Flight flight = flights.iterator().next();
							
							carrierCode = flight.getCarrier() != null ? flight.getCarrier().getIata() : "";
							flightNumber = flight.getFlightNumber();
							origin = flight.getOrigin() != null ? flight.getOrigin().getIata() : "";
							destination  = flight.getDestination() != null ? flight.getDestination().getIata() : "";
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
					qbPassenger.setSeatNumber(seatNumber);
					
					qbPassengers.add(qbPassenger);
				}
			}
		}
		
		return qbPassengers;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private QueryResult mapQueryToQueryResult(Query query) throws JsonParseException, JsonMappingException, IOException {
		QueryResult result = new QueryResult();
		ObjectMapper mapper = new ObjectMapper();
		
		result.setId(query.getId());
		result.setTitle(query.getTitle());
		result.setDescription(query.getDescription());
		result.setQuery(mapper.readValue(query.getQueryText(), QueryObject.class));
		
		return result;
	}
	
	/**
	 * 
	 * @param queryList
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private List<IQueryResult> mapQueryListToResultList(List<Query> queryList) throws JsonParseException, JsonMappingException, IOException {
		List<IQueryResult> resultList = new ArrayList<>();
		
		if(queryList != null && queryList.size() > 0) {
			for(Query query : queryList) {
				resultList.add(mapQueryToQueryResult(query));
			}
		}
		
		return resultList;
	}
		
	@ExceptionHandler(QueryAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public IQueryResponse handleQueryExistsException(QueryAlreadyExistsException exception) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		return createQueryErrorResponse(Status.FAILURE, exception.getMessage(), mapper.writeValueAsString(exception.getQueryRequest()));
    }

	@ExceptionHandler(InvalidQueryRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public IQueryResponse handleInvalidQueryRequestException(InvalidQueryRequestException exception) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		return createQueryErrorResponse(Status.FAILURE, exception.getMessage(), mapper.writeValueAsString(exception.getRequest()));
    }
	
	@ExceptionHandler(InvalidQueryObjectException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public IQueryResponse handleInvalidQueryObjectException(InvalidQueryObjectException exception) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		return createQueryErrorResponse(Status.FAILURE, exception.getMessage(), mapper.writeValueAsString(exception.getQueryObject()));
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public IQueryResponse handleMethodArgumentException(MethodArgumentNotValidException exception) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		return createQueryErrorResponse(Status.FAILURE, QueryValidationUtils.getErrorString(exception.getBindingResult()), 
				mapper.writeValueAsString(exception.getBindingResult().getTarget()));
    }

	/**
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public IQueryResponse handleExceptions(Exception e) {
		
		e.printStackTrace();
		
		return createQueryErrorResponse(Status.FAILURE, Constants.QUERY_SERVICE_ERROR_MSG, null);
	}
}
