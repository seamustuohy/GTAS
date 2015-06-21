package gov.gtas.udrbuilder.service;

import gov.gtas.model.udr.Rule;
import gov.gtas.udrbuilder.repository.RuleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class UdrBuilderServiceImpl implements UdrBuilderService {

	@Autowired
	RuleRepository ruleRepository;

	@Override
	public void saveRule(Rule rule) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rule getById(Long id) {
		return ruleRepository.findOne(id);
	}

	@Override
	public void updateRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Rule> allRules() {
		// TODO Auto-generated method stub
		return null;
	}

}
