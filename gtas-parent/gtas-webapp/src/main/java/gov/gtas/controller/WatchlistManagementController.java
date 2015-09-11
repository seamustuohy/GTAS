package gov.gtas.controller;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.WatchlistConstants;
import gov.gtas.constants.Constants;
import gov.gtas.enumtype.Status;
import gov.gtas.error.CommonServiceException;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.svc.RuleManagementService;
import gov.gtas.svc.WatchlistService;
import gov.gtas.util.SampleDataGenerator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST service end-point controller for creating and managing watch lists.
 * 
 * @author GTAS3 (AB)
 *
 */
@RestController
@RequestMapping(Constants.WL_ROOT)
public class WatchlistManagementController {
	/*
	 * The logger for the UdrManagementController
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WatchlistManagementController.class);

	@Autowired
    private WatchlistService watchlistService;
	
	@Autowired
	private RuleManagementService ruleManagementService;

	@RequestMapping(value = Constants.WL_GET_BY_NAME, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public WatchlistSpec getWatchlist(@PathVariable String entity, @PathVariable String name) {
		System.out.println("******** name =" + name);
		WatchlistSpec resp = watchlistService.fetchWatchlist(name);
		if(resp == null){
			resp = 	new WatchlistSpec(name, entity);
		}
		return resp;
	}

	@RequestMapping(value = Constants.WL_GETALL, method = RequestMethod.GET)
	public List<WatchlistSpec> getWatchlist() {
		return watchlistService.fetchAllWatchlists();
	}

	@RequestMapping(value = Constants.WL_GETDRL, method = RequestMethod.GET)
	public JsonServiceResponse getDrl() {
		String rules = ruleManagementService.fetchDrlRulesFromKnowledgeBase(WatchlistConstants.WL_KNOWLEDGE_BASE_NAME);
		return createDrlRulesResponse(rules);
	}
	
	/**
	 * Creates the DRL rule response JSON object.
	 * @param rules the DRL rules.
	 * @return the JSON response object containing the rules.
	 */
	private JsonServiceResponse createDrlRulesResponse(String rules){
		JsonServiceResponse resp = new JsonServiceResponse(Status.SUCCESS, "Drools rules fetched successfully");
		String[] lines = rules.split("\n");
		resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute("DRL Rules", lines));
		return resp;
	}
	//@RequestMapping(value = Constants.WL_POST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = Constants.WL_POST, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse createWatchlist(
			@PathVariable String userId, @RequestBody WatchlistSpec inputSpec) {

		logger.info("******** Received UDR Create request by user =" + userId);
		if (inputSpec == null) {
			throw new CommonServiceException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					String.format(
							CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE,
							"Create Query For Rule", "inputSpec"));
		}
		
		JsonServiceResponse resp = watchlistService.createOrUpdateWatchlist(userId, inputSpec);

		return resp;
	}

	//@RequestMapping(value = Constants.WL_PUT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = Constants.WL_PUT, method = RequestMethod.PUT,  produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse updateUDR(
			@PathVariable String userId, @RequestBody WatchlistSpec inputSpec) {
		logger.info("******** Received UDR Update request by user =" + userId);
						
		JsonServiceResponse resp = watchlistService.createOrUpdateWatchlist(userId, inputSpec);

		return resp;
	}

	@RequestMapping(value = Constants.WL_DELETE, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse deleteUDR(@PathVariable String name) {
		logger.info("******** Received UDR Delete requestfor watch list =" + name);
		JsonServiceResponse resp = watchlistService.deleteWatchlist(name);
		return resp;
	}

	@RequestMapping(value = Constants.WL_COMPILE, method = {RequestMethod.POST, RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse compileWatchlists(){
		return watchlistService.activateAllWatchlists();
	}

    /**
     * 
     * @return
     */
	@RequestMapping(value = Constants.WL_TEST, method = RequestMethod.GET)
	public WatchlistSpec getTestWatchlist() {
		WatchlistSpec resp = SampleDataGenerator.createSampleWatchlist("TestWatchlist");
		return resp;
	}

}
