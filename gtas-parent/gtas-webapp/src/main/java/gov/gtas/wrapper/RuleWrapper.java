package gov.gtas.wrapper;

import com.google.gson.annotations.Expose;

public class RuleWrapper {

	@Expose
	private Rule rule;

	/**
	 * 
	 * @return The rule
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * 
	 * @param rule
	 *            The rule
	 */
	public void setRule(Rule rule) {
		this.rule = rule;
	}

}
