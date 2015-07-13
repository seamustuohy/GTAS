package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.model.Query;

import java.util.List;

public interface QueryBuilderRepository {
	
	public List<Flight> getFlightsByDynamicQuery(String query);
	public List<Traveler> getPassengersByDynamicQuery(String query);
	public Query saveQuery(Query query) throws QueryAlreadyExistsException;
	public Query editQuery(Query query) throws QueryAlreadyExistsException;
	public List<Query> listQueryByUser(String userId);
	public Query getQuery(int id);
	public void deleteQuery(String userId, int queryId);
}
