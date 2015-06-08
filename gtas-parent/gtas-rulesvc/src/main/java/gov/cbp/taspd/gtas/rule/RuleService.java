package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.RuleServiceRequest;


public interface RuleService {
  public RuleServiceResult invokeRuleset(int ruleSetId, RuleServiceRequest req); 
}
