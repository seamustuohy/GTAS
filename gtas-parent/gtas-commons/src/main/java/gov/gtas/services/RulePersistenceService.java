package gov.gtas.services;

import gov.gtas.model.udr.Rule;

import java.util.List;

public interface RulePersistenceService {
	public Rule create(Rule rule);
    public Rule delete(Long id);
    public List<Rule> findAll();
    public Rule update(Rule rule, String userId) ;
    public Rule findById(Long id);
}
