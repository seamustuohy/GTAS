package gov.gtas.svc;

import static gov.gtas.constant.AuditLogConstants.UDR_LOG_CREATE_MESSAGE;
import static gov.gtas.constant.AuditLogConstants.UDR_LOG_DELETE_MESSAGE;
import static gov.gtas.constant.AuditLogConstants.UDR_LOG_TARGET_PREFIX;
import static gov.gtas.constant.AuditLogConstants.UDR_LOG_TARGET_SUFFIX;
import static gov.gtas.constant.AuditLogConstants.UDR_LOG_UPDATE_MESSAGE;
import static gov.gtas.constant.AuditLogConstants.UDR_LOG_UPDATE_META_MESSAGE;
import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.RuleConstants;
import gov.gtas.enumtype.AuditActionType;
import gov.gtas.enumtype.YesNoEnum;
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
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.services.AuditLogPersistenceService;
import gov.gtas.services.security.UserData;
import gov.gtas.services.security.UserService;
import gov.gtas.services.security.UserServiceUtil;
import gov.gtas.services.udr.RulePersistenceService;
import gov.gtas.svc.util.UdrServiceHelper;
import gov.gtas.svc.util.UdrServiceJsonResponseHelper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of the UDR Service API.
 * 
 * @author GTAS3 (AB)
 *
 */
@Service
public class UdrServiceImpl implements UdrService {
	private static Logger logger = LoggerFactory
			.getLogger(UdrServiceImpl.class);

	@Autowired
	private RulePersistenceService rulePersistenceService;

	@Resource
	private HitsSummaryRepository hitSummaryRepository;

	@Autowired
	private AuditLogPersistenceService auditLogPersistenceService;

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
		UdrRule fetchedRule = rulePersistenceService.findByTitleAndAuthor(
				title, userId);
		if (fetchedRule == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE, "UDR",
					"title=" + title);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#fetchUdr(java.lang.Long)
	 */
	@Override
	public UdrSpecification fetchUdr(Long id) {
		UdrRule fetchedRule = rulePersistenceService.findById(id);
		if (fetchedRule == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE, "UDR",
					"id=" + id);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#fetchUdrSummaryList(java.lang.String)
	 */
	@Override
	public List<JsonUdrListElement> fetchUdrSummaryList(String userId) {
		return convertSummaryList(rulePersistenceService
				.findAllUdrSummary(userId));
	}

	/**
	 * Converts the UDR query data into a summary list.
	 * 
	 * @param fetchedRuleList
	 *            the query data.
	 * @return summary list.
	 */
	private List<JsonUdrListElement> convertSummaryList(
			List<Object[]> fetchedRuleList) {
		Map<Long, Long> udrHitCountMap = createUdrHitCountMap();
		List<JsonUdrListElement> ret = new LinkedList<JsonUdrListElement>();
		if (fetchedRuleList != null && !fetchedRuleList.isEmpty()) {
			for (Object[] data : fetchedRuleList) {
				String editedBy = (String) data[1];
				Date editedOn = (Date) data[2];
				String authorUserId = (String) data[8];
				final MetaData meta = new MetaData((String) data[3],
						(String) data[4], (Date) data[5], authorUserId);

				meta.setEnabled((YesNoEnum) data[6] == YesNoEnum.Y ? true
						: false);
				meta.setEndDate((Date) data[7]);
				Long udrId = (Long) data[0];
				JsonUdrListElement item = new JsonUdrListElement(udrId,
						editedBy, editedOn, meta);
				Long hitCount = udrHitCountMap.get(udrId);
				if (hitCount != null) {
					item.setHitCount(hitCount.intValue());
				}
				ret.add(item);
			}
		}
		return ret;
	}

	private Map<Long, Long> createUdrHitCountMap() {
		List<Object[]> udrCounts = hitSummaryRepository.findDetailsByUdr();
		Map<Long, Long> hitCountMap = new HashMap<Long, Long>();
		if (!CollectionUtils.isEmpty(udrCounts)) {
			for (Object[] data : udrCounts) {
				/*
				 * map key is UDR ID map value is hit count
				 */
				hitCountMap.put((Long) data[0], (Long) data[1]);
			}
		}
		return hitCountMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#fetchUdrSummaryList()
	 */
	@Override
	public List<JsonUdrListElement> fetchUdrSummaryList() {
		return convertSummaryList(rulePersistenceService
				.findAllUdrSummary(null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.UdrService#createUdr(gov.gtas.model.udr.json.
	 * UdrSpecification )
	 */
	@Override
	@Transactional
	public JsonServiceResponse createUdr(String userId,
			UdrSpecification udrToCreate) {
		if (udrToCreate == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"udrToCreate", "Create UDR");
		}
		MetaData meta = udrToCreate.getSummary();
		if (meta == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, "summary",
					"Create UDR");
		}
		// get the author object
		String authorUserId = meta.getAuthor();
		User author = fetchRuleAuthor(userId, authorUserId);

		UdrRule ruleToSave = null;
		try {
			ruleToSave = JsonToDomainObjectConverter.createUdrRuleFromJson(
					udrToCreate, author);
			UdrServiceHelper.addEngineRulesToUdrRule(ruleToSave, udrToCreate);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe.getMessage());
		}

		UdrRule savedRule = rulePersistenceService.create(ruleToSave, userId);
		recompileRules(RuleConstants.UDR_KNOWLEDGE_BASE_NAME, userId);
		writeAuditLog(AuditActionType.CREATE_UDR, savedRule, udrToCreate,
				userId, author);
		return UdrServiceJsonResponseHelper.createResponse(true,
				RuleConstants.UDR_CREATE_OP_NAME, savedRule);
	}

	/**
	 * Fetches all the active rules and recompiles the Knowledge Base. If no
	 * rules are found then the knowledge Base is deleted.
	 * 
	 * @param kbName
	 * @param userId
	 */
	private void recompileRules(final String kbName, String userId) {
		List<UdrRule> ruleList = rulePersistenceService.findAll();
		if (!CollectionUtils.isEmpty(ruleList)) {
			ruleManagementService.createKnowledgeBaseFromUdrRules(kbName,
					ruleList, userId);
		} else {
			ruleManagementService.deleteKnowledgeBase(kbName);
			logger.warn("UdrService - no active rules -> deleting Knowledge Base!");
		}
	}

	private User fetchRuleAuthor(final String userId, final String authorUserId) {
		String authorId = authorUserId;
		if (StringUtils.isEmpty(authorId)) {
			authorId = userId;
		}
		return fetchUser(authorId);
	}

	private User fetchUser(final String userId) {
		UserData user = userService.findById(userId);
		if (user == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
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
	public JsonServiceResponse updateUdr(String userId,
			UdrSpecification udrToUpdate) {
		if (udrToUpdate == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"udrToUpdate", "Update UDR");
		}
		Long id = udrToUpdate.getId();
		if (id == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, "id",
					"Update UDR");
		}
		MetaData meta = udrToUpdate.getSummary();
		if (meta == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"udrToUpdate.summary", "Update UDR");
		}
		// get the author object
		String authorUserId = meta.getAuthor();
		// fetch the UdrRule
		UdrRule ruleToUpdate = rulePersistenceService.findById(id);
		if (ruleToUpdate == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.UPDATE_RECORD_MISSING_ERROR_CODE,
					udrToUpdate.getSummary().getTitle(), userId, id);
		}
		/*
		 * check if the user has permission to update the UDR
		 */
		User author = ruleToUpdate.getAuthor();
		if (!author.getUserId().equals(userId)) {
			// TODO check if the user is admin
			// else throw exception
			logger.error(String
					.format("UdrServiceImpl.updateUdr() - %s trying to update rule by different author %s!",
							authorUserId, ruleToUpdate.getAuthor().getUserId()));
		}
		// update the meta data
		RuleMeta ruleMeta = JsonToDomainObjectConverter
				.extractRuleMetaUpdates(udrToUpdate);
		ruleToUpdate.setMetaData(ruleMeta);
		ruleToUpdate.setTitle(ruleMeta.getTitle());

