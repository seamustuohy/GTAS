package gov.gtas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;

public interface PassengerRepository extends PagingAndSortingRepository<Passenger, Long>{
	
	@Query("SELECT p FROM Passenger p WHERE UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
	public List<Passenger> getPassengerByName(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query("SELECT p FROM Passenger p WHERE UPPER(p.lastName) = UPPER(:lastName)")
	public List<Passenger> getPassengersByLastName(@Param("lastName") String lastName);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId)")
    public List<Passenger> getPassengersByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT p FROM Flight f join f.passengers p")
    public Page<Passenger> getAllPassengers(Pageable pageable);

    @Query("SELECT p, f FROM Flight f join f.passengers p")
    public List<Object[]> getAllPassengers2(Pageable pageable);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId)")
    public Page<Passenger> getPassengersByFlightId(@Param("flightId") Long flightId, Pageable pageable);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId) AND UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
    public List<Passenger> getPassengersByFlightIdAndName(@Param("flightId") Long flightId, @Param("firstName") String firstName,@Param("lastName") String lastName);
    
    //@Query("SELECT p FROM Flight f join f.passengers p WHERE f.eta >= CURDATE() ORDER BY f.eta asc")
    //@Query("SELECT p FROM Flight f join f.passengers p ORDER BY f.eta asc")
    @Query("SELECT p FROM Passenger p ORDER BY p.id asc")
    public Page<Passenger> getPassengersFromUpcomingFlights(Pageable pageable);
    
    @Query("SELECT p FROM Passenger p WHERE UPPER(p.lastName) LIKE %?1%")
	public Page<Passenger> getPaxByLastName(String lastName, Pageable pageable);
    
    @Query("SELECT p FROM Passenger p JOIN p.flights f WHERE f.flightDate between :startDate AND :endDate")
    public List<Passenger> getPassengersByFlightDates(@Param("startDate") Date startDate, 
            							  @Param("endDate") Date endDate);

}
