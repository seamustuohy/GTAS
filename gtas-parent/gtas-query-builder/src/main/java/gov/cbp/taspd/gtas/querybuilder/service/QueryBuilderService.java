package gov.cbp.taspd.gtas.querybuilder.service;

import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.querybuilder.model.AddressDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.FlightDisplay;
import gov.cbp.taspd.gtas.querybuilder.repository.AddressDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.FlightDisplayRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author GTAS5
 *
 */
public class QueryBuilderService {

	@Autowired
	FlightDisplayRepository flightRepository;
	@Autowired
	AddressDisplayRepository addressRepository;
	
	public List<FlightDisplay> getFlightDisplay() {
		List<FlightDisplay> flightDisplayList = (List<FlightDisplay>) flightRepository.findAll();
		
		return flightDisplayList;
	}
	
	public List<AddressDisplay> getAddressDisplay() {
		List<AddressDisplay> addressDisplayList = (List<AddressDisplay>) addressRepository.findAll();
		
		return addressDisplayList;
	}
	
	/**
	 * 
	 */
	public List<Flight> runQueryOnFlight(String query) {
		
		return null;
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
