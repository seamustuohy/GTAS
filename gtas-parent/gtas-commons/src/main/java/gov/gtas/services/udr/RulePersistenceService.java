package gov.gtas.services.udr;

import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.UdrRule;

import java.util.List;
/**
 * The Persistence Layer service for UDR (User Defined Rule).
 * Since an UDR can have complex AND-OR logic in it, it can give rise
 * to multiple rules for the Rule Engine. The latter are called engine rules.
 * @author GTAS3 (AB)
 *
 */
public interface RulePersistenceService {
	/**
	 * Creates UDR rule.
	 * @param rule the UDR rule object to persist in the DB.
	 * @param user the user persisting the rule (usually also the rule author.)
	 * @return the persisted rule.
	 */
	public UdrRule create(UdrRule rule, String userId);
	/**
	 * Deletes a UDR rule. The object is not physically deleted, but a 
	 * "delete flag" is set to indicate it is no longer in use.
	 * @param id the Id of the rule to delete.
	 * @param userId the user Id of the person deleting the rule (usually its author).
	 * @return the deleted rule.
	 */
    public UdrRule delete(Long id, String userId);
    /**
     * Find and return the list of all rules that have the "delete flag" set to "N".
     * @return list of all non-deleted rules.
     */
    public List<UdrRule> findAll();
    /**
     * Updates a UDR rule or its children engine rules.
     * @param rule the UDR rule to update.
     * @param userId the user Id of the person updating the rule (usually its author).
     * @return the updated rule.
     */
    public UdrRule update(UdrRule rule, List<Rule> newEngineRules, String userId) ;
    /**
     * Fetches a UDR rule by its Id.
     * Note: deleted rules can also be fetched by specifying their Id.
     * @param id the Id of the rule to fetch.
     * @return the fetched rule or null.
     */
    public UdrRule findById(Long id);
    /**
     * Fetches a UDR rule by its author and Title.
     * @param title the rule title.
     * @param authorUserId the author's user Id.
     * @return the fetched rule or null.
     */
    public UdrRule findByTitleAndAuthor(String title, String authorUserId);
    /**
     * Fetches a list of rules created by the specified author.
     * @param authorUserId the user Id of the author.
     * @return list of rules authored by the specified author or an empty list.
     */
    public List<UdrRule> findByAuthor(String authorUserId);
    /**
     * Fetches the latest version of the default knowledge base.
     * @return the knowledge base.
     */
    public KnowledgeBase findUdrKnowledgeBase();
    /**
     * Saves or updates the knowledge base.
     * @param kb the knowledge base.
     * @return the saved knowledge base.
     */
    public KnowledgeBase saveKnowledgeBase(KnowledgeBase kb);
}
