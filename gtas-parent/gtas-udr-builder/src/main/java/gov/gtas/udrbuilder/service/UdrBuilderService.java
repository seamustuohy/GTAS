package gov.gtas.udrbuilder.service;

import gov.gtas.model.udr.Rule;

import java.util.List;

import org.springframework.stereotype.Service;

@Service("udrBuilderService")
public interface UdrBuilderService {

	public void saveRule(Rule rule);

	public Rule getById(Long id);

	public void updateRule();

	public void deleteById(Long id);
	
	public List<Rule> allRules();

}
