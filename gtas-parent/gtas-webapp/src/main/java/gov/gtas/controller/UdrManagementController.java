package gov.gtas.controller;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.constants.Constants;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonServiceException;
import gov.gtas.error.ErrorDetails;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.udr.json.JsonUdrListElement;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.error.GtasJsonError;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;
import gov.gtas.rule.RuleServiceResult;
import gov.gtas.svc.RuleManagementService;
import gov.gtas.svc.TargetingService;
import gov.gtas.svc.UdrService;
import gov.gtas.util.DateCalendarUtils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST service end-point controller for creating and managing user Defined
 * Rules (UDR) for targeting.
 * 
 * @author GTAS3 (AB)
 *
 */
@RestController
@RequestMapping(Constants.UDR_ROOT)
public class UdrManagementController {
	/*
	 * The logger for the UdrManagementController
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(UdrManagementController.class);

	@Autowired
	private UdrService udrService;
	
	@Autowired
	private TargetingService targetingService;

	@Autowired
	private RuleManagementService ruleManagementService;

	@RequestMapping(value = Constants.UDR_GET_BY_AUTHOR_TITLE, method = RequestMethod.GET)
	public UdrSpecification getUDR(@PathVariable String authorId,
			@PathVariable String title) {
		System.out.println("******** user =" + authorId + ", title=" + title);
		UdrSpecification resp = udrService.fetchUdr(authorId, title);
		return resp;
	}

	@RequestMapping(value = Constants.UDR_TARGET, method = RequestMethod.GET)
	public List<?> getTargetingResult(@PathVariable Long id) {
			RuleServiceResult resp = targetingService.analyzeApisMessage(id);
			return resp.getResultList();
	}
	@RequestMapping(value = Constants.UDR_TARGET_ALL_APIS, method = RequestMethod.GET)
	public List<?> getTargetingApisResult() {
		List<RuleHitDetail> ret = targetingService.analyzeLoadedApisMessage();
		return ret;
	}

	@RequestMapping(value = Constants.UDR_GET_BY_ID, method = RequestMethod.GET)
	public UdrSpecification getUDRById(@PathVariable Long id) {
		System.out.println("******** Received GET UDR request for id=" + id);
		UdrSpecification resp = udrService.fetchUdr(id);
		return resp;
	}

	@RequestMapping(value = Constants.UDR_GETALL, method = RequestMethod.GET)
	public List<JsonUdrListElement> getUDRList(
			@PathVariable String userId) {
		System.out.println("******** user =" + userId);
		List<JsonUdrListElement> resp = udrService.fetchUdrSummaryList(userId);
		return resp;
	}

	@RequestMapping(value = Constants.UDR_GETDRL, method = RequestMethod.GET)
	public JsonServiceResponse getDrl() {
		String rules = ruleManagementService.fetchDefaultDrlRulesFromKnowledgeBase();
		return createDrlRulesResponse(rules);
	}
	@RequestMapping(value = Constants.UDR_GETDRL_BY_NAME, method = RequestMethod.GET)
	public JsonServiceResponse getDrlByName(@PathVariable String kbName) {
		String rules = ruleManagementService.fetchDrlRulesFromKnowledgeBase(kbName);
		return createDrlRulesResponse(rules);
	}
	/**
	 * Creates the DRL rule response JSON object.
	 * @param rules the DRL rules.
	 * @return the JSON response object containing the rules.
	 */
	private JsonServiceResponse createDrlRulesResponse(String rules){
		System.out.println("******* The rules:\n"+rules+"\n***************\n");
		JsonServiceResponse resp = new JsonServiceResponse(JsonServiceResponse.SUCCESS_RESPONSE, 
				"Rule Management Service", "fetchDefaultDrlRulesFromKnowledgeBase", "Drools rules fetched successsfully");
		String[] lines = rules.split("\n");
		resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute("DRL Rules", lines));
		return resp;
	}
	@RequestMapping(value = Constants.UDR_POST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse createUDR(
			@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {

		logger.info("******** Received UDR Create request by user =" + userId);
		if (inputSpec == null) {
			throw new CommonServiceException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					String.format(
							CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE,
							"Create Query For Rule", "inputSpec"));
		}
		/*
		 * The Jackson JSON parser assumes that the time zone is GMT if no
		 * offset is explicitly indicated. Thus "2015-07-10" is interpreted as
		 * "2015-07-10T00:00:00" GMT or "2015-07-09T20:00:00" EDT, i.e., the
		 * previous day. The following 3 lines of code reverses this interpretation.
		 */
		MetaData meta = inputSpec.getSummary();
		if(meta != null){
			meta.setStartDate(fixMetaDataDates(meta.getStartDate()));
			meta.setEndDate(fixMetaDataDates(meta.getEndDate()));
		}
		
		JsonServiceResponse resp = udrService.createUdr(userId, inputSpec);

		return resp;
	}

	/**
	 * Subtracts the offset to reverse the interpretation at GMT time.
	 * @param inputUdrdate the date to "fix"
	 * @return the fixed date.
	 */
	private Date fixMetaDataDates(Date inputUdrdate) {
		if (inputUdrdate != null) {
			long offset = DateCalendarUtils
					.calculateOffsetFromGMT(inputUdrdate);
			return new Date(inputUdrdate.getTime() - offset);
		} else {
			return null;
		}

	}

	@RequestMapping(value = Constants.UDR_PUT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse updateUDR(
			@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {
		logger.info("******** Received UDR Update request by user =" + userId);
		
		/*
		 * The Jackson JSON parser assumes that the time zone is GMT if no
		 * offset is explicitly indicated. Thus "2015-07-10" is interpreted as
		 * "2015-07-10T00:00:00" GMT or "2015-07-09T20:00:00" EDT, i.e., the
		 * previous day. The following 3 lines of code reverses this interpretation.
		 */
		MetaData meta = inputSpec.getSummary();
		if(meta != null){
			meta.setStartDate(fixMetaDataDates(meta.getStartDate()));
			meta.setEndDate(fixMetaDataDates(meta.getEndDate()));
		}
				
		JsonServiceResponse resp = udrService.updateUdr(userId, inputSpec);

		return resp;
	}

	@RequestMapping(value = Constants.UDR_DELETE, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonServiceResponse deleteUDR(
			@PathVariable String userId, @PathVariable Long id) {
		logger.info("******** Received UDR Delete request by user =" + userId
				+ " for " + id);
		JsonServiceResponse resp = udrService.deleteUdr(userId, id);

		return resp;
	}
    /**
     * 
     * @return
     */
	@RequestMapping(value = Constants.UDR_TEST, method = RequestMethod.GET)
	public UdrSpecification getUDR() {
		UdrSpecification resp = UdrSpecificationBuilder.createSampleSpec();
		return resp;
	}

}
