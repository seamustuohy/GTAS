package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
