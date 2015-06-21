package gov.gtas.services.udr;

import gov.gtas.error.BasicErrorHandler;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.model.User;
import gov.gtas.model.udr.CondValue;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.YesNoEnum;
import gov.gtas.repository.udr.RuleRepository;
import gov.gtas.services.UserService;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class RulePersistenceServiceImpl implements RulePersistenceService {
    @Resource
    private RuleRepository ruleRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BasicErrorHandler errorHandler;
    
	@Override
	@Transactional
	public Rule create(Rule r, String userId) {
		final User user = userService.findById(userId);
		if(user == null){
			throw errorHandler.createException(CommonErrorConstants.INVALID_USER_ID, userId);
		}
		// save meta and rule conditions for now
		//we will add them after saving the bare rule first.
		RuleMeta savedMeta = r.getMetaData();
		List<RuleCond> savedCondList = r.getRuleConds();
		
		r.setEditDt(new Date());
		r.setEditedBy(user);
		r.setMetaData(null);
		r.removeAllConditions();
		
		//save the rule with the meta data and conditions stripped.
		//Once the rule id is generated we will add back the meta and conditions
		//and set their composite keys with the rule ID.
		Rule rule = ruleRepository.save(r);
		
		//now add back the meta and conditions and update the rule.
		if(savedMeta != null || !CollectionUtils.isEmpty(savedCondList)){
			long ruleid = rule.getId();
			if(savedMeta != null){
				savedMeta.setId(ruleid);
				rule.setMetaData(savedMeta);
				savedMeta.setParent(rule);
			}
			if(!CollectionUtils.isEmpty(savedCondList)){
				for(RuleCond rc : savedCondList) {
					rc.refreshParentRuleId(ruleid);
					rule.addConditionToRule(rc);
				}				
			}
			rule = ruleRepository.save(rule);
		}
		return rule;
	}

	@Override
	@Transactional
	public Rule delete(Long id, String userId) {
		final User user = userService.findById(userId);
		if(user == null){
			throw errorHandler.createException(CommonErrorConstants.INVALID_USER_ID, userId);
		}
		Rule ruleToDelete = ruleRepository.findOne(id);
		if(ruleToDelete != null){
			ruleToDelete.setDeleted(YesNoEnum.Y);
			ruleToDelete.setEditedBy(user);
			ruleToDelete.setEditDt(new Date());
			ruleRepository.save(ruleToDelete);
		}
		return ruleToDelete;
	}
	
	@Override
	@Transactional(value=TxType.SUPPORTS)
	public List<Rule> findAll() {
		return (List<Rule>)ruleRepository.findByDeleted(YesNoEnum.N);				
	}

	@Override
	@Transactional
	public Rule update(Rule rule, String userId) {
		final User user = userService.findById(userId);
		if(user == null){
			throw errorHandler.createException(CommonErrorConstants.INVALID_USER_ID, userId);
		}
		rule.setEditDt(new Date()); 
		rule.setEditedBy(user);
		ruleRepository.save(rule);
		return rule;
	}

	@Override
	public Rule findById(Long id) {
		return ruleRepository.findOne(id);
	}

}
