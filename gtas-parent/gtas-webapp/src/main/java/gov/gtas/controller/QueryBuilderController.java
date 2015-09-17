package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.Status;
import gov.gtas.json.JsonServiceResponse;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryBuilderException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.mappings.QueryBuilderMapping;
import gov.gtas.querybuilder.mappings.QueryBuilderMappingFactory;
import gov.gtas.querybuilder.model.IQueryResponse;
import gov.gtas.querybuilder.model.IQueryResult;
import gov.gtas.querybuilder.model.IUserQueryResult;
import gov.gtas.querybuilder.model.QueryErrorResponse;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.QueryResponse;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.security.service.GtasSecurityUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Query Builder
 */

@RestController
@RequestMapping(Constants.QUERY_SERVICE)
public class QueryBuilderController {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilderController.class);
	
	@Autowired
	QueryBuilderService queryService;

	/**
	 * This method generates the Entity and Field mappings for the 
	 * Rule and Query UI
	 * @return
	 */
	@RequestMapping(value = Constants.INIT, method = RequestMethod.GET)
	public Map<String, QueryBuilderMapping> initQueryBuilder() {
		
		logger.debug("Getting query builder UI mappings");
		return getQueryBuilderMapping();
	}
	
	/**
	 * This method makes a call to the method in the service layer to execute the 
	 * user defined query against Flight data
	 * @param queryObject
	 * @return 
	 * @throws InvalidQueryException
	 */
	@RequestMapping(value = Constants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.POST)
	public JsonServiceResponse runFlightQuery(@RequestBody QueryObject queryObject) throws InvalidQueryException {
		
		List<IQueryResult> flights = queryService.runFlightQuery(queryObject);
		return new JsonServiceResponse(Status.SUCCESS, flights != null ? flights.size() + " record(s)" : "flight is null" , flights);
	}
	
	/**
	 * This method makes a call to the method in the service layer to execute the 
	 * user defined query against Passenger data
	 * @param queryObject
	 * @return
	 * @throws InvalidQueryException
	 */
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.POST)
	public JsonServiceResponse runPassengerQuery(@RequestBody QueryObject queryObject) throws InvalidQueryException {
		
		List<IQueryResult> passengers = queryService.runPassengerQuery(queryObject);
		return new JsonServiceResponse(Status.SUCCESS, passengers != null ? passengers.size() + " record(s)" : "query result is null", passengers);
	}
	
	/**
	 * This method makes a call to the method in the service layer to save the user defined query
	 * @param queryRequest
	 * @return
	 * @throws InvalidQueryException
	 * @throws QueryAlreadyExistsException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public JsonServiceResponse saveQuery(@RequestBody QueryRequest queryRequest) throws InvalidQueryException, QueryAlreadyExistsException {

		IUserQueryResult result = queryService.saveQuery(queryRequest);
		
		return new JsonServiceResponse(Status.SUCCESS, Constants.QUERY_SAVED_SUCCESS_MSG, result.getId());
	}

	/**
	 * This method makes a call to the method in the service layer to update a user defined query
	 * @param queryRequest
	 * @return
	 * @throws InvalidQueryException
	 * @throws QueryAlreadyExistsException
	 * @throws QueryDoesNotExistException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public JsonServiceResponse editQuery(@RequestBody QueryRequest queryRequest) throws InvalidQueryException, QueryAlreadyExistsException, QueryDoesNotExistException  {
			
		queryService.editQuery(queryRequest);
		
		return new JsonServiceResponse(Status.SUCCESS, Constants.QUERY_EDITED_SUCCESS_MSG, null);
	}
	
	/**
	 * This method makes a call to the method in the service layer to list a user's 
	 * query
	 * @return
	 * @throws InvalidQueryException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public JsonServiceResponse listQueryByUser() throws InvalidQueryException {
		List<IUserQueryResult> resultList = new ArrayList<>();
		String userId = GtasSecurityUtils.fetchLoggedInUserId();
		logger.debug("******** Received Query Builder List request by user =" + userId);
		
		resultList = queryService.listQueryByUser(userId);
		
		return new JsonServiceResponse(Status.SUCCESS, resultList != null ? resultList.size() + " record(s)" : "resultList is null", resultList);
	}
	
	/**
	 * This method makes a call to the method in the service layer to delete a user's query
	 * @param id the id of the query to be deleted
	 * @return
	 * @throws QueryDoesNotExistException
	 */
	@RequestMapping(value = Constants.DELETE_QUERY_URI, method = RequestMethod.DELETE)
	public JsonServiceResponse deleteQuery(@PathVariable int id) throws QueryDoesNotExistException {
		String userId = GtasSecurityUtils.fetchLoggedInUserId();
		logger.debug("******** Received Query Builder Delete request by user =" + userId
				+ " for " + id);
		
		queryService.deleteQuery(userId, id);
		return new JsonServiceResponse(Status.SUCCESS, Constants.QUERY_DELETED_SUCCESS_MSG, null);
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
	
	@ExceptionHandler(QueryBuilderException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public JsonServiceResponse handleExceptions(QueryBuilderException exception) {
		
		return new JsonServiceResponse(Status.FAILURE, exception.getMessage(), exception.getObject());
    }

}
