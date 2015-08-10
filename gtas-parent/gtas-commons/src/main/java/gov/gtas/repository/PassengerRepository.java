package gov.gtas.repository;

import gov.gtas.model.Passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PassengerRepository extends CrudRepository<Passenger, Long>{
	
	@Query("SELECT p FROM Passenger p WHERE UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
	public List<Passenger> getPassengerByName(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query("SELECT p FROM Passenger p WHERE UPPER(p.lastName) = UPPER(:lastName)")
	public List<Passenger> getPassengersByLastName(@Param("lastName") String lastName);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId)")
    public List<Passenger> getPassengersByFlightId(@Param("flightId") Long flightId);

}
