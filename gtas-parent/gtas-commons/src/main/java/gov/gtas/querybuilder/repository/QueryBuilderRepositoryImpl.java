package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Pax;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class QueryBuilderRepositoryImpl implements QueryBuilderRepository {
	
	@PersistenceContext 
 	private EntityManager entityManager;
	
	@Override
	public List<Flight> getFlightsByDynamicQuery(String query) {
		List<Flight> flights = new ArrayList<>();
		
//		query = "select f from Flight f join f.passengers p join p.documents d where d.documentNumber = 'A12345'";
		
		flights = entityManager.createQuery(query, Flight.class).getResultList();
		
		return flights;
	}

	@Override
	public List<Pax> getPassengersByDynamicQuery(String query) {
		List<Pax> passengers = new ArrayList<>();
		
		passengers = entityManager.createQuery(query, Pax.class).getResultList();
		
		return passengers;
	}

}
