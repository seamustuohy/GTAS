package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.model.BaseEntity;
import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.querybuilder.util.EntityEnum;
import gov.gtas.web.querybuilder.model.IQueryBuilderModel;
import gov.gtas.web.querybuilder.model.QueryBuilderFlightResult;
import gov.gtas.web.querybuilder.model.QueryBuilderModelFactory;
import gov.gtas.web.querybuilder.model.QueryBuilderPassengerResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author GTAS5
 *
 */

@RestController
@RequestMapping(Constants.QUERY_SERVICE)
public class QueryBuilderController {

	@Autowired
	QueryBuilderService queryService;

	@RequestMapping(value = Constants.INIT, method = RequestMethod.GET)
	public Map<String, IQueryBuilderModel> initQueryBuilder() {
		
		return getQueryBuilderModel();
	}
	
	/**
	 * 
	 * @param query
	 */
	@RequestMapping(value = Constants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.POST)
	public List<QueryBuilderFlightResult> runQueryOnFlight(@RequestBody QueryObject queryObject) {
		List<QueryBuilderFlightResult> qbFlights = new ArrayList<>();
		
		if(queryObject != null) {
			qbFlights = mapFlightToQueryFlightResult(queryService.runQuery(queryObject, EntityEnum.FLIGHT));
		}
		
		return qbFlights;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.POST)
	public List<QueryBuilderPassengerResult> runQueryOnPassenger(@RequestBody QueryObject queryObject) {
		List<QueryBuilderPassengerResult> qbPassengers = new ArrayList<>();
		
		if(queryObject != null) {
			qbPassengers = mapFlightToQueryPassengerResult(queryService.runQuery(queryObject, EntityEnum.PAX));
		}
		
		return qbPassengers;
	}
	
	/**
	 * 
	 * @param query
	 */
	@RequestMapping(value = Constants.SAVE_QUERY_URI, method = RequestMethod.POST)
	public void saveQuery(String query) {
		
		queryService.saveQuery();
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = Constants.VIEW_QUERY_URI)
	public void viewQuery() {
		
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = Constants.EDIT_QUERY_URI)
	public void editQuery() {
		
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = Constants.DELETE_QUERY_URI)
	public void deleteQuery() {
		
		queryService.deleteQuery();
	}
	
	private Map<String, IQueryBuilderModel> getQueryBuilderModel() {
		Map<String, IQueryBuilderModel> modelMap = new LinkedHashMap<>();
		
		modelMap.put(EntityEnum.ADDRESS.getFriendlyName(), getModel(EntityEnum.ADDRESS));
		modelMap.put(EntityEnum.API.getFriendlyName(), getModel(EntityEnum.API));
		modelMap.put(EntityEnum.CREDIT_CARD.getFriendlyName(), getModel(EntityEnum.CREDIT_CARD));
		modelMap.put(EntityEnum.DOCUMENT.getFriendlyName(), getModel(EntityEnum.DOCUMENT));
		modelMap.put(EntityEnum.EMAIL.getFriendlyName(), getModel(EntityEnum.EMAIL));
		modelMap.put(EntityEnum.FLIGHT.getFriendlyName(), getModel(EntityEnum.FLIGHT));
		modelMap.put(EntityEnum.FREQUENT_FLYER.getFriendlyName(), getModel(EntityEnum.FREQUENT_FLYER));
		modelMap.put(EntityEnum.HITS.getFriendlyName(), getModel(EntityEnum.HITS));
		modelMap.put(EntityEnum.NAME_ORIGIN.getFriendlyName(), getModel(EntityEnum.NAME_ORIGIN));
		modelMap.put(EntityEnum.PAX.getFriendlyName(), getModel(EntityEnum.PAX));
		modelMap.put(EntityEnum.PHONE.getFriendlyName(), getModel(EntityEnum.PHONE));
		modelMap.put(EntityEnum.PNR.getFriendlyName(), getModel(EntityEnum.PNR));
		modelMap.put(EntityEnum.TRAVEL_AGENCY.getFriendlyName(), getModel(EntityEnum.TRAVEL_AGENCY));
		
		return modelMap;
	}
	
	private IQueryBuilderModel getModel(EntityEnum modelType) {
		QueryBuilderModelFactory factory = new QueryBuilderModelFactory();
		
		IQueryBuilderModel model = factory.getQueryBuilderModel(modelType);
		
		return model;
	}
	
	private List<QueryBuilderFlightResult> mapFlightToQueryFlightResult(List<? extends BaseEntity> list) {
		List<QueryBuilderFlightResult> qbFlights = new ArrayList<>();
		SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		
		if(list != null && list.size() > 0) {
			List<Flight> flights = (List<Flight>) list;
			for(Flight flight : flights) {
				if(flight != null) {
					QueryBuilderFlightResult qbFlight = new QueryBuilderFlightResult();
					
					qbFlight.setId(flight.getId());
					qbFlight.setFlightNumber(flight.getFlightNumber());
					qbFlight.setCarrierCode(flight.getCarrier() != null ? flight.getCarrier().getIata() : "");
					qbFlight.setOrigin(flight.getOrigin() != null ? flight.getOrigin().getCity() : "");
					qbFlight.setOriginCountry(flight.getOriginCountry() != null ? flight.getOriginCountry().getIso3() : "");
					qbFlight.setDepartureDt(sdFormat.format(flight.getEtd()));
					qbFlight.setDestination(flight.getDestination() != null ? flight.getDestination().getCity() : "");
					qbFlight.setDestinationCountry(flight.getDestinationCountry() != null ? flight.getDestinationCountry().getIso3() : "");
					qbFlight.setArrivalDt(sdFormat.format(flight.getEta()));
					
					qbFlights.add(qbFlight);
				}
			}
		}
		
		return qbFlights;
	}
	
	private List<QueryBuilderPassengerResult> mapFlightToQueryPassengerResult(List<? extends BaseEntity> list) {
		List<QueryBuilderPassengerResult> qbPassengers = new ArrayList<>();
		SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		if(list != null && list.size() > 0) {
			List<Traveler> travelers = (List<Traveler>) list;
			for(Traveler traveler : travelers) {
				if(traveler != null) {
					QueryBuilderPassengerResult qbPassenger = new QueryBuilderPassengerResult();
					
					qbPassenger.setOnSomethingList(true);
					qbPassenger.setOnWatchList(true);
					qbPassenger.setFirstName(traveler.getFirstName());
					qbPassenger.setLastName(traveler.getLastName());
					qbPassenger.setPassengerType("not yet provided");
					qbPassenger.setGender(traveler.getGender() != null ? traveler.getGender().toString() : "");
					qbPassenger.setDob(sdFormat.format(traveler.getDob()));
					qbPassenger.setCitizenship(traveler.getCitizenshipCountry() != null ? traveler.getCitizenshipCountry().getIso3() : "");
					qbPassenger.setDocumentNumber("");
					qbPassenger.setDocumentType("");
					qbPassenger.setDocumentIssuanceContry("");
					qbPassenger.setCarrierCode("");
					qbPassenger.setFlightNumber("");
					qbPassenger.setOrigin("");
					qbPassenger.setDestination("");
					qbPassenger.setDepartureDt("");
					qbPassenger.setArrivalDt("");
					qbPassenger.setSeatNumber("");
					qbPassenger.setStatus("");
					
					qbPassengers.add(qbPassenger);
				}
			}
		}
		
		return qbPassengers;
	}
	
}
