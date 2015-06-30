package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;

import java.util.List;

public interface QueryBuilderRepository {
	
	public List<Flight> getFlightsByDynamicQuery(String query);
	public List<Traveler> getPassengersByDynamicQuery(String query);
}
