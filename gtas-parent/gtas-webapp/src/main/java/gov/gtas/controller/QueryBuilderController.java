package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.querybuilder.model.IDisplay;
import gov.gtas.querybuilder.service.QueryBuilderService;
import gov.gtas.web.querybuilder.model.Column;
import gov.gtas.web.querybuilder.model.IQueryBuilderModel;
import gov.gtas.web.querybuilder.model.QueryBuilderFlightResult;
import gov.gtas.web.querybuilder.model.QueryBuilderModelFactory;
import gov.gtas.web.querybuilder.model.QueryBuilderPassengerResult;

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
@RequestMapping(Constants.QUERY_SERVICE)
public class QueryBuilderController {

	@Autowired
	QueryBuilderService queryService;

	@RequestMapping(value = Constants.INIT, method = RequestMethod.GET)
	public List<IQueryBuilderModel> initQueryBuilder() {
		
		return (getQueryBuilderModel());
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
	
	private List<IQueryBuilderModel> getQueryBuilderModel() {
		List<IQueryBuilderModel> modelList = new ArrayList<>();
		
		modelList.add(getModel(queryService.getAddressDisplay(), Constants.ADDRESS));
		modelList.add(getModel(queryService.getAPIDisplay(), Constants.API));
		modelList.add(getModel(queryService.getCreditCardDisplay(), Constants.CREDIT_CARD));
		modelList.add(getModel(queryService.getDocumentDisplay(), Constants.DOCUMENT));
		modelList.add(getModel(queryService.getEmailDisplay(), Constants.EMAIL));
		modelList.add(getModel(queryService.getFlightDisplay(), Constants.FLIGHT));
		modelList.add(getModel(queryService.getFrequentFlyerDisplay(), Constants.FREQUENT_FLYER));
		modelList.add(getModel(queryService.getHitsDisplay(), Constants.HITS));
		modelList.add(getModel(queryService.getNameOriginDisplay(), Constants.NAME_ORIGIN));
		modelList.add(getModel(queryService.getPassengerDisplay(), Constants.PASSENGER));
		modelList.add(getModel(queryService.getPhoneDisplay(), Constants.PHONE));
		modelList.add(getModel(queryService.getPNRDisplay(), Constants.PNR));
		modelList.add(getModel(queryService.getTravelAgencyDisplay(), Constants.TRAVEL_AGENCY));
		
		return modelList;
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
