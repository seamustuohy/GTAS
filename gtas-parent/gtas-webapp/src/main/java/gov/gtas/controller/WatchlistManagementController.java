package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonServiceException;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.json.Watchlist;
import gov.gtas.model.watchlist.util.WatchlistBuilder;
import gov.gtas.svc.RuleManagementService;
import gov.gtas.svc.WatchlistService;

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

	@RequestMapping(value = Constants.WL_GET_BY_NAME, method = RequestMethod.GET)
	public Watchlist getWatchlist(@PathVariable String name) {
		System.out.println("******** name =" + name);
		Watchlist resp = watchlistService.fetchWatchlist(name);
		return resp;
	}


//	@RequestMapping(value = Constants.UDR_GETDRL, method = RequestMethod.GET)
//	public JsonServiceResponse getDrl() {
//		String rules = ruleManagementService.fetchDefaultDrlRulesFromKnowledgeBase();
//		return createDrlRulesResponse(rules);
//	}
	
	/**
	 * Creates the DRL rule response JSON object.
	 * @param rules the DRL rules.
	 * @return the JSON response object containing the rules.
	 */
//	private JsonServiceResponse createDrlRulesResponse(String rules){
//		System.out.println("******* The rules:\n"+rules+"\n***************\n");
//		JsonServiceResponse resp = new JsonServiceResponse(JsonServiceResponse.SUCCESS_RESPONSE, 
//				"Rule Management Service", "fetchDefaultDrlRulesFromKnowledgeBase", "Drools rules fetched successsfully");
//		String[] lines = rules.split("\n");
//		resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute("DRL Rules", lines));
//		return resp;
//	}
	@RequestMapping(value = Constants.WL_POST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse createWatchlist(
			@PathVariable String userId, @RequestBody Watchlist inputSpec) {

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

	@RequestMapping(value = Constants.WL_PUT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse updateUDR(
			@PathVariable String userId, @RequestBody Watchlist inputSpec) {
		logger.info("******** Received UDR Update request by user =" + userId);
						
		JsonServiceResponse resp = watchlistService.createOrUpdateWatchlist(userId, inputSpec);

		return resp;
	}

    /**
     * 
     * @return
     */
	@RequestMapping(value = Constants.WL_TEST, method = RequestMethod.GET)
	public Watchlist getTestWatchlist() {
		Watchlist resp = WatchlistBuilder.createSampleWatchlist();
		return resp;
	}

}
