package gov.cbp.taspd.gtas.querybuilder;

import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author GTAS5
 *
 */
public class QueryBuilderService {

	/**
	 * 
	 */
	public List<Flight> runQueryOnFlight(String query) {
		List<Flight> result = new ArrayList<>();
		
		Flight flight = new Flight();
		flight.setDestination("USA");
		flight.setEta(new Date());
		
		result.add(flight);
	
		return result;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<Pax> runQueryOnPassenger(String query) {
		List<Pax> result = new ArrayList<>();
		
		return result;
	}
	
	/**
	 * 
	 */
	public void saveQuery() {
		
	}
	
	/**
	 * 
	 */
	public void viewQuery() {
		
	}
	
	/**
	 * 
	 */
	public void editQuery() {
		
	}
	
	/**
	 * 
	 */
	public void deleteQuery() {
		
	}
}
