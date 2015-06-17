package gov.gtas.services;

import gov.gtas.model.udr.Rule;
import gov.gtas.repository.RuleRepository;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class RulePersistenceServiceImpl implements RulePersistenceService {
    @Resource
    private RuleRepository ruleRepository;
    
	@Override
	@Transactional
	public Rule create(Rule r) {
		Rule rule = ruleRepository.save(r);
		return rule;
	}

	@Override
	@Transactional
	public Rule delete(Long id) {
		Rule ruleToDelete = ruleRepository.findOne(id);
		if(ruleToDelete != null){
			ruleRepository.delete(id);
		}
		return ruleToDelete;
	}

	@Override
	public List<Rule> findAll() {
		return (List<Rule>)ruleRepository.findAll();
	}

	@Override
	@Transactional
	public Rule update(Rule rule, String userId) {
		//User updatingUser =  
		ruleRepository.save(rule);
		return null;
	}

	@Override
	public Rule findById(Long id) {
		return ruleRepository.findOne(id);
	}

}
