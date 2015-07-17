package gov.gtas.rule;

import java.util.Collection;

import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.UdrRule;

public interface RuleManagementService {
    KnowledgeBase createKnowledgeBaseFromDRLString(String kbName, String drlString);
    String fetchDrlRulesFromKnowledgeBase(String kbName);
    KnowledgeBase createKnowledgeBaseFromUdrRules(String kbName, Collection<UdrRule> rules);
}
