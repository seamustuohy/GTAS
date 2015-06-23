package gov.gtas.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Condition {

	@Expose
	private String entitytype;
	@Expose
	private String entityfield;
	@Expose
	private String operator;
	@Expose
	private List<Value> values = new ArrayList<Value>();
	@Expose
	private String logicalOpeator;
	@Expose
	private List<Condition> conditions = new ArrayList<Condition>();

	/**
	 * 
	 * @return The entitytype
	 */
	public String getEntitytype() {
		return entitytype;
	}

	/**
	 * 
	 * @param entitytype
	 *            The entitytype
	 */
	public void setEntitytype(String entitytype) {
		this.entitytype = entitytype;
	}

	/**
	 * 
	 * @return The entityfield
	 */
	public String getEntityfield() {
		return entityfield;
	}

	/**
	 * 
	 * @param entityfield
	 *            The entityfield
	 */
	public void setEntityfield(String entityfield) {
		this.entityfield = entityfield;
	}

	/**
	 * 
	 * @return The operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 
	 * @param operator
	 *            The operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 
	 * @return The values
	 */
	public List<Value> getValues() {
		return values;
	}

	/**
	 * 
	 * @param values
	 *            The values
	 */
	public void setValues(List<Value> values) {
		this.values = values;
	}

	/**
	 * 
	 * @return The logicalOpeator
	 */
	public String getLogicalOpeator() {
		return logicalOpeator;
	}

	/**
	 * 
	 * @param logicalOpeator
	 *            The logicalOpeator
	 */
	public void setLogicalOpeator(String logicalOpeator) {
		this.logicalOpeator = logicalOpeator;
	}

	/**
	 * 
	 * @return The conditions
	 */
	public List<Condition> getConditions() {
		return conditions;
	}

	/**
	 * 
	 * @param conditions
	 *            The conditions
	 */
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

}
