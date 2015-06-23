package gov.gtas.udrbuilder.service;

import gov.gtas.model.udr.Rule;
import gov.gtas.udrbuilder.repository.RuleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UdrBuilderServiceImpl implements UdrBuilderService {

	@Autowired
	RuleRepository ruleRepository;

	@Override
	public Rule getRule(Long id) {
		return ruleRepository.findOne(id);
	}

	@Override
	public void updateRule(Rule rule) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRule(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Rule> allRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRule(Rule rule) {
		// TODO Auto-generated method stub

	}

}
