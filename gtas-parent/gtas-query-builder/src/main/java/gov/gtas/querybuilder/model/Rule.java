package gov.gtas.querybuilder.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
"id",
"field",
"type",
"input",
"operator",
"value",
"condition",
"rules"
})
public class Rule {
	
	private String id;
    private String field;
    private String type;
    private String input;
    private String operator;
    private String value;
    @JsonProperty(required=false)
    private String condition;
    @JsonProperty(required=false)
    private List<Rule> rules;

    
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

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
		return "Rule [id=" + id + ", field=" + field + ", type=" + type
				+ ", input=" + input + ", operator=" + operator + ", value="
				+ value + ", condition=" + condition + ", rules=" + rules + "]";
	}

}
