package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.RuleServiceRequest;
import gov.cbp.taspd.gtas.model.Message;


public interface RuleService {
  RuleServiceResult invokeRuleset(String ruleSetName, RuleServiceRequest req);
  RuleServiceResult invokeRuleset(RuleServiceRequest req);
  RuleServiceRequest createRuleServiceRequest(Message requestMessage);
}
