package gov.gtas.services.udr;

import gov.gtas.model.udr.UdrRule;

import java.util.List;
/**
 * The Data Layer service for UDR.
 * @author GTAS3 (AB)
 *
 */
public interface RulePersistenceService {
	/**
	 * Creates UDR rule
	 * @param rule
	 * @param user
	 * @return
	 */
	public UdrRule create(UdrRule rule, String userId);
    public UdrRule delete(Long id, String userId);
    public List<UdrRule> findAll();
    public UdrRule update(UdrRule rule, String userId) ;
    public UdrRule findById(Long id);
    public UdrRule findByTitleAndAuthor(String title, String authorUserId);
    public List<UdrRule> findByAuthor(String authorUserId);
}
