package gov.gtas.repository;

import gov.gtas.model.Traveler;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PassengerRepository extends CrudRepository<Traveler, Long>{
	
	@Query("SELECT p FROM Traveler p WHERE UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
	public List<Traveler> getPassengerByName(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query("SELECT p FROM Traveler p WHERE UPPER(p.lastName) = UPPER(:lastName)")
	public List<Traveler> getPassengersByLastName(@Param("lastName") String lastName);

//    @Query("SELECT t FROM Traveler t join flight_traveler ft on ft.traveler_id = t.id where ft.flight_id = (:flightId)")
//    public List<Traveler> getPassengersByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT p FROM Flight f join f.passengers p where f.id = (:flightId)")
    public List<Traveler> getPassengersByFlightId(@Param("flightId") Long flightId);

}
