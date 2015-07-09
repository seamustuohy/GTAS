package gov.gtas.repository;

import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;

import java.util.List;

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

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private AirportRepository airportRepository;

	@Autowired
	private CarrierRepository carrierRepository;

	@Override
	@Cacheable("country")
	public List<Country> getAllCountries() {
		return (List<Country>) countryRepository.findAll();
	}

	@Override
	@Cacheable("carrier")
	public List<Carrier> getAllCarriers() {
		return (List<Carrier>) carrierRepository.findAll();
	}

	@Override
	@Cacheable("airport")
	public List<Airport> getAllAirports() {
		return (List<Airport>) airportRepository.findAll();
	}

	@Override
	@Transactional
	@CacheEvict(value = "carrier", allEntries = true)
	public void clearAllEntitiesCache() {
		// remove all entities from cache
	}

	@Override
	@Transactional
	public void clearEntityFromCache(Long id) {
		// NOTE: later

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
		return countryRepository.findByName(countryName);
	}

	@Transactional
	@CacheEvict(value = "country", key = "#countryName")
	public void removeCountryCache(String countryName) {
		// remove entity from cache only
	}

	@Transactional
	public void deleteCountryDb(Country country) {
		countryRepository.delete(country);
	}

}
