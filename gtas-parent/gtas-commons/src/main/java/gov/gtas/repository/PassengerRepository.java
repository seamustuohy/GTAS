package gov.gtas.repository;

import gov.gtas.model.Pax;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PassengerRepository extends CrudRepository<Pax, Long>{
	
	@Query("SELECT p FROM Pax p WHERE UPPER(p.firstName) = UPPER(:firstName) AND UPPER(p.lastName) = UPPER(:lastName)")
	public List<Pax> getPassengerByName(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query("SELECT p FROM Pax p WHERE UPPER(p.lastName) = UPPER(:lastName)")
	public List<Pax> getPassengersByLastName(@Param("lastName") String lastName);
	
}
