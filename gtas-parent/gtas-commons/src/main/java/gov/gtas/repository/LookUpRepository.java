package gov.gtas.repository;

import java.util.List;

import gov.gtas.model.FlightDirection;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;

public interface LookUpRepository {
	List<Country> getAllCountries();

	List<Carrier> getAllCarriers();

	List<Airport> getAllAirports();
	
	List<FlightDirection> getFlightDirections();

	public void clearAllEntitiesCache();

	public void clearEntityFromCache(Long id);

	public Country saveCountry(Country country);

	public Country getCountry(String countryName);

	public void removeCountryCache(String countryName);

	public void deleteCountryDb(Country country);

}
