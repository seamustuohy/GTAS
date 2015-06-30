package gov.gtas.controller;

//import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.constants.Constants;
import gov.gtas.error.BasicErrorHandler;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonServiceException;
import gov.gtas.model.User;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.udr.json.JsonUdrListElement;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.error.GtasJsonError;
import gov.gtas.model.udr.json.util.JsonToDomainObjectConverter;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import gov.gtas.rule.RuleServiceResult;

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
			.getLogger(BasicErrorHandler.class);

	@Autowired
	UserService userService;

	@Autowired
	private RulePersistenceService rulePersistenceService;

	@RequestMapping(value = Constants.UDR_GET, method = RequestMethod.GET)
	public @ResponseBody UdrSpecification getUDR(@PathVariable String userId,
			@PathVariable String title) {
		System.out.println("******** user =" + userId + ", title=" + title);
		UdrSpecification resp = getUdrRuleSpecs(userId, title);
		return resp;
	}

	private UdrSpecification getUdrRuleSpecs(String userId, String title) {
		UdrRule fetchedRule = rulePersistenceService.findByTitleAndAuthor(
				title, userId);
		if (fetchedRule == null) {
			throw new CommonServiceException(
					CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE,
					String.format(
							CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_MESSAGE,
							"UDR", "title=" + title));
		}
		UdrSpecification jsonObject = null;
		try {
			jsonObject = JsonToDomainObjectConverter
					.getJsonFromUdrRule(fetchedRule);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		return jsonObject;
	}

	@RequestMapping(value = Constants.UDR_GETALL, method = RequestMethod.GET)
	public @ResponseBody List<JsonUdrListElement> getUDRList(
			@PathVariable String userId) {
		System.out.println("******** user =" + userId);
		List<JsonUdrListElement> resp = getUdrList(userId);
		return resp;
	}

	private List<JsonUdrListElement> getUdrList(String userId) {
		List<UdrRule> fetchedRuleList = rulePersistenceService.findAll();
		if (fetchedRuleList == null || fetchedRuleList.isEmpty()) {
			throw new CommonServiceException(
					CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE,
					String.format(
							CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_MESSAGE,
							"UDR List", "userId=" + userId));
		}
		List<JsonUdrListElement> ret = new LinkedList<JsonUdrListElement>();
		try{
		for (UdrRule rule : fetchedRuleList) {
			if(rule.getUdrConditionObject() != null){
				ret.add(new JsonUdrListElement(rule.getId(),
						JsonToDomainObjectConverter.getJsonFromUdrRule(rule)
								.getSummary()));
			}
		}
		}catch(ClassNotFoundException | IOException ex){
			throw new RuntimeException("Error in getUdrList", ex);
		}
		return ret;
	}

	@RequestMapping(value = Constants.UDR_POST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JsonServiceResponse createUDR(
			@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {
		System.out.println("******** user =" + userId);
		if (inputSpec != null) {
			QueryObject queryObject = inputSpec.getDetails();
			logger.info("******** condition =" + queryObject.getCondition());
			List<QueryEntity> rules = queryObject.getRules();
			logger.info("******** rule count =" + rules.size());
			QueryTerm trm = (QueryTerm) rules.get(0);
			logger.info("******** entity =" + trm.getEntity());
		} else {
			throw new CommonServiceException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					String.format(
							CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE,
							"Create Query For Rule", "inputSpec"));
		}
		UdrRule createdRule = null;
		try {
			createdRule = createUdrRule(userId, inputSpec);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe.getMessage());
		}
		JsonServiceResponse resp = new JsonServiceResponse(
				"SUCCESS",
				"UDR Management Service",
				"Create UDR",
				String.format(
						"UDR Rule with title='%s' was saved with ID='%s' for author:'%s'.",
						inputSpec.getSummary().getTitle(), createdRule.getId(),
						inputSpec.getSummary().getAuthor()));
		return resp;
	}

	private UdrRule createUdrRule(String userId, UdrSpecification querySpec)
			throws IOException {
		// get the author object
		String authorUserId = querySpec.getSummary().getAuthor();
		if (StringUtils.isEmpty(authorUserId)) {
			authorUserId = userId;
		}
		User author = userService.findById(authorUserId);
		UdrRule ruleToSave = JsonToDomainObjectConverter.createUdrRuleFromJson(
				querySpec, author);
		UdrRule savedRule = rulePersistenceService.create(ruleToSave, userId);
		return savedRule;
	}

	private UdrRule updateUdrRule(String userId, UdrSpecification querySpec)
			throws IOException {
		// get the author object
		String authorUserId = querySpec.getSummary().getAuthor();
		if (StringUtils.isEmpty(authorUserId)) {
			authorUserId = userId;
		}
		User author = userService.findById(authorUserId);
		UdrRule ruleToUpdate = JsonToDomainObjectConverter
				.createUdrRuleFromJson(querySpec, author);
		UdrRule updatedRule = rulePersistenceService.update(ruleToUpdate,
				userId);
		return updatedRule;
	}

	@RequestMapping(value = Constants.UDR_PUT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JsonServiceResponse updateUDR(
			@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {
		logger.info("******** user =" + userId);
		if (inputSpec == null) {
			throw new CommonServiceException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					String.format(
							CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE,
							"Create Query For Rule", "inputSpec"));
		}
		UdrRule updatedRule = null;
		try {
			updatedRule = updateUdrRule(userId, inputSpec);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe.getMessage());
		}
		JsonServiceResponse resp = new JsonServiceResponse("SUCCESS",
				"UDR Management Service", "Update UDR", String.format(
						"UDR Rule with title='%s' and ID='%s' was updated.",
						inputSpec.getSummary().getTitle(), updatedRule.getId()));
		return resp;
	}

	@RequestMapping(value = Constants.UDR_TEST, method = RequestMethod.GET)
	public @ResponseBody UdrSpecification getUDR() {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate", "Date",
				"EQUAL", new String[] { new Date().toString() });
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "String", "EQUAL",
				new String[] { "Jones" }));

		QueryObject queryObjectEmbedded = new QueryObject();
		queryObjectEmbedded.setCondition("AND");
		List<QueryEntity> rules2 = new LinkedList<QueryEntity>();
		QueryTerm trm2 = new QueryTerm("Pax", "embarkation.name", "String",
				"IN", new String[] { "DBY", "PKY", "FLT" });
		rules2.add(trm2);
		rules2.add(new QueryTerm("Pax", "debarkation.name", "String", "EQUAL",
				new String[] { "IAD" }));
		queryObjectEmbedded.setRules(rules2);

		rules.add(queryObjectEmbedded);

		queryObject.setRules(rules);

		UdrSpecification resp = new UdrSpecification(null, queryObject,
				new MetaData("Hello Rule 1", "This is a test", new Date(),
						"jpjones"));
		return resp;
	}

	@ExceptionHandler(CommonServiceException.class)
	public @ResponseBody GtasJsonError handleError(CommonServiceException ex) {
		return new GtasJsonError(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody GtasJsonError handleError(Exception ex) {
		logger.error(ex.getMessage());
		return new GtasJsonError(CommonErrorConstants.SYSTEM_ERROR_CODE,
				String.format(CommonErrorConstants.SYSTEM_ERROR_MESSAGE,
						System.currentTimeMillis()));
	}
}
