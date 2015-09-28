package gov.gtas.services;

import java.util.Date;
import java.util.List;

import gov.gtas.model.Flight;

public interface FlightService {	
	public Flight create(Flight flight);
    public Flight update(Flight flight) ;
    public Flight findById(Long id);

	public FlightsPage findAll(int pageNumber, int pageSize);
    
    public Flight getUniqueFlightByCriteria(String carrier, String flightNumber, String origin, String destination, Date flightDate);
    public List<Flight> getFlightByPaxId(Long paxId);
    public List<Flight> getFlightsByDates(Date startDate, Date endDate);
}
