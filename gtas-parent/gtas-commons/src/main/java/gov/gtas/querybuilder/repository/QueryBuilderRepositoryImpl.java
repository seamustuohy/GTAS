package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.User;
import gov.gtas.querybuilder.exceptions.Constants;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.model.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class QueryBuilderRepositoryImpl implements QueryBuilderRepository {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilderRepository.class);
	
	@PersistenceContext 
 	private EntityManager entityManager;
	
	@Override
	public List<Flight> getFlightsByDynamicQuery(String query) {
		List<Flight> flights = new ArrayList<>();
		
		if(query != null ) {
			logger.debug("Getting Flights by this query: " + query);
			
			flights = entityManager.createQuery(query, Flight.class).getResultList();
			
			logger.debug("Number of Flights returned: " + (flights != null ? flights.size() : "Flight result is null"));
		}
		
		return flights;
	}

	@Override
	public List<Traveler> getPassengersByDynamicQuery(String query) {
		List<Traveler> passengers = new ArrayList<>();
		
		if(query != null) {
			logger.debug("Getting Passengers by this query: " + query);
			
			passengers = entityManager.createQuery(query, Traveler.class).getResultList();
			
			logger.debug("Number of Passengers returned: " + (passengers != null ? passengers.size() : "Passenger result is null"));
		}
		
		return passengers;
	}

	@Override
	@Transactional
	public Query saveQuery(Query query) throws QueryAlreadyExistsException {
		if(query != null & query.getQueryText() != null) {
			
			if(isUniqueTitle(query)) {
				query.setId(null);
				query.setTitle(query.getTitle().trim());
				query.setCreatedDt(new Date());
				
				// save query to database
				entityManager.persist(query);
			}
			else {
				throw new QueryAlreadyExistsException(Constants.QUERY_EXISTS_ERROR_MSG);
			}
		}
		
		return query;
	}

	@Override
	@Transactional
	public Query editQuery(Query query) throws QueryAlreadyExistsException {
		Query queryToSave = new Query();
		boolean uniqueTitle = true;
		
		if(query != null && query.getId() != null) {
			
				queryToSave = entityManager.find(Query.class, query.getId());
				
				if(!query.getTitle().trim().equalsIgnoreCase(queryToSave.getTitle().trim())) {
					uniqueTitle = isUniqueTitle(query);
				}
				
				if(uniqueTitle) {
					queryToSave.setTitle(query.getTitle().trim());
					queryToSave.setDescription(query.getDescription().trim());
					queryToSave.setQueryText(query.getQueryText());
					
					entityManager.flush();
				}
				else {
					throw new QueryAlreadyExistsException(Constants.QUERY_EXISTS_ERROR_MSG);
				}
		}
	
		return queryToSave;
	}

	@Override
	public List<Query> listQueryByUser(String userId) {
		List<Query> queryList = new ArrayList<>();
		
		queryList = entityManager.createNamedQuery("Query.listQueryByUser", Query.class)
				.setParameter("createdBy", userId).getResultList();
		
		return queryList;
	}
	
	public Query getQuery(int id) {

		return entityManager.find(Query.class, id);
	}

	@Override
	@Transactional
	public void deleteQuery(String userId, int id) {
		
		if(userId != null) {
			Query query = entityManager.find(Query.class, id);
			
			if(query != null) {
				User user = new User();
				user.setUserId(userId);
				
				query.setDeletedDt(new Date());
				query.setDeletedBy(user);
				
				entityManager.flush();
			}
		}
	}
	
	private boolean isUniqueTitle(Query query) {
		boolean unique = false;
		
		// check uniqueness of query title for this user
		List<Integer> ids = entityManager.createNamedQuery("Query.checkUniqueTitle", Integer.class)
				.setParameter("createdBy", query.getCreatedBy())
				.setParameter("title", query.getTitle().trim()).getResultList();
		
		if(ids == null || ids.size() == 0) {
			unique = true;
		}
		
		return unique;
	}
	
}
