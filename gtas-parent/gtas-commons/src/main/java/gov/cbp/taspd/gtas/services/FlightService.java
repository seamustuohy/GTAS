package gov.cbp.taspd.gtas.services;

import gov.cbp.taspd.gtas.model.Flight;


import java.util.List;

public interface FlightService {
	
	public Flight create(Flight flight);
    public Flight delete(Long id);
    public List<Flight> findAll();
    public Flight update(Flight flight) ;
    public Flight findById(Long id);

}
