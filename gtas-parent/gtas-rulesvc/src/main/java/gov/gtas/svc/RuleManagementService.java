package gov.gtas.svc;

import java.util.Collection;

import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.UdrRule;
/**
 * Interface specification for the rule management service.
 * @author GTAS3 (AB)
 *
 */
public interface RuleManagementService {
    KnowledgeBase createKnowledgeBaseFromDRLString(String kbName, String drlString);
    String fetchDrlRulesFromKnowledgeBase(String kbName);
    String fetchDefaultDrlRulesFromKnowledgeBase();
    KnowledgeBase createKnowledgeBaseFromUdrRules(String kbName, Collection<UdrRule> rules);
}
