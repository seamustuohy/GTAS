package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.InvalidUserRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;

import java.util.List;

public interface QueryBuilderRepository {
	
	public List<Flight> getFlightsByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException;
	public long totalFlightsByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException;
	public List<Object[]> getPassengersByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException;
	public long totalPassengersByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException;
	public UserQuery saveQuery(UserQuery query) throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, InvalidUserRepositoryException;
	public UserQuery editQuery(UserQuery query) throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, InvalidUserRepositoryException;
	public List<UserQuery> listQueryByUser(String userId) throws InvalidUserRepositoryException;
	public void deleteQuery(String userId, int queryId) throws InvalidUserRepositoryException, QueryDoesNotExistRepositoryException;
}
