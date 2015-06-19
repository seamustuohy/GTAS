package gov.gtas.services.udr;

import gov.gtas.model.udr.Rule;

import java.util.List;

public interface RulePersistenceService {
	/**
	 * Creates
	 * @param rule
	 * @param user
	 * @return
	 */
	public Rule create(Rule rule, String userId);
    public Rule delete(Long id, String userId);
    public List<Rule> findAll();
    public Rule update(Rule rule, String userId) ;
    public Rule findById(Long id);
}
