package gov.gtas.repository;

import gov.gtas.model.lookup.Airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AirportRepository extends CrudRepository<Airport, Long>{
	
	@Query("SELECT a FROM Airport a WHERE UPPER(a.iata) = UPPER(:airportCode)")
	public List<Airport> getAirportByThreeLetterCode(@Param("airportCode") String airportCode);
	
	@Query("SELECT a FROM Airport a WHERE UPPER(a.icao) = UPPER(:airportCode)")
	public List<Airport> getAirportByFourLetterCode(@Param("airportCode") String airportCode);

}
