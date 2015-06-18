package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.querybuilder.model.AddressDisplay;
import gov.gtas.querybuilder.model.CreditCardDisplay;
import gov.gtas.querybuilder.model.FrequentFlyerDisplay;
import gov.gtas.querybuilder.model.IDisplay;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.web.querybuilder.model.Address;
import gov.gtas.web.querybuilder.model.Column;
import gov.gtas.web.querybuilder.model.IQueryBuilderModel;
import gov.gtas.web.querybuilder.model.QueryBuilderFlightResult;
import gov.gtas.web.querybuilder.model.QueryBuilderModelFactory;
import gov.gtas.web.querybuilder.model.QueryBuilderPassengerResult;
import gov.gtas.web.querybuilder.model.BaseQueryBuilderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	@RequestMapping(value = Constants.RUN_QUERY_FLIGHT_URI, method=RequestMethod.GET)
	public List<QueryBuilderFlightResult> runQueryOnFlight(String query) {
		
		queryService.runQueryOnFlight(query);
		
		return null;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = Constants.RUN_QUERY_PASSENGER_URI, method = RequestMethod.GET)
	public List<QueryBuilderPassengerResult> runQueryOnPassenger(String query) {
		
		return null;
	}
	
	/**
	 * 
	 * @param query
	 */
	@RequestMapping(value = Constants.SAVE_QUERY_URI)
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
		
		modelMap.put(Constants.ADDRESS, getModel(queryService.getAddressDisplay(), Constants.ADDRESS));
		modelMap.put(Constants.API, getModel(queryService.getAPIDisplay(), Constants.API));
		modelMap.put(Constants.CREDIT_CARD, getModel(queryService.getCreditCardDisplay(), Constants.CREDIT_CARD));
		modelMap.put(Constants.DOCUMENT, getModel(queryService.getDocumentDisplay(), Constants.DOCUMENT));
		modelMap.put(Constants.EMAIL, getModel(queryService.getEmailDisplay(), Constants.EMAIL));
		modelMap.put(Constants.FLIGHT, getModel(queryService.getFlightDisplay(), Constants.FLIGHT));
		modelMap.put(Constants.FREQUENT_FLYER, getModel(queryService.getFrequentFlyerDisplay(), Constants.FREQUENT_FLYER));
		modelMap.put(Constants.HITS, getModel(queryService.getHitsDisplay(), Constants.HITS));
		modelMap.put(Constants.NAME_ORIGIN, getModel(queryService.getNameOriginDisplay(), Constants.NAME_ORIGIN));
		modelMap.put(Constants.PASSENGER, getModel(queryService.getPassengerDisplay(), Constants.PASSENGER));
		modelMap.put(Constants.PHONE, getModel(queryService.getPhoneDisplay(), Constants.PHONE));
		modelMap.put(Constants.PNR, getModel(queryService.getPNRDisplay(), Constants.PNR));
		modelMap.put(Constants.TRAVEL_AGENCY, getModel(queryService.getTravelAgencyDisplay(), Constants.TRAVEL_AGENCY));
		
		return modelMap;
	}
	
	private IQueryBuilderModel getModel(List<? extends IDisplay> displayList, String modelType) {
		QueryBuilderModelFactory factory = new QueryBuilderModelFactory();
		
		IQueryBuilderModel model = factory.getQueryBuilderModel(modelType);
				
		if(displayList != null)
		{
			List<Column> colList = new ArrayList<>();
			
			for(IDisplay display : displayList) {
					
				Column column = new Column();
				column.setId(display.getColumnName());
				column.setLabel(display.getDisplayName());
				column.setType(display.getType());
				
				colList.add(column);
			}
			
			model.setColumns(colList);
		}
		
		return model;
	}
	
}
