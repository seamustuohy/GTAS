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
import gov.gtas.querybuilder.model.QueryFlightResult;
import gov.gtas.querybuilder.model.QueryPassengerResult;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.QueryResponse;
import gov.gtas.querybuilder.model.QueryResult;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.service.QueryBuilderService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		
		List<IQueryResult> flights = queryService.runFlightQuery(queryObject);
		response = createQueryResponse(Status.SUCCESS, flights != null ? flights.size() + " record(s)" : "flight is null" , flights);
		
		return response;
	}
	
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.POST)
	public IQueryResponse runPassengerQuery(@RequestBody QueryObject queryObject) throws InvalidQueryException {
		IQueryResponse response = new QueryResponse();
		
		List<IQueryResult> passengers = queryService.runPassengerQuery(queryObject);
		response = createQueryResponse(Status.SUCCESS, passengers != null ? passengers.size() + " record(s)" : "query result is null", passengers);

		return response;
	}
	
	@RequestMapping(value = Constants.SAVE_QUERY_URI, method = RequestMethod.POST)
	public IQueryResponse saveQuery(@RequestBody QueryRequest queryRequest) throws InvalidQueryException, QueryAlreadyExistsException {
		IQueryResponse response = new QueryResponse();
		List<IQueryResult> resultList = new ArrayList<>();

		resultList.add(queryService.saveQuery(queryRequest));
		
		response = createQueryResponse(Status.SUCCESS, Constants.QUERY_SAVED_SUCCESS_MSG, resultList);
		
		return response;
	}

	@RequestMapping(value = Constants.EDIT_QUERY_URI, method = RequestMethod.PUT)
	public IQueryResponse editQuery(@RequestBody QueryRequest queryRequest) throws InvalidQueryException, QueryAlreadyExistsException, QueryDoesNotExistException  {
		IQueryResponse response = new QueryResponse();
		List<IQueryResult> resultList = new ArrayList<>();
			
		resultList.add(queryService.editQuery(queryRequest));
		
		response = createQueryResponse(Status.SUCCESS, Constants.QUERY_EDITED_SUCCESS_MSG, resultList);
		
		return response;
	}
	
	@RequestMapping(value = Constants.LIST_QUERY_URI, method = RequestMethod.GET)
	public IQueryResponse listQueryByUser(@RequestParam("userId") String userId) throws InvalidQueryException {
		IQueryResponse response = new QueryResponse();
		
		if(userId != null) {
			List<IQueryResult> resultList = new ArrayList<>();
			
			resultList = queryService.listQueryByUser(userId);
			
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
		
		qbMap.put(EntityEnum.ADDRESS.getEntityName(), getMapping(EntityEnum.ADDRESS));
		qbMap.put(EntityEnum.CREDIT_CARD.getEntityName(), getMapping(EntityEnum.CREDIT_CARD));
		qbMap.put(EntityEnum.DOCUMENT.getEntityName(), getMapping(EntityEnum.DOCUMENT));
		qbMap.put(EntityEnum.EMAIL.getEntityName(), getMapping(EntityEnum.EMAIL));
		qbMap.put(EntityEnum.FLIGHT.getEntityName(), getMapping(EntityEnum.FLIGHT));
		qbMap.put(EntityEnum.FREQUENT_FLYER.getEntityName(), getMapping(EntityEnum.FREQUENT_FLYER));
		qbMap.put(EntityEnum.HITS.getEntityName(), getMapping(EntityEnum.HITS));
		qbMap.put(EntityEnum.PASSENGER.getEntityName(), getMapping(EntityEnum.PASSENGER));
		qbMap.put(EntityEnum.PHONE.getEntityName(), getMapping(EntityEnum.PHONE));
		qbMap.put(EntityEnum.PNR.getEntityName(), getMapping(EntityEnum.PNR));
		qbMap.put(EntityEnum.TRAVEL_AGENCY.getEntityName(), getMapping(EntityEnum.TRAVEL_AGENCY));
		
		return qbMap;
	}
	
	private QueryBuilderMapping getMapping(EntityEnum entityType) {
		QueryBuilderMappingFactory factory = new QueryBuilderMappingFactory();
		
		QueryBuilderMapping model = factory.getQueryBuilderMapping(entityType);
		
		return model;
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
