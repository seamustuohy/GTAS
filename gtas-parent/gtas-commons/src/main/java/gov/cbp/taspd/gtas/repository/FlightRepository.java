package gov.cbp.taspd.gtas.repository;

import gov.cbp.taspd.gtas.model.Flight;

import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Long> {

}