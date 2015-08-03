package gov.gtas.rule.builder;

import gov.gtas.model.udr.RuleCond;

import java.util.LinkedList;
import java.util.List;

public abstract class EntityConditionBuilder {
    private List<String> conditionList;
    private boolean andConnectorIsComma;
    private String entityClassName;
    private String drlVariableName;
    
    protected EntityConditionBuilder(final String varName, final String entityClassName){
    	this.entityClassName = entityClassName;
    	this.drlVariableName = varName;
    	init();
    }
    public void init(){
    	this.andConnectorIsComma = true;
    	this.conditionList = new LinkedList<String>();   	
    }
    public boolean isEmpty(){
    	return conditionList.isEmpty();
    }
    public String build(){
		final StringBuilder bldr = new StringBuilder();
		if(conditionList.isEmpty()){
			addSpecialConditionsWithoutActualConditions(bldr);
		} else {
			bldr.append(drlVariableName).append(":").append(entityClassName).append("(");
			if(andConnectorIsComma){
			   bldr.append(String.join(", ", conditionList));
			}else{
				   bldr.append(String.join(" && ", conditionList));				
			}
			bldr.append(")\n");
			addSpecialConditions(bldr);
		}
    	return bldr.toString();
    }
    protected abstract void addSpecialConditionsWithoutActualConditions(final StringBuilder  bldr);
    protected abstract void addSpecialConditions(final StringBuilder  bldr);
    
	public void addCondition(final RuleCond cond) {
		final StringBuilder bldr = new StringBuilder();
		switch (cond.getOpCode()) {
		case EQUAL:
		case NOT_EQUAL:
		case GREATER:
		case GREATER_OR_EQUAL:
		case LESS:
		case LESS_OR_EQUAL:
		case BEGINS_WITH:
		case NOT_BEGINS_WITH:
		case ENDS_WITH:
		case NOT_ENDS_WITH:
		case CONTAINS:
		case NOT_CONTAINS:
			bldr.append(cond.getAttrName()).append(" ").append(cond.getOpCode().getOperatorString()).append(" ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			break;
		case IN:
		case NOT_IN:
			bldr.append(cond.getAttrName()).append(" ").append(cond.getOpCode().getOperatorString()).append(" ");
			RuleConditionBuilderHelper.addConditionValues(cond, bldr);
			break;
		case IS_EMPTY:
		case IS_NOT_EMPTY:
		case IS_NULL:
		case IS_NOT_NULL:
			bldr.append(cond.getAttrName()).append(" ").append(cond.getOpCode().getOperatorString()).append(" ");
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
			//convert all commas to &&
			andConnectorIsComma = false;
			break;
		case MEMBER_OF:
			bldr.append(cond.getAttrName()).append(" memberOf ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues().get(0), bldr);
			break;
		case NOT_MEMBER_OF:
			bldr.append(cond.getAttrName()).append(" not memberOf ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues().get(0), bldr);
			break;
		}
		this.conditionList.add(bldr.toString());
	}
	/**
	 * @return the drlVariableName
	 */
	public String getDrlVariableName() {
		return drlVariableName;
	}
	
}
