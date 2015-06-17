package gov.gtas.repository;

import gov.gtas.model.Flight;

import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Long> {

}