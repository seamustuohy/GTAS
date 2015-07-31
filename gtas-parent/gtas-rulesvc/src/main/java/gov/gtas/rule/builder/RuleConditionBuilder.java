package gov.gtas.rule.builder;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
/**
 * Generates the "when" part of a DRL rule.
 * @author GTAS3 (AB)
 *
 */
public class RuleConditionBuilder {
//	private static final String ACTION_TRAVELER_HIT = "resultList.add(new RuleHitDetail(%dL, %d, $t.getId()));\n";
	private static final String ACTION_TRAVELER_HIT = "resultList.add(RuleHitDetail.createRuleHitDetail(%dL, %d, \"%s\", $t, \"%s\"));\n";
	public void addRuleAction(StringBuilder ruleStringBuilder, UdrRule parent, Rule rule) {
		String cause = conditionDescriptionBuilder.toString().replace("\"", "'");
	    System.out.println("***** cause="+cause);
		ruleStringBuilder
				.append("then\n")
				.append(String.format(ACTION_TRAVELER_HIT, parent.getId(),
						rule.getRuleIndex(), parent.getTitle(), cause)).append("end\n");
		conditionDescriptionBuilder = null;
	}

	private static final String TRAVELER_VARIABLE_NAME="$t";
	private static final String DOCUMENT_VARIABLE_NAME="$d";
	private static final String FLIGHT_VARIABLE_NAME="$f";
	
	private static final String FLIGHT_TRAVELER_LINK_CONDITION =
			EntityLookupEnum.Traveler.toString()
			+"(id == "+TRAVELER_VARIABLE_NAME+".id) from "+FLIGHT_VARIABLE_NAME+".passengers\n";
	private static final String FLIGHT_TRAVELER_LINK_CONDITION2 =
			TRAVELER_VARIABLE_NAME+":"+EntityLookupEnum.Traveler.toString()
			+"() from "+FLIGHT_VARIABLE_NAME+".passengers\n";
	
	private StringBuilder travelerConditionBuilder;	
	private StringBuilder flightConditionBuilder;
	private StringBuilder documentConditionBuilder;
	
	private boolean travelerIsPassenger;
	private boolean travelerIsCrew;
	private boolean documentIsPassport;
	private boolean documentIsVisa;
	
	private StringBuilder conditionDescriptionBuilder;
	
	/**
	 * Appends the generated "when" part of the rule to the rule document.
	 * @param parentStringBuilder the rule document builder.
	 * @throws ParseException if the UDR has invalid formatting.
	 */
	public void buildConditionsAndApppend(
			final StringBuilder parentStringBuilder) throws ParseException{
		if (documentConditionBuilder != null) {
			RuleCond cond = RuleConditionBuilderHelper.createRuleCondition(
					EntityLookupEnum.Traveler,
					EntityAttributeConstants.TRAVELER_ATTR_ID,
					OperatorCodeEnum.EQUAL, 
					DOCUMENT_VARIABLE_NAME+"."+EntityAttributeConstants.DOCUMENT_ATTR_TRAVELER_ID, 
					   ValueTypesEnum.OBJECT_REF);
			addRuleCondition(cond);
			parentStringBuilder.append(documentConditionBuilder.append(")\n")
					.toString());
		}
		boolean addFlightTravelerCondition = false;
		if (flightConditionBuilder != null) {
			addFlightTravelerCondition = true;
			parentStringBuilder.append(flightConditionBuilder.append(")\n")
					.toString());
		}
		if (travelerConditionBuilder != null) {
			parentStringBuilder.append(travelerConditionBuilder.append(")\n")
					.toString());
			if(addFlightTravelerCondition){
			   parentStringBuilder.append(FLIGHT_TRAVELER_LINK_CONDITION);
			}
		} else{
			//There is no traveler condition
			if(addFlightTravelerCondition){
				   parentStringBuilder.append(FLIGHT_TRAVELER_LINK_CONDITION2);
			}
			
		}
		travelerConditionBuilder = null;
		flightConditionBuilder = null;
		documentConditionBuilder = null;
	}
    /**
     * Adds a rule condition to the builder.
     * @param cond the condition to add.
     */
	public void addRuleCondition(final RuleCond cond) {
		if(conditionDescriptionBuilder == null){
			conditionDescriptionBuilder = new StringBuilder(RuleConditionBuilderHelper.createConditionDescription(cond));
		}else{
		    conditionDescriptionBuilder.append(RuleHitDetail.HIT_REASON_SEPARATOR).append(RuleConditionBuilderHelper.createConditionDescription(cond));
		}
		
		switch (cond.getEntityName()) {
		case Pax:
		case Crew:
		case Traveler:
			if (travelerConditionBuilder == null) {
				travelerConditionBuilder = new StringBuilder(TRAVELER_VARIABLE_NAME+":Traveler(");
			} else {
				travelerConditionBuilder.append(", ");
			}
			addCondition(cond, travelerConditionBuilder);
			break;
		case Passport:
		case Visa:
		case Document:
			if (documentConditionBuilder == null) {
				documentConditionBuilder = new StringBuilder(DOCUMENT_VARIABLE_NAME+":Document(");
			} else {
				documentConditionBuilder.append(", ");
			}
			addCondition(cond, documentConditionBuilder);
			break;
		case Flight:
			if (flightConditionBuilder == null) {
				flightConditionBuilder = new StringBuilder(FLIGHT_VARIABLE_NAME+":Flight(");
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
