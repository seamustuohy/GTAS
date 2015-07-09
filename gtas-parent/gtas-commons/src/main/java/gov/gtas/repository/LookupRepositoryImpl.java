package gov.gtas.repository;

import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Srinivas class LookupRepositoryImpl serves as delivering lookup table
 *         data to the calling applications. The purpose of this class is to
 *         enable the CACHING for methods passing QUERYHINT in the query.
 *
 */
@Repository
public class LookupRepositoryImpl implements LookUpRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CountryRepository countryRepository;

	@SuppressWarnings("unchecked")
	@Override
	@Cacheable("country")
	public List<Country> getAllCountries() {
		Query query = entityManager.createQuery("from Country");
		return (List<Country>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Cacheable("carrier")
	public List<Carrier> getAllCarriers() {
		Query query = entityManager.createQuery("from Carrier");
		return (List<Carrier>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Cacheable("airport")
	public List<Airport> getAllAirports() {
		Query query = entityManager.createQuery("from Airport");
		return (List<Airport>) query.getResultList();
	}

	@Override
	@Transactional
	@CacheEvict(value = "carrier", allEntries = true)
	public void clearAllEntitiesCache() {

	}

	@Override
	@Transactional
	public void clearEntityFromCache(Long id) {
		// SessionFactory sessionFactory = entityManager.unwrap(Session.class)
		// .getSessionFactory();
		// sessionFactory.getCache().evictEntity(Country.class, id);

	}

	@Override
	@Transactional
	public Country saveCountry(Country country) {
		return countryRepository.save(country);
	}

	@Override
	@Transactional
	@Cacheable(value = "country", key = "#countryName")
	public Country getCountry(String countryName) {
		return (Country) entityManager.createQuery(
			    "SELECT c FROM Country c WHERE c.name LIKE :countryName")
			    .setParameter("countryName", countryName)
			    .getSingleResult();
	}
	
	@Transactional
	@CacheEvict(value = "country", key = "#countryName")
	public void removeCountryCache(String countryName) {
		// do not actually delete
	}
	
	@Transactional
	public void deleteCountryDb(Country country) {
		countryRepository.delete(country);
	}

}