		UdrRule updatedRule = null;

		QueryObject queryObject = udrToUpdate.getDetails();
		if (queryObject != null) {
			try {
				final byte[] ruleBlob = JsonToDomainObjectConverter
						.convertQueryObjectToBlob(queryObject);
				ruleToUpdate.setUdrConditionObject(ruleBlob);
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex.getMessage());
			}

			// update the engine rules
			List<Rule> newEngineRules = UdrServiceHelper.listEngineRules(
					ruleToUpdate, udrToUpdate);

			ruleToUpdate.clearEngineRules();
			for (Rule r : newEngineRules) {
				ruleToUpdate.addEngineRule(r);
			}

			updatedRule = rulePersistenceService.update(ruleToUpdate, userId);

			recompileRules(RuleConstants.UDR_KNOWLEDGE_BASE_NAME, userId);

			writeAuditLog(AuditActionType.UPDATE_UDR, updatedRule, udrToUpdate,
					userId, author);
		} else {
			// simple update - meta data only
			// no need to re-generate the Knowledge Base.
			updatedRule = rulePersistenceService.update(ruleToUpdate, userId);
			writeAuditLog(AuditActionType.UPDATE_UDR_META, ruleToUpdate,
					udrToUpdate, userId, author);
		}

		return UdrServiceJsonResponseHelper.createResponse(true,
				RuleConstants.UDR_UPDATE_OP_NAME, updatedRule);
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
			recompileRules(RuleConstants.UDR_KNOWLEDGE_BASE_NAME, userId);
			writeAuditLog(AuditActionType.DELETE_UDR, deletedRule, null,
					userId, null);
			return UdrServiceJsonResponseHelper.createResponse(true,
					RuleConstants.UDR_DELETE_OP_NAME, deletedRule);
		} else {
			return UdrServiceJsonResponseHelper.createResponse(false,
					RuleConstants.UDR_DELETE_OP_NAME, deletedRule,
					"since it does not exist or has been deleted previously");
		}
	}

	private void writeAuditLog(AuditActionType actionType, UdrRule udr,
			UdrSpecification udrspec, String userId, User author) {
		User user = author;
		if (author == null || !author.getUserId().equals(userId)) {
			user = fetchUser(userId);
		}
		String message = null;
		Object actionData = null;
		switch (actionType) {
		case UPDATE_UDR:
			message = UDR_LOG_UPDATE_MESSAGE;
			actionData = udrspec;
			break;
		case UPDATE_UDR_META:
			message = UDR_LOG_UPDATE_META_MESSAGE;
			actionData = udrspec.getSummary();
			break;
		case CREATE_UDR:
			message = UDR_LOG_CREATE_MESSAGE;
			actionData = udrspec;
			break;
		case DELETE_UDR:
			message = UDR_LOG_DELETE_MESSAGE;
			break;
		default:
			break;

		}
		auditLogPersistenceService.create(actionType, UDR_LOG_TARGET_PREFIX
				+ udr.getTitle() + UDR_LOG_TARGET_SUFFIX, actionData, message,
				user);
	}
}
