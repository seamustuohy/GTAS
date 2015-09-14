package gov.gtas.svc;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.RuleConstants;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.error.UdrServiceErrorHandler;
import gov.gtas.json.JsonServiceResponse;
import gov.gtas.model.User;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.JsonUdrListElement;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.util.JsonToDomainObjectConverter;
import gov.gtas.services.security.UserData;
import gov.gtas.services.security.UserService;
import gov.gtas.services.security.UserServiceUtil;
import gov.gtas.services.udr.RulePersistenceService;
import gov.gtas.svc.util.UdrServiceHelper;
import gov.gtas.svc.util.UdrServiceJsonResponseHelper;

/**
 * Implementation of the UDR Service API.
 * 
 * @author GTAS3 (AB)
 *
 */
@Service
public class UdrServiceImpl implements UdrService {
	private static Logger logger = LoggerFactory.getLogger(UdrServiceImpl.class);

	@Autowired
	private RulePersistenceService rulePersistenceService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserServiceUtil userServiceUtil;

	@Autowired
	private RuleManagementService ruleManagementService;

	@PostConstruct
	private void initializeErrorHandler() {
		ErrorHandler errorHandler = new UdrServiceErrorHandler();
		ErrorHandlerFactory.registerErrorHandler(errorHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#fetchUdr(java.lang.String, java.lang.String)
	 */
	@Override
	public UdrSpecification fetchUdr(String userId, String title) {
		UdrRule fetchedRule = rulePersistenceService.findByTitleAndAuthor(title, userId);
		if (fetchedRule == null) {
			throw ErrorHandlerFactory.getErrorHandler()
					.createException(CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE, "UDR", "title=" + title);
		}
		UdrSpecification jsonObject = null;
		try {
			jsonObject = JsonToDomainObjectConverter.getJsonFromUdrRule(fetchedRule);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		return jsonObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#fetchUdr(java.lang.Long)
	 */
	@Override
	public UdrSpecification fetchUdr(Long id) {
		UdrRule fetchedRule = rulePersistenceService.findById(id);
		if (fetchedRule == null) {
			throw ErrorHandlerFactory.getErrorHandler()
					.createException(CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE, "UDR", "id=" + id);
		}
		UdrSpecification jsonObject = null;
		try {
			jsonObject = JsonToDomainObjectConverter.getJsonFromUdrRule(fetchedRule);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		return jsonObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#fetchUdrSummaryList(java.lang.String)
	 */
	@Override
	public List<JsonUdrListElement> fetchUdrSummaryList(String userId) {
		List<UdrRule> fetchedRuleList = rulePersistenceService.findByAuthor(userId);
		if (fetchedRuleList == null || fetchedRuleList.isEmpty()) {
			return new LinkedList<JsonUdrListElement>();
		}
		List<JsonUdrListElement> ret = new LinkedList<JsonUdrListElement>();
		try {
			for (UdrRule rule : fetchedRuleList) {
				if (rule.getUdrConditionObject() != null) {
					ret.add(new JsonUdrListElement(rule.getId(),
							JsonToDomainObjectConverter.getJsonFromUdrRule(rule).getSummary()));
				}
			}
		} catch (ClassNotFoundException | IOException ex) {
			throw new RuntimeException("Error in getUdrList", ex);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#createUdr(gov.gtas.model.udr.json.
	 * UdrSpecification )
	 */
	@Override
	@Transactional
	public JsonServiceResponse createUdr(String userId, UdrSpecification udrToCreate) {
		if (udrToCreate == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"udrToCreate", "Create UDR");
		}
		MetaData meta = udrToCreate.getSummary();
		if (meta == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"summary", "Create UDR");
		}
		// get the author object
		String authorUserId = meta.getAuthor();
		User author = fetchRuleAuthor(userId, authorUserId);

		UdrRule ruleToSave = null;
		try {
			ruleToSave = JsonToDomainObjectConverter.createUdrRuleFromJson(udrToCreate, author);
			UdrServiceHelper.addEngineRulesToUdrRule(ruleToSave, udrToCreate);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe.getMessage());
		}

		UdrRule savedRule = rulePersistenceService.create(ruleToSave, userId);

		List<UdrRule> ruleList = rulePersistenceService.findAll();
		ruleManagementService.createKnowledgeBaseFromUdrRules(RuleConstants.UDR_KNOWLEDGE_BASE_NAME, ruleList, userId);

		return UdrServiceJsonResponseHelper.createResponse(true, RuleConstants.UDR_CREATE_OP_NAME, savedRule);
	}

	private User fetchRuleAuthor(final String userId, final String authorUserId) {
		String authorId = authorUserId;
		if (StringUtils.isEmpty(authorId)) {
			authorId = userId;
		}
		UserData user = userService.findById(authorId);
		if (user == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, authorId);
		}
		return userServiceUtil.mapUserEntityFromUserData(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#updateUdr(gov.gtas.model.udr.json.
	 * UdrSpecification )
	 */
	@Override
	@Transactional
	public JsonServiceResponse updateUdr(String userId, UdrSpecification udrToUpdate) {
		if (udrToUpdate == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"udrToUpdate", "Update UDR");
		}
		Long id = udrToUpdate.getId();
		if (id == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"id", "Update UDR");
		}
		MetaData meta = udrToUpdate.getSummary();
		if (meta == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"udrToUpdate.summary", "Update UDR");
		}
		// get the author object
		String authorUserId = meta.getAuthor();
		// fetch the UdrRule
		UdrRule ruleToUpdate = rulePersistenceService.findById(id);
		if (ruleToUpdate == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_CODE, udrToUpdate.getSummary().getTitle(), userId,
					id);
		}
		/*
		 * check if the user has permission to update the UDR
		 */
		if (!ruleToUpdate.getAuthor().getUserId().equals(authorUserId)) {
			// TODO throw exception here
			logger.error(String.format("UdrServiceImpl.updateUdr() - %s trying to update rule by different author %s!",
					authorUserId, ruleToUpdate.getAuthor().getUserId()));
		}
		// update the meta data
		RuleMeta ruleMeta = JsonToDomainObjectConverter.extractRuleMetaUpdates(udrToUpdate);
		ruleToUpdate.setMetaData(ruleMeta);
		ruleToUpdate.setTitle(ruleMeta.getTitle());

		UdrRule updatedRule = null;

		QueryObject queryObject = udrToUpdate.getDetails();
		if (queryObject != null) {
			try {
				final byte[] ruleBlob = JsonToDomainObjectConverter.convertQueryObjectToBlob(queryObject);
				ruleToUpdate.setUdrConditionObject(ruleBlob);
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getMessage());
			}

			// update the engine rules
			List<Rule> newEngineRules = UdrServiceHelper.listEngineRules(ruleToUpdate, udrToUpdate);

			ruleToUpdate.clearEngineRules();
			for (Rule r : newEngineRules) {
				ruleToUpdate.addEngineRule(r);
			}

			updatedRule = rulePersistenceService.update(ruleToUpdate, userId);

			List<UdrRule> ruleList = rulePersistenceService.findAll();
			ruleManagementService.createKnowledgeBaseFromUdrRules(RuleConstants.UDR_KNOWLEDGE_BASE_NAME, ruleList,
					userId);

		} else {
			// simple update - meta data only
			// no need to re-generate the Knowledge Base.
			updatedRule = rulePersistenceService.update(ruleToUpdate, userId);
		}

		return UdrServiceJsonResponseHelper.createResponse(true, RuleConstants.UDR_UPDATE_OP_NAME, updatedRule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#deleteUdr(java.lang.Long)
	 */
	@Override
	@Transactional
	public JsonServiceResponse deleteUdr(String userId, Long id) {
		UdrRule deletedRule = rulePersistenceService.delete(id, userId);
		if (deletedRule != null) {
			List<UdrRule> ruleList = rulePersistenceService.findAll();
			if (!CollectionUtils.isEmpty(ruleList)) {
				ruleManagementService.createKnowledgeBaseFromUdrRules(RuleConstants.UDR_KNOWLEDGE_BASE_NAME, ruleList,
						userId);
			} else {
				ruleManagementService.deleteKnowledgeBase(RuleConstants.UDR_KNOWLEDGE_BASE_NAME);
			}
			return UdrServiceJsonResponseHelper.createResponse(true, RuleConstants.UDR_DELETE_OP_NAME, deletedRule);
		} else {
			return UdrServiceJsonResponseHelper.createResponse(false, RuleConstants.UDR_DELETE_OP_NAME, deletedRule,
					"since it does not exist or has been deleted previously");
		}
	}

}
