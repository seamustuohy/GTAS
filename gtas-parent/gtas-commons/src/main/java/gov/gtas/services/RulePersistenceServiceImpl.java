package gov.gtas.services;

import gov.gtas.error.BasicErrorHandler;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.model.User;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.YesNoEnum;
import gov.gtas.repository.RuleRepository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		RuleMeta savedMeta = r.getMetaData();
		r.setEditDt(new Date());
		r.setEditedBy(user);
		r.setMetaData(null);
		Rule rule = ruleRepository.save(r);
		if(savedMeta != null){
			savedMeta.setId(rule.getId());
			rule.setMetaData(savedMeta);
			savedMeta.setParent(rule);
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
