package gov.cbp.taspd.gtas.controller;

import gov.cbp.taspd.gtas.constants.URIConstants;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.querybuilder.QueryBuilderService;
import gov.cbp.taspd.gtas.web.model.QueryBuilderFlight;
import gov.cbp.taspd.gtas.web.model.QueryBuilderPassenger;
import gov.cbp.taspd.gtas.web.model.QueryBuilderTable;

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
	public List<QueryBuilderTable> initQueryBuilder() {
		List<QueryBuilderTable> tableList = new ArrayList<>();
		
		QueryBuilderTable qbTable = new QueryBuilderTable();
		qbTable.setTableName("Flight");
		TreeMap<String, String> columns = new TreeMap<>();
		columns.put("carrier", "Carrier");
		columns.put("eta", "ETA");
		columns.put("etd", "ETD");
		columns.put("flightNumber", "Number");
		
		qbTable.setColumn(columns);
		
		tableList.add(qbTable);
		
		return tableList;
	}
	
	/**
	 * 
	 * @param query
	 */
	@RequestMapping(value = URIConstants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.GET)
	public List<QueryBuilderFlight> runQueryOnFlight(String query) {
		
		return mapToQueryBuilderFlight(queryService.runQueryOnFlight(query));
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = URIConstants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.GET)
	public List<QueryBuilderPassenger> runQueryOnPassenger(String query) {
		
		return mapToQueryBuilderPassenger(queryService.runQueryOnPassenger(query));
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
	
	private List<QueryBuilderFlight> mapToQueryBuilderFlight(List<Flight> flightList) {
		List<QueryBuilderFlight> qbFlightList = new ArrayList<>();
		
		if(flightList != null) {
			for(Flight flight : flightList) {
				
				if(flight != null) {
					QueryBuilderFlight qbFlight = new QueryBuilderFlight();
					
					qbFlight.setId(flight.getId());
					qbFlight.setFlightNumber(flight.getNumber());
					qbFlight.setCarrierCode(flight.getCarrier());
					qbFlight.setOrigin(flight.getOrigin());
					qbFlight.setOriginCountry(null);
					qbFlight.setDestination(flight.getDestination());
					qbFlight.setDestinationCountry(null);
					qbFlight.setDepartureDt(flight.getEtd());
					qbFlight.setArrivalDt(flight.getEta());
					
					qbFlightList.add(qbFlight);
				}
			}
		}
		
		return qbFlightList;
	}
	
	private List<QueryBuilderPassenger> mapToQueryBuilderPassenger(List<Pax> passengerList) {
		List<QueryBuilderPassenger> qbPassengerList = new ArrayList<>();
		
		if(passengerList != null) {
			for(Pax passenger : passengerList) {
				if(passenger != null) {
					QueryBuilderPassenger qbPassenger = new QueryBuilderPassenger();
					
					qbPassenger.setId(passenger.getId());
					qbPassenger.setFirstName(passenger.getFirstName());
					qbPassenger.setLastName(passenger.getLastName());
					qbPassenger.setPassengerType(null);
					qbPassenger.setGender(passenger.getGender());
					qbPassenger.setDob(passenger.getDob());
					qbPassenger.setCitizenship(null);
					qbPassenger.setDocumentNumber(null);
					qbPassenger.setDocumentType(null);
					qbPassenger.setDocumentIssuanceContry(null);
					qbPassenger.setSeatNumber(null);
					qbPassenger.setStatus(null);
					qbPassenger.setOnWatchList(true);
					qbPassenger.setOnSomethingList(true);
					
					qbPassengerList.add(qbPassenger);
				}
			}
		}
		
		return qbPassengerList;
	}
}
