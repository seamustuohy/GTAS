package gov.gtas.model.udr.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Recursive query condition object.
 * @author GTAS3 (AB)
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class QueryObject implements QueryEntity{
   /**
	 * serial  version UID.
	 */
	private static final long serialVersionUID = -1825443604051080662L;
	
   private String condition;
   private List<QueryEntity> rules;
/**
 * @return the condition
 */
public String getCondition() {
	return condition;
}
/**
 * @param condition the condition to set
 */
public void setCondition(String condition) {
	this.condition = condition;
}
/**
 * @return the rules
 */
public List<QueryEntity> getRules() {
	return rules;
}
/**
 * @param rules the rules to set
 */
public void setRules(List<QueryEntity> rules) {
	this.rules = rules;
}

   
}
