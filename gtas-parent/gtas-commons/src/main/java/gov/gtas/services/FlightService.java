package gov.gtas.services;

import gov.gtas.model.Flight;

import java.util.Date;
import java.util.List;

public interface FlightService {
	
	public Flight create(Flight flight);
    public Flight delete(Long id);
    public List<Flight> findAll();
    public Flight update(Flight flight) ;
    public Flight findById(Long id);
    public List<Flight> getUniqueFlightByCriteria(String carrier,String flightNumber,String origin,String destination,Date flightDate);
}
