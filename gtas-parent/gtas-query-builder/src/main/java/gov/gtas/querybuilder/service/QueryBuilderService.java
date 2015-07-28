package gov.gtas.querybuilder.service;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public UserQuery saveQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, InvalidQueryException {
		UserQuery result = null;
		
		try {
			
			result = queryRepository.saveQuery(createUserQuery(queryRequest));
		} catch(QueryAlreadyExistsRepositoryException e) {
			throw new QueryAlreadyExistsException(e.getMessage(), queryRequest);
		} catch (InvalidQueryRepositoryException | IOException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryRequest);
		}
		
		return result;
	}

	public UserQuery editQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		UserQuery result = null;
		
		try {
			
			result = queryRepository.editQuery(createUserQuery(queryRequest));
		} catch(QueryAlreadyExistsRepositoryException e) {
			throw new QueryAlreadyExistsException(e.getMessage(), queryRequest);
		} catch (QueryDoesNotExistRepositoryException e) {
			throw new QueryDoesNotExistException(e.getMessage(), queryRequest);
		} catch (InvalidQueryRepositoryException | IOException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryRequest);
		} 
		
		return result;
	}
	
	public List<UserQuery> listQueryByUser(String userId) {
		
		return queryRepository.listQueryByUser(userId);
	}
	
	public void deleteQuery(String userId, int id) throws QueryDoesNotExistException {
		
		try {
			queryRepository.deleteQuery(userId, id);
		} catch (QueryDoesNotExistRepositoryException e) {
			throw new QueryDoesNotExistException(e.getMessage(), null);
		}
	}
		
	public List<Flight> runFlightQuery(QueryObject queryObject) throws InvalidQueryException {
		List<Flight> flights = new ArrayList<>();
		
		try {
			flights = queryRepository.getFlightsByDynamicQuery(queryObject);
		} catch (InvalidQueryRepositoryException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryObject);
		} 
		
		return flights;
	}
	
	public List<Traveler> runPassengerQuery(QueryObject queryObject) throws InvalidQueryException {
		List<Traveler> travelers = new ArrayList<>();
		
		try {
			travelers = queryRepository.getPassengersByDynamicQuery(queryObject);
		} catch (InvalidQueryRepositoryException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryObject);
		}
		
		return travelers;
	}
	
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

