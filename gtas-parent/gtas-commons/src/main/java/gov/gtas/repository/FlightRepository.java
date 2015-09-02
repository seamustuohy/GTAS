package gov.gtas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.gtas.model.Flight;

public interface FlightRepository extends CrudRepository<Flight, Long> {
	@Query("SELECT f FROM Flight f WHERE f.carrier = :carrier "
	        + "AND f.flightNumber = :flightNumber "
	        + "AND f.origin = :origin "
	        + "AND f.destination=:destination "
	        + "AND f.flightDate = :flightDate")
	public Flight getFlightByCriteria(@Param("carrier") String carrier,
			@Param("flightNumber") String flightNumber,@Param("origin") String origin,
			@Param("destination") String destination,@Param("flightDate") Date flightDate);

	/**
	 * I thought I was having problems comparing dates with hibernate,
	 * but it appears that zero'ing out the time portion of the date was
	 * sufficient.  Use this method as a last resort to compare 
	 * flight dates. 
	 */
    @Query("SELECT f FROM Flight f WHERE f.carrier = :carrier "
            + "AND f.flightNumber = :flightNumber "
            + "AND f.origin = :origin "
            + "AND f.destination=:destination "
            + "AND f.flightDate between :startDate AND :endDate")
    public Flight getFlightByCriteria(
            @Param("carrier") String carrier,
            @Param("flightNumber") String flightNumber, 
            @Param("origin") String origin,
            @Param("destination") String destination, 
            @Param("startDate") Date startDate, 
            @Param("endDate") Date endDate);


    @Query("SELECT f FROM Flight f join f.passengers p where p.id = (:paxId)")
    public List<Flight> getFlightByPaxId(@Param("paxId") Long paxId);

    @Query("SELECT f FROM Flight f WHERE f.flightDate between :startDate AND :endDate")
    public List<Flight> getFlightsByDates(@Param("startDate") Date startDate, 
            							  @Param("endDate") Date endDate);

}