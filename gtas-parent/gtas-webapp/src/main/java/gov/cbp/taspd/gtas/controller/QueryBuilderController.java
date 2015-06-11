package gov.cbp.taspd.gtas.controller;

import gov.cbp.taspd.gtas.constants.URIConstants;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.querybuilder.QueryBuilderService;
import gov.cbp.taspd.gtas.web.querybuilder.model.Address;
import gov.cbp.taspd.gtas.web.querybuilder.model.Column;
import gov.cbp.taspd.gtas.web.querybuilder.model.Flight;
import gov.cbp.taspd.gtas.web.querybuilder.model.IWebModel;
import gov.cbp.taspd.gtas.web.querybuilder.model.QueryBuilderFlightResult;
import gov.cbp.taspd.gtas.web.querybuilder.model.QueryBuilderPassengerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
		
		modelList.add(createFlight());
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
	
	private Flight createFlight() {
		Flight flight = new Flight();
		List<Column> colList = new ArrayList<>();
		
		flight.setLabel("Flight");
		
		Column qbColumn = new Column();
		qbColumn.setId("carrier");
		qbColumn.setLabel("Carrier");
		qbColumn.setType("string");
		
		colList.add(qbColumn);
		
		qbColumn = new Column();
		qbColumn.setId("eta");
		qbColumn.setLabel("ETA");
		qbColumn.setType("date");
		colList.add(qbColumn);
		
		qbColumn.setId("etd");
		qbColumn.setLabel("ETD");
		qbColumn.setType("date");
		colList.add(qbColumn);
		
		qbColumn = new Column();
		qbColumn.setId("flightNumber");
		qbColumn.setLabel("Number");
		qbColumn.setType("string");
		colList.add(qbColumn);
		
		flight.setColumns(colList);
		
		return flight;
	}
	
	private Address createAddress() {
		Address address = new Address();
		List<Column> colList = new ArrayList<>();
		
		address.setLabel("Address");
		
		Column qbColumn = new Column();
		qbColumn.setId("firstName");
		qbColumn.setLabel("First Name");
		qbColumn.setType("string");
		
		colList.add(qbColumn);
		
		qbColumn = new Column();
		qbColumn.setId("lastName");
		qbColumn.setLabel("Last Name");
		qbColumn.setType("string");
		colList.add(qbColumn);
		
		qbColumn.setId("streetAddress1");
		qbColumn.setLabel("Street Address 1");
		qbColumn.setType("string");
		colList.add(qbColumn);
		
		qbColumn = new Column();
		qbColumn.setId("streetAddress2");
		qbColumn.setLabel("Street Address 2");
		qbColumn.setType("string");
		colList.add(qbColumn);
		
		address.setColumns(colList);
		
		return address;
	}
	
}
