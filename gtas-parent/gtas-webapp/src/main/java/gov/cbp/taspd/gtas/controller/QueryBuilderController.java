package gov.cbp.taspd.gtas.controller;

import gov.cbp.taspd.gtas.constants.URIConstants;
import gov.cbp.taspd.gtas.querybuilder.model.AddressDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.FlightDisplay;
import gov.cbp.taspd.gtas.querybuilder.service.QueryBuilderService;
import gov.cbp.taspd.gtas.web.querybuilder.model.Address;
import gov.cbp.taspd.gtas.web.querybuilder.model.Column;
import gov.cbp.taspd.gtas.web.querybuilder.model.Flight;
import gov.cbp.taspd.gtas.web.querybuilder.model.IWebModel;
import gov.cbp.taspd.gtas.web.querybuilder.model.QueryBuilderFlightResult;
import gov.cbp.taspd.gtas.web.querybuilder.model.QueryBuilderPassengerResult;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author GTAS5
 *
 */

@RestController
@RequestMapping("/query")
public class QueryBuilderController {

	@Autowired
	QueryBuilderService queryService;
	

	@RequestMapping(value = URIConstants.INIT, method = RequestMethod.GET)
	public List<IWebModel> initQueryBuilder() {
		List<IWebModel> modelList = new ArrayList<>();
		
		modelList.add(getFlightDisplay());
		modelList.add(createAddress());
		
		return modelList;
	}
	
	/**
	 * 
	 * @param query
	 */
	@RequestMapping(value = URIConstants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.GET)
	public List<QueryBuilderFlightResult> runQueryOnFlight(String query) {
		
		return null;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = URIConstants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.GET)
	public List<QueryBuilderPassengerResult> runQueryOnPassenger(String query) {
		
		return null;
	}
	
	/**
	 * 
	 * @param query
	 */
	@RequestMapping(value = URIConstants.SAVE_QUERY_URI)
	public void saveQuery(String query) {
		
		queryService.saveQuery();
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = URIConstants.VIEW_QUERY_URI)
	public void viewQuery() {
		
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = URIConstants.EDIT_QUERY_URI)
	public void editQuery() {
		
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = URIConstants.DELETE_QUERY_URI)
	public void deleteQuery() {
		
		queryService.deleteQuery();
	}
	
	private Flight getFlightDisplay() {
		Flight flight = new Flight();
		
		List<FlightDisplay> flightDisplayList = queryService.getFlightDisplay();
		
		if(flightDisplayList != null) {
			flight.setLabel("Flight");
			List<Column> colList = new ArrayList<>();
			
			for(FlightDisplay flightDisplay : flightDisplayList) {
				Column column = new Column();
				column.setId(flightDisplay.getColumnName());
				column.setLabel(flightDisplay.getDisplayName());
				column.setType(flightDisplay.getType());
				
				colList.add(column);
			}
			
			flight.setColumns(colList);
		}
				
		return flight;
	}
	
	private Address createAddress() {
		Address address = new Address();
		List<AddressDisplay> addressDisplayList = queryService.getAddressDisplay();
		
		if(addressDisplayList != null) {
			address.setLabel("Address");
			List<Column> colList = new ArrayList<>();
			
			for(AddressDisplay addressDisplay : addressDisplayList) {
				Column column = new Column();
				column.setId(addressDisplay.getColumnName());
				column.setLabel(addressDisplay.getDisplayName());
				column.setType(addressDisplay.getType());
				
				colList.add(column);
			}
			
			address.setColumns(colList);
		}
		
		return address;
	}
	
}
