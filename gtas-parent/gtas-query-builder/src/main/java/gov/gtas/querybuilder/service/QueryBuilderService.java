package gov.gtas.querybuilder.service;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryObjectException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRequestException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryNotUniqueRepositoryException;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;
import gov.gtas.querybuilder.validation.QueryRequestValidationUtils;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author GTAS5
 *
 */
@Service
public class QueryBuilderService {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilderService.class);
	
	@Autowired
	QueryBuilderRepository queryRepository;
	
	/**
	 * 
	 * @param queryRequest
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws JsonProcessingException
	 * @throws InvalidQueryRequestException
	 */
	public UserQuery saveQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, JsonProcessingException, InvalidQueryRequestException {
		UserQuery result = null;
//		Errors errors = QueryRequestValidationUtils.validateQueryRequest(queryRequest);
//		
//		if(errors != null && errors.hasErrors()) {
//			throw new InvalidQueryRequestException(QueryValidationUtils.getErrorString(errors), queryRequest);
//		}
		
		try {
			
			result = queryRepository.saveQuery(createUserQuery(queryRequest));
		} catch(QueryNotUniqueRepositoryException ex) {
			throw new QueryAlreadyExistsException(Constants.QUERY_EXISTS_ERROR_MSG, queryRequest);
		}
		
		return result;
	}

	/**
	 * 
	 * @param queryRequest
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws JsonProcessingException
	 * @throws InvalidQueryRequestException
	 * @throws QueryDoesNotExistException
	 */
	public UserQuery editQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, JsonProcessingException, InvalidQueryRequestException, QueryDoesNotExistException {
		UserQuery result = null;
//		Errors errors = QueryRequestValidationUtils.validateQueryRequest(queryRequest);
//		
//		if(errors != null && errors.hasErrors()) {
//			throw new InvalidQueryRequestException(QueryValidationUtils.getErrorString(errors), queryRequest);
//		}
		
		try {
			
			result = queryRepository.editQuery(createUserQuery(queryRequest));
		} catch(QueryNotUniqueRepositoryException ex) {
			throw new QueryAlreadyExistsException(Constants.QUERY_EXISTS_ERROR_MSG, queryRequest);
		} catch (QueryDoesNotExistRepositoryException e) {
			throw new QueryDoesNotExistException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, queryRequest);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserQuery> listQueryByUser(String userId) {
		
		return queryRepository.listQueryByUser(userId);
	}
	
	/**
	 * 
	 * @param userId
	 * @param id
	 * @throws QueryDoesNotExistException
	 */
	public void deleteQuery(String userId, int id) throws QueryDoesNotExistException {
		
		try {
			queryRepository.deleteQuery(userId, id);
		} catch (QueryDoesNotExistRepositoryException e) {
			throw new QueryDoesNotExistException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, null);
		}
	}
		
	/**
	 * 
	 * @param queryObject
	 * @return
	 * @throws InvalidQueryObjectException
	 * @throws ParseException
	 */
	public List<Flight> runFlightQuery(QueryObject queryObject) throws InvalidQueryObjectException, ParseException {
		
		return queryRepository.getFlightsByDynamicQuery(queryObject);
	}
	
	/**
	 * 
	 * @param queryObject
	 * @return
	 * @throws InvalidQueryObjectException
	 * @throws ParseException
	 */
	public List<Traveler> runPassengerQuery(QueryObject queryObject) throws InvalidQueryObjectException, ParseException {
				
		return queryRepository.getPassengersByDynamicQuery(queryObject);
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 * @throws JsonProcessingException
	 */
	private UserQuery createUserQuery(QueryRequest req) throws JsonProcessingException {
		UserQuery query = new UserQuery();
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
	
}

