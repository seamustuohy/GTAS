package gov.gtas.querybuilder.repository;

import gov.gtas.querybuilder.model.FlightDisplay;
import gov.gtas.querybuilder.model.IDisplay;

import org.springframework.data.repository.CrudRepository;

public interface FlightDisplayRepository extends CrudRepository<FlightDisplay, Integer> {

}
