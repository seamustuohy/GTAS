package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.exceptions.InvalidQueryObjectException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryNotUniqueRepositoryException;
import gov.gtas.querybuilder.model.UserQuery;

import java.text.ParseException;
import java.util.List;

public interface QueryBuilderRepository {
	
	public List<Flight> getFlightsByDynamicQuery(QueryObject queryObject) throws InvalidQueryObjectException, ParseException;
	public List<Traveler> getPassengersByDynamicQuery(QueryObject queryObject) throws InvalidQueryObjectException, ParseException;
	public UserQuery saveQuery(UserQuery query) throws QueryNotUniqueRepositoryException;
	public UserQuery editQuery(UserQuery query) throws QueryNotUniqueRepositoryException, QueryDoesNotExistRepositoryException;
	public List<UserQuery> listQueryByUser(String userId);
	public void deleteQuery(String userId, int queryId) throws QueryDoesNotExistRepositoryException;
}
