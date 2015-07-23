package gov.gtas.rule.builder;

import java.text.ParseException;

import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
/**
 * Generates the "when" part of a DRL rule.
 * @author GTAS3 (AB)
 *
 */
public class RuleConditionBuilder {
	private static final String FLIGHT_PASSENGER_LINK_CONDITION =
			EntityLookupEnum.Pax.toString()
			+"(id == $p.id) from $f.passengers\n";
	private static final String FLIGHT_PASSENGER_LINK_CONDITION2 =
			"$p:"+EntityLookupEnum.Pax.toString()
			+"() from $f.passengers\n";
	
	private StringBuilder passengerConditionBuilder;
	private StringBuilder flightConditionBuilder;
	private StringBuilder documentConditionBuilder;
	/**
	 * Appends the generated "when" part of the rule to the rule document.
	 * @param parentStringBuilder the rule document builder.
	 * @throws ParseException if the UDR has invalid formatting.
	 */
	public void buildConditionsAndApppend(
			final StringBuilder parentStringBuilder) throws ParseException{
		if (documentConditionBuilder != null) {
			RuleCond cond = RuleConditionBuilderHelper.createRuleCondition(
					EntityLookupEnum.Pax,
					EntityAttributeConstants.PAX_ATTR_ID,
					OperatorCodeEnum.EQUAL, 
					   "$d."+EntityAttributeConstants.DOCUMENT_ATTR_TRAVELER_ID, 
					   ValueTypesEnum.OBJECT_REF);
			addRuleCondition(cond);
			parentStringBuilder.append(documentConditionBuilder.append(")\n")
					.toString());
		}
		boolean addFlightPassengerCondition = false;
		if (flightConditionBuilder != null) {
			addFlightPassengerCondition = true;
			parentStringBuilder.append(flightConditionBuilder.append(")\n")
					.toString());
		}
		if (passengerConditionBuilder != null) {
			parentStringBuilder.append(passengerConditionBuilder.append(")\n")
					.toString());
			if(addFlightPassengerCondition){
			   parentStringBuilder.append(FLIGHT_PASSENGER_LINK_CONDITION);
			}
		} else{
			//There is no passenger condition
			if(addFlightPassengerCondition){
				   parentStringBuilder.append(FLIGHT_PASSENGER_LINK_CONDITION2);
			}
			
		}
		passengerConditionBuilder = null;
		flightConditionBuilder = null;
		documentConditionBuilder = null;
	}
    /**
     * Adds a rule condition to the builder.
     * @param cond the condition to add.
     */
	public void addRuleCondition(final RuleCond cond) {
		switch (cond.getEntityName()) {
		case Pax:
			if (passengerConditionBuilder == null) {
				passengerConditionBuilder = new StringBuilder("$p:Pax(");
			} else {
				passengerConditionBuilder.append(", ");
			}
			addCondition(cond, passengerConditionBuilder);
			break;
		case Document:
			if (documentConditionBuilder == null) {
				documentConditionBuilder = new StringBuilder("$d:Document(");
			} else {
				documentConditionBuilder.append(", ");
			}
			addCondition(cond, documentConditionBuilder);
			break;
		case Flight:
			if (flightConditionBuilder == null) {
				flightConditionBuilder = new StringBuilder("$f:Flight(");
			} else {
				flightConditionBuilder.append(", ");
			}
			addCondition(cond, flightConditionBuilder);
			break;
		case Airport:
			break;
		}

	}

	private void addCondition(final RuleCond cond, final StringBuilder bldr) {
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
		case NOT_BETWEEN://TODO convert all commas to &&
			bldr.append("(").append(cond.getAttrName()).append(" < ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(0), bldr);
			bldr.append(" || ").append(cond.getAttrName()).append(" > ");
			RuleConditionBuilderHelper.addConditionValue(cond.getValues()
					.get(1), bldr);
			bldr.append(")");
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
	}
}