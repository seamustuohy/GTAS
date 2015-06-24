package gov.gtas.repository;

import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;
import java.util.List;
import org.hibernate.jpa.QueryHints;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Srinivas
 * class LookupRepositoryImpl serves as delivering lookup table data to the calling applications.
 * The purpose of this class is to enable the CACHING for methods passing QUERYHINT in the query.
 *
 */
@Repository
public class LookupRepositoryImpl implements LookUpRepository {

	@PersistenceContext 
 	private EntityManager entityManager;

	@Override
	public List<Country> getAllCountries() {
		Query query = entityManager.createQuery("from Country"); 
	 	//stores the query results in the second level cache (if enabled) 
	 	query.setHint(QueryHints.HINT_CACHEABLE, true); 
	 	
	 return (List<Country>)query.getResultList(); 
	}

	@Override
	public List<Carrier> getAllCarriers() {
		
		Query query = entityManager.createQuery("from Carrier"); 
	 	//stores the query results in the second level cache (if enabled) 
	 	query.setHint(QueryHints.HINT_CACHEABLE, true); 
	 	
	 return (List<Carrier>)query.getResultList(); 
	}

	@Override
	public List<Airport> getAllAirports() {
		Query query = entityManager.createQuery("from Airport"); 
	 	//stores the query results in the second level cache (if enabled) 
	 	query.setHint(QueryHints.HINT_CACHEABLE, true); 
	 	
	 return (List<Airport>)query.getResultList(); 
	}

	@Override
	@Transactional
	public void clearAllEntitiesCache() {
		SessionFactory sessionFactory = entityManager.unwrap(Session.class).getSessionFactory();
		sessionFactory.getCache().evictEntityRegion(Country.class);
		
	}

	@Override
	@Transactional
	public void clearEntityFromCache(Long id) {
		SessionFactory sessionFactory = entityManager.unwrap(Session.class).getSessionFactory();
		sessionFactory.getCache().evictEntity(Country.class, id);
		
	}

	@Override
	@Transactional
	public void clearHibernateCache() {
		SessionFactory sessionFactory = entityManager.unwrap(Session.class).getSessionFactory();
		sessionFactory.getCache().evictEntityRegions();
		sessionFactory.getCache().evictCollectionRegions();
		sessionFactory.getCache().evictDefaultQueryRegion();
		sessionFactory.getCache().evictQueryRegions();
		
	} 

}
