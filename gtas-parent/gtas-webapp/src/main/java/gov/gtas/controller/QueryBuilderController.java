package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.model.BaseEntity;
import gov.gtas.model.Crew;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passport;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.User;
import gov.gtas.model.Visa;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.parsers.paxlst.usedifact.PDT.PersonStatus;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.model.Query;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.querybuilder.util.EntityEnum;
import gov.gtas.web.querybuilder.model.IQueryBuilderModel;
import gov.gtas.web.querybuilder.model.QueryBuilderFlightResult;
import gov.gtas.web.querybuilder.model.QueryBuilderModelFactory;
import gov.gtas.web.querybuilder.model.QueryBuilderPassengerResult;
import gov.gtas.web.querybuilder.model.QueryRequest;
import gov.gtas.web.querybuilder.model.QueryResponse;
import gov.gtas.web.querybuilder.model.QueryResult;
import gov.gtas.web.querybuilder.model.Status;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	@RequestMapping(value = Constants.INIT, method = RequestMethod.GET)
	public Map<String, IQueryBuilderModel> initQueryBuilder() {
		
		logger.debug("Getting query builder model");
		return getQueryBuilderModel();
	}
	
	/**
	 * 
	 * @param queryObject
	 * @return
	 */
	@RequestMapping(value = Constants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.POST)
	public List<QueryBuilderFlightResult> runQueryOnFlight(@Valid @RequestBody QueryObject queryObject) {
		List<QueryBuilderFlightResult> qbFlights = new ArrayList<>();
		
		if(queryObject != null) {
			qbFlights = mapFlightToQueryFlightResult(queryService.runQuery(queryObject, EntityEnum.FLIGHT));
		}
		
		return qbFlights;
	}
	
	/**
	 * 
	 * @param queryObject
	 * @return
	 */
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.POST)
	public List<QueryBuilderPassengerResult> runQueryOnPassenger(@Valid @RequestBody QueryObject queryObject) {
		List<QueryBuilderPassengerResult> qbPassengers = new ArrayList<>();
		
		if(queryObject != null) {
			qbPassengers = mapFlightToQueryPassengerResult(queryService.runQuery(queryObject, EntityEnum.PAX));
		}
		
		return qbPassengers;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws IOException 
	 */
	@RequestMapping(value = Constants.SAVE_QUERY_URI, method = RequestMethod.POST)
	public QueryResponse saveQuery(@Valid @RequestBody QueryRequest query) throws IOException {
		QueryResponse response = new QueryResponse();
		
		if(query != null) {
			List<QueryResult> resultList = new ArrayList<>();
			QueryResult result = new QueryResult();
			
			try {
				result = mapQueryToQueryResult(queryService.saveQuery(createQuery(query)));
				resultList.add(result);
				
				response = createQueryResponse(Status.SUCCESS, Constants.QUERY_SAVED_SUCCESS_MSG, resultList);
				
			} catch (QueryAlreadyExistsException e) {
				result = mapQueryToQueryResult(createQuery(query));
				resultList.add(result);
				
				response = createQueryResponse(Status.FAILURE, e.getMessage(), resultList);
			}
		}
		
		return response;
	}

	/**
	 * 
	 * @param query
	 * @return
	 * @throws IOException
	 * @throws QueryAlreadyExistsException
	 */
	@RequestMapping(value = Constants.EDIT_QUERY_URI, method = RequestMethod.PUT)
	public QueryResponse editQuery(@Valid @RequestBody QueryRequest query) throws IOException {
		QueryResponse response = new QueryResponse();
		
		if(query != null) {
			List<QueryResult> resultList = new ArrayList<>();
			QueryResult result = new QueryResult();
			
			try {
				result = mapQueryToQueryResult(queryService.editQuery(createQuery(query)));
				resultList.add(result);
				
				response = createQueryResponse(Status.SUCCESS, Constants.QUERY_EDITED_SUCCESS_MSG, resultList);
				
			} catch (QueryAlreadyExistsException e) {
				result = mapQueryToQueryResult(createQuery(query));
				resultList.add(result);
				
				response = createQueryResponse(Status.FAILURE, e.getMessage(), resultList);
			}
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
	public QueryResponse listQueryByUser(@RequestParam("userId") String userId) throws JsonParseException, JsonMappingException, IOException {
		QueryResponse response = new QueryResponse();
		
		if(userId != null) {
			List<QueryResult> resultList = new ArrayList<>();
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
	public QueryResponse deleteQuery(@RequestParam("userId") String userId, @RequestParam("id") int id) {
		QueryResponse response = new QueryResponse();
		
		if(userId != null && id > 0) {
			queryService.deleteQuery(userId, id);
			response = createQueryResponse(Status.SUCCESS, Constants.QUERY_DELETED_SUCCESS_MSG, null);
		}
		
		return response;
	}
	
	private Query createQuery(QueryRequest req) throws JsonProcessingException {
		Query query = new Query();
		ObjectMapper mapper = new ObjectMapper();
		
		if(req != null) {
			User user = new User();
			user.setUserId(req.getUserId());
			
			query.setId(req.getId());
			query.setCreatedBy(user);
			query.setTitle(req.getTitle());
			query.setDescription(req.getDescription());
			query.setQueryText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req.getQuery()));
			
		}
		
		return query;
	}
	
	private QueryResponse createQueryResponse(Status status, String message, List<QueryResult> resultList) {
		QueryResponse response = new QueryResponse();
		
		response.setStatus(status);
		response.setMessage(message);
		response.setResult(resultList);
		
		return response;
	}
	
	/**
	 * 
	 * @return
	 */
	private Map<String, IQueryBuilderModel> getQueryBuilderModel() {
		Map<String, IQueryBuilderModel> modelMap = new LinkedHashMap<>();
		
		modelMap.put(EntityEnum.ADDRESS.getFriendlyName(), getModel(EntityEnum.ADDRESS));
		modelMap.put(EntityEnum.API.getFriendlyName(), getModel(EntityEnum.API));
		modelMap.put(EntityEnum.CREDIT_CARD.getFriendlyName(), getModel(EntityEnum.CREDIT_CARD));
		modelMap.put(EntityEnum.DOCUMENT.getFriendlyName(), getModel(EntityEnum.DOCUMENT));
		modelMap.put(EntityEnum.EMAIL.getFriendlyName(), getModel(EntityEnum.EMAIL));
		modelMap.put(EntityEnum.FLIGHT.getFriendlyName(), getModel(EntityEnum.FLIGHT));
		modelMap.put(EntityEnum.FREQUENT_FLYER.getFriendlyName(), getModel(EntityEnum.FREQUENT_FLYER));
		modelMap.put(EntityEnum.HITS.getFriendlyName(), getModel(EntityEnum.HITS));
		modelMap.put(EntityEnum.NAME_ORIGIN.getFriendlyName(), getModel(EntityEnum.NAME_ORIGIN));
		modelMap.put(EntityEnum.PAX.getFriendlyName(), getModel(EntityEnum.PAX));
		modelMap.put(EntityEnum.PHONE.getFriendlyName(), getModel(EntityEnum.PHONE));
		modelMap.put(EntityEnum.PNR.getFriendlyName(), getModel(EntityEnum.PNR));
		modelMap.put(EntityEnum.TRAVEL_AGENCY.getFriendlyName(), getModel(EntityEnum.TRAVEL_AGENCY));
		
		return modelMap;
	}
	
	/**
	 * 
	 * @param modelType
	 * @return
	 */
	private IQueryBuilderModel getModel(EntityEnum modelType) {
		QueryBuilderModelFactory factory = new QueryBuilderModelFactory();
		
		IQueryBuilderModel model = factory.getQueryBuilderModel(modelType);
		
		return model;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private List<QueryBuilderFlightResult> mapFlightToQueryFlightResult(List<? extends BaseEntity> list) {
		List<QueryBuilderFlightResult> qbFlights = new ArrayList<>();
		
		if(list != null && list.size() > 0) {
			List<Flight> flights = (List<Flight>) list;
			for(Flight flight : flights) {
				if(flight != null) {
					QueryBuilderFlightResult qbFlight = new QueryBuilderFlightResult();
					
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
	private List<QueryBuilderPassengerResult> mapFlightToQueryPassengerResult(List<? extends BaseEntity> list) {
		List<QueryBuilderPassengerResult> qbPassengers = new ArrayList<>();
		SimpleDateFormat dobFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		if(list != null && list.size() > 0) {
			List<Traveler> travelers = (List<Traveler>) list;
			for(Traveler traveler : travelers) {
				if(traveler != null) {
					QueryBuilderPassengerResult qbPassenger = new QueryBuilderPassengerResult();
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
					String status = "Not available";
					
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
					qbPassenger.setStatus(status);
					
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
	private List<QueryResult> mapQueryListToResultList(List<Query> queryList) throws JsonParseException, JsonMappingException, IOException {
		List<QueryResult> resultList = new ArrayList<>();
		
		if(queryList != null && queryList.size() > 0) {
			for(Query query : queryList) {
				resultList.add(mapQueryToQueryResult(query));
			}
		}
		
		return resultList;
	}
		
	@ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public QueryResponse handleMethodArgumentException(MethodArgumentNotValidException exception) {
		QueryResponse response = new QueryResponse();
		
		response = createQueryResponse(Status.FAILURE, Constants.QUERY_SERVICE_ERROR_MSG, null);
		
		return response;
    }

	/**
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public QueryResponse handleExceptions(Exception e) {
		QueryResponse response = new QueryResponse();
		
		response = createQueryResponse(Status.FAILURE, Constants.QUERY_SERVICE_ERROR_MSG, null);
		
		logger.error(e.getStackTrace().toString());
		
	    return response;
	}
}
