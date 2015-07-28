package gov.gtas.services.udr;

import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.User;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.UdrConstants;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.YesNoEnum;
import gov.gtas.repository.udr.UdrRuleRepository;
import gov.gtas.services.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * The back-end service for persisting rules.
 * @author GTAS3 (AB)
 *
 */
@Service
public class RulePersistenceServiceImpl implements RulePersistenceService {
	/*
	 * The logger for the RulePersistenceService.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(RulePersistenceServiceImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Resource
    private UdrRuleRepository udrRuleRepository;
    
    @Autowired
    private UserService userService;
    
	@Override
	@Transactional
	public UdrRule create(UdrRule r, String userId) {
		final User user = userService.findById(userId);
		if(user == null){
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
		}
		// save meta and rule conditions for now
		//we will add them after saving the UDR rule and its child Drools rules first.
		RuleMeta savedMeta = r.getMetaData();
		Map<Integer, List<RuleCond>> ruleConditionMap = null;
		if(r.getEngineRules() != null){
			ruleConditionMap = saveEngineRuleConditions(r);
		}
		
		r.setEditDt(new Date());
		r.setAuthor(user);
		r.setEditedBy(user);
		r.setMetaData(null);
		
		//save the rule with the meta data and conditions stripped.
		//Once the rule id is generated we will add back the meta and conditions
		//and set their composite keys with the rule ID.
		UdrRule rule = udrRuleRepository.save(r);
		
		//now add back the meta and conditions and update the rule.
		if(savedMeta != null || ruleConditionMap != null){
			long ruleid = rule.getId();
			if(savedMeta != null){
				savedMeta.setId(ruleid);
				rule.setMetaData(savedMeta);
				savedMeta.setParent(rule);
			}
			if(ruleConditionMap != null){
				for(Rule engineRule: rule.getEngineRules()){
					for(RuleCond rc : ruleConditionMap.get(engineRule.getRuleIndex())) {
						rc.refreshParentRuleId(engineRule.getId());
						engineRule.addConditionToRule(rc);
					}	
				}
			}
			rule = udrRuleRepository.save(rule);
		}
		return rule;
	}
    private Map<Integer, List<RuleCond>> saveEngineRuleConditions(UdrRule udrRule){
    	Map<Integer, List<RuleCond>> ruleConditionMap = new HashMap<Integer, List<RuleCond>>();
    	for(Rule r: udrRule.getEngineRules()){
    		ruleConditionMap.put(r.getRuleIndex() ,r.getRuleConds());
		    r.removeAllConditions();
    	}
    	return ruleConditionMap;
    }
    private Map<Integer, List<RuleCond>> saveEngineRuleConditions(List<Rule> engineRules){
    	Map<Integer, List<RuleCond>> ruleConditionMap = new HashMap<Integer, List<RuleCond>>();
    	for(Rule r: engineRules){
    		ruleConditionMap.put(r.getRuleIndex() ,r.getRuleConds());
		    r.removeAllConditions();
    	}
    	return ruleConditionMap;
    }
	@Override
	@Transactional
	public UdrRule delete(Long id, String userId) {
		final User user = userService.findById(userId);
		if(user == null){
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
		}
		UdrRule ruleToDelete = udrRuleRepository.findOne(id);
		if(ruleToDelete != null){
			ruleToDelete.setDeleted(YesNoEnum.Y);
			RuleMeta meta = ruleToDelete.getMetaData();
			meta.setEnabled(YesNoEnum.N);
			ruleToDelete.setEditedBy(user);
			ruleToDelete.setEditDt(new Date());
			udrRuleRepository.save(ruleToDelete);
		}else{
			logger.warn("RulePersistenceServiceImpl.delete() - object does not exist:"+id);
		}
		return ruleToDelete;
	}
	
	@Override
	@Transactional(value=TxType.SUPPORTS)
	public List<UdrRule> findAll() {
		return (List<UdrRule>)udrRuleRepository.findByDeleted(YesNoEnum.N);				
	}
	@Override
	@Transactional
	public UdrRule update(UdrRule rule, List<Rule> newEngineRules, String userId) {
		final User user = userService.findById(userId);
		if(user == null){
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
		}
		if(rule.getId() == null){
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, "id", "Update UDR");
		}
		
		rule.setEditDt(new Date()); 
		rule.setEditedBy(user);
		if(newEngineRules != null){
			rule.clearEngineRules();
		}
		udrRuleRepository.save(rule);
        UdrRule updatedRule = udrRuleRepository.findOne(rule.getId());
        
		if(newEngineRules != null){
			batchInsertEngineRules(updatedRule, newEngineRules);
		}
		return updatedRule;
	}
	private void batchInsertEngineRules(UdrRule parent, List<Rule> newEngineRules){
		Map<Integer, List<RuleCond>> ruleConditionMap = saveEngineRuleConditions(newEngineRules);
		for(Rule r:newEngineRules){
			r.setParent(parent);
			entityManager.persist(r);
		}
		entityManager.flush();
		entityManager.clear();
		for(Rule engineRule: newEngineRules){
			for(RuleCond rc : ruleConditionMap.get(engineRule.getRuleIndex())) {
				rc.refreshParentRuleId(engineRule.getId());
				engineRule.addConditionToRule(rc);
			}
			entityManager.merge(engineRule);
		}
		entityManager.flush();
		entityManager.clear();
		
	}
	@Override
	@Transactional(TxType.SUPPORTS)
	public UdrRule findById(Long id) {
		return udrRuleRepository.findOne(id);
	}
	/* (non-Javadoc)
	 * @see gov.gtas.services.udr.RulePersistenceService#findByTitleAndAuthor(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(TxType.SUPPORTS)
	public UdrRule findByTitleAndAuthor(String title, String authorUserId) {
		return udrRuleRepository.getUdrRuleByTitleAndAuthor(title, authorUserId);
	}
	/* (non-Javadoc)
	 * @see gov.gtas.services.udr.RulePersistenceService#findByAuthor(java.lang.String)
	 */
	@Override
	public List<UdrRule> findByAuthor(String authorUserId) {
		return udrRuleRepository.getUdrRuleByAuthor(authorUserId);
	}
	/* (non-Javadoc)
	 * @see gov.gtas.services.udr.RulePersistenceService#findDefaultKnowledgeBase()
	 */
	@Override
	public KnowledgeBase findUdrKnowledgeBase() {
		return udrRuleRepository.getKnowledgeBaseByName(UdrConstants.UDR_KNOWLEDGE_BASE_NAME);
	}
	
	/* (non-Javadoc)
	 * @see gov.gtas.services.udr.RulePersistenceService#findUdrKnowledgeBase(java.lang.String)
	 */
	@Override
	public KnowledgeBase findUdrKnowledgeBase(String kbName) {
		return udrRuleRepository.getKnowledgeBaseByName(kbName);
	}
	/* (non-Javadoc)
	 * @see gov.gtas.services.udr.RulePersistenceService#saveKnowledgeBase(gov.gtas.model.udr.KnowledgeBase)
	 */
	@Override
	public KnowledgeBase saveKnowledgeBase(KnowledgeBase kb) {
		kb.setCreationDt(new Date());
		if(kb.getId() == null){
		  entityManager.persist(kb);
		} else {
			entityManager.merge(kb);
		}
		return kb;
	}
	/* (non-Javadoc)
	 * @see gov.gtas.services.udr.RulePersistenceService#deleteKnowledgeBase(java.lang.String)
	 */
	@Override
	public KnowledgeBase deleteKnowledgeBase(String kbName) {
		KnowledgeBase kb = findUdrKnowledgeBase(kbName);
		if(kb != null){
			entityManager.remove(kb);
		}
		return kb;
	}
	
}
