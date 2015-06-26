package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.constants.TableNameEnum;
import gov.gtas.model.Flight;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.web.querybuilder.model.IQueryBuilderModel;
import gov.gtas.web.querybuilder.model.QueryBuilderModelFactory;
import gov.gtas.web.querybuilder.model.QueryBuilderPassengerResult;

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
	public List<Flight> runQueryOnFlight(String JSONQuery) {
		
		 return queryService.runQueryOnFlight(JSONQuery);
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
		
		modelMap.put(TableNameEnum.ADDRESS.toString(), getModel(TableNameEnum.ADDRESS.toString()));
		modelMap.put(TableNameEnum.API.toString(), getModel(TableNameEnum.API.toString()));
		modelMap.put(TableNameEnum.CREDIT_CARD.toString(), getModel(TableNameEnum.CREDIT_CARD.toString()));
		modelMap.put(TableNameEnum.DOCUMENT.toString(), getModel(TableNameEnum.DOCUMENT.toString()));
		modelMap.put(TableNameEnum.EMAIL.toString(), getModel(TableNameEnum.EMAIL.toString()));
		modelMap.put(TableNameEnum.FLIGHT.toString(), getModel(TableNameEnum.FLIGHT.toString()));
		modelMap.put(TableNameEnum.FREQUENT_FLYER.toString(), getModel(TableNameEnum.FREQUENT_FLYER.toString()));
		modelMap.put(TableNameEnum.HITS.toString(), getModel(TableNameEnum.HITS.toString()));
		modelMap.put(TableNameEnum.NAME_ORIGIN.toString(), getModel(TableNameEnum.NAME_ORIGIN.toString()));
		modelMap.put(TableNameEnum.PASSENGER.toString(), getModel(TableNameEnum.PASSENGER.toString()));
		modelMap.put(TableNameEnum.PHONE.toString(), getModel(TableNameEnum.PHONE.toString()));
		modelMap.put(TableNameEnum.PNR.toString(), getModel(TableNameEnum.PNR.toString()));
		modelMap.put(TableNameEnum.TRAVEL_AGENCY.toString(), getModel(TableNameEnum.TRAVEL_AGENCY.toString()));
		
		return modelMap;
	}
	
	private IQueryBuilderModel getModel(String modelType) {
		QueryBuilderModelFactory factory = new QueryBuilderModelFactory();
		
		IQueryBuilderModel model = factory.getQueryBuilderModel(modelType);
		
		return model;
	}
	
}
