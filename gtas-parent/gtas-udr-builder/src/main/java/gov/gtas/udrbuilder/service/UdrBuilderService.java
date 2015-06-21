package gov.gtas.udrbuilder.service;

import gov.gtas.model.udr.Rule;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UdrBuilderService {

	public Rule getRule(Long id);

	public void updateRule(Rule rule);

	public void deleteRule(Long id);

	public List<Rule> allRules();

	public void createRule(Rule rule);

}
