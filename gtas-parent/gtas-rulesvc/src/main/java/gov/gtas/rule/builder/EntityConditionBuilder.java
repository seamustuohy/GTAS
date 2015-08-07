package gov.gtas.rule.builder;

import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;

import java.util.LinkedList;
import java.util.List;

/**
 * Common functionality for building Rule criteria for entities such as
 * Traveler, Fligh and Document.
 * 
 * @author GTAS3 (AB)
 *
 */
public abstract class EntityConditionBuilder {
	/*
	 * list of criteria for the entity that will be combined to create an LHS term for the rule.
	 * Normally the criteria will be separated by ',' signifying an AND connector.
	 */
	private List<String> conditionList;
	/*
	 * when one or more criteria contain a connector such as '||' then the comma connector cannot be used.
	 * In that case andConnectorIsComma is false, and '&&' is used as connector.
	 * (an example is the NOT_BETWEEN operator, which contains a '||' connector.)
	 */
	private boolean andConnectorIsComma;
	/*
	 * The entity Java class name used to form the Drools LHS terms.
	 */
	private String entityClassName;
	/*
	 * The binding variable name for this entity.
	 */
	private String drlVariableName;

	/**
	 * The protected constructor to be invoked by derived classes.
	 * @param varName the binding variable name.
	 * @param entityClassName the entity class name without the package part of the name.
	 */
	protected EntityConditionBuilder(final String varName,
			final String entityClassName) {
		this.entityClassName = entityClassName;
		this.drlVariableName = varName;
		this.andConnectorIsComma = true;
		this.conditionList = new LinkedList<String>();
	}
    /**
     * This builder can be reused after calling reset.
     */
	protected void reset() {
		this.andConnectorIsComma = true;
		this.conditionList.clear();
	}
    /**
     * Checks whether criteria have been added.
     * @return true if no criteria have been added.
     */
	public boolean isEmpty() {
		return conditionList.isEmpty();
	}

	public String build() {
		final StringBuilder bldr = new StringBuilder();
		if (conditionList.isEmpty()) {
			addSpecialConditionsWithoutActualConditions(bldr);
		} else {
			bldr.append(drlVariableName).append(":").append(entityClassName)
					.append("(");
			if (andConnectorIsComma) {
				bldr.append(String.join(", ", conditionList));
			} else {
				bldr.append(String.join(" && ", conditionList));
			}
			bldr.append(")\n");
			addSpecialConditions(bldr);
		}
		return bldr.toString();
	}

	protected abstract void addSpecialConditionsWithoutActualConditions(
			final StringBuilder bldr);

	protected abstract void addSpecialConditions(final StringBuilder bldr);

	public void addCondition(final RuleCond cond) {
		final StringBuilder bldr = new StringBuilder();
		switch (cond.getOpCode()) {
		case EQUAL:
		case NOT_EQUAL:
		case GREATER:
		case GREATER_OR_EQUAL:
		case LESS:
		case LESS_OR_EQUAL:
			bldr.append(cond.getAttrName()).append(" ")
					.append(cond.getOpCode().getOperatorString()).append(" ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			break;
		case IN:
		case NOT_IN:
			bldr.append(cond.getAttrName()).append(" ")
					.append(cond.getOpCode().getOperatorString()).append(" ");
			RuleConditionBuilderHelper.addConditionValues(cond, bldr);
			break;
		case IS_EMPTY:
		case IS_NOT_EMPTY:
		case IS_NULL:
		case IS_NOT_NULL:
			//no values!
			bldr.append(cond.getAttrName()).append(" ")
					.append(cond.getOpCode().getOperatorString()).append(" ");
			break;
		case BEGINS_WITH:
		case ENDS_WITH:
		case CONTAINS:
			bldr.append(cond.getAttrName()).append(" ")
			.append(OperatorCodeEnum.IS_NOT_EMPTY.getOperatorString());
			bldr.append(", ").append(cond.getAttrName()).append(" ")
			.append(cond.getOpCode().getOperatorString()).append(" ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
            break;			
		case NOT_BEGINS_WITH:
		case NOT_ENDS_WITH:
		case NOT_CONTAINS:
			bldr.append("!(")
			.append(cond.getAttrName()).append(" ")
			.append(OperatorCodeEnum.IS_NOT_EMPTY.getOperatorString());
			bldr.append(", ").append(cond.getAttrName()).append(" ")
			.append(cond.getOpCode().getOperatorString()).append(" ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			bldr.append(")");
            break;			
		case BETWEEN:
			bldr.append(cond.getAttrName()).append(" >= ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			bldr.append(", ").append(cond.getAttrName()).append(" <= ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(1), bldr);
			break;
		case NOT_BETWEEN:
			bldr.append("(").append(cond.getAttrName()).append(" < ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			bldr.append(" || ").append(cond.getAttrName()).append(" > ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(1), bldr);
			bldr.append(")");
			// convert all commas to &&
			andConnectorIsComma = false;
			break;
		case MEMBER_OF:
			bldr.append(cond.getAttrName()).append(" memberOf ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			break;
		case NOT_MEMBER_OF:
			bldr.append(cond.getAttrName()).append(" not memberOf ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			break;
		}
		this.conditionList.add(bldr.toString());
	}

	public void addConditionAsString(final String condStr) {
		this.conditionList.add(condStr);
	}

	/**
	 * @return the drlVariableName
	 */
	public String getDrlVariableName() {
		return drlVariableName;
	}

}
