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
	
}
