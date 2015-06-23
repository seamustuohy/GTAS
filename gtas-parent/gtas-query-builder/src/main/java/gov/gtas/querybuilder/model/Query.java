package gov.gtas.querybuilder.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
"condition",
"rules"
})
public class Query {
	private String condition;
	private List<Rule> rules;
	
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public List<Rule> getRules() {
		return rules;
	}
	
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "Query [condition=" + condition + ", rules=" + rules + "]";
	}
	
}
