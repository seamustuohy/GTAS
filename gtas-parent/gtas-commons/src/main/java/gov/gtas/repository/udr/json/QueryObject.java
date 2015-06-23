package gov.gtas.repository.udr.json;

import java.io.Serializable;
import java.util.List;

public class QueryObject implements Serializable{
   /**
	 * serial  version UID.
	 */
	private static final long serialVersionUID = -1825443604051080662L;
	
   private String conditionCode;
   private List<QueryObject> rule;
/**
 * @return the conditionCode
 */
public String getConditionCode() {
	return conditionCode;
}
/**
 * @param conditionCode the conditionCode to set
 */
public void setConditionCode(String conditionCode) {
	this.conditionCode = conditionCode;
}
/**
 * @return the rule
 */
public List<QueryObject> getRule() {
	return rule;
}
/**
 * @param rule the rule to set
 */
public void setRule(List<QueryObject> rule) {
	this.rule = rule;
}
   
}
