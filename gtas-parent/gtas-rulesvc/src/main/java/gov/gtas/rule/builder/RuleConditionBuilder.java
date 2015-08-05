package gov.gtas.rule.builder;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;

import java.text.ParseException;
/**
 * Generates the "when" part of a DRL rule.
 * @author GTAS3 (AB)
 *
 */
public class RuleConditionBuilder {
			
	private TravelerConditionBuilder travelerConditionBuilder;
	private DocumentConditionBuilder documentConditionBuilder;
	private FlightConditionBuilder flightConditionBuilder;
	
	private String travelerVariableName;
	
	private StringBuilder conditionDescriptionBuilder;
	
	/**
	 * Constructor for the Simple Rules:<br>
	 * One Traveler, one document, one flight.
	 * 
	 */
	public RuleConditionBuilder(final String travelerVariableName, final String flightVariableName, final String documentVariableName){
		this.travelerVariableName = travelerVariableName;
		this.travelerConditionBuilder = new TravelerConditionBuilder(travelerVariableName);
		this.documentConditionBuilder = new DocumentConditionBuilder(documentVariableName, travelerVariableName);
		this.flightConditionBuilder = new FlightConditionBuilder(flightVariableName, travelerVariableName);		
	}
	
	/**
	 * Appends the generated "when" part of the rule to the rule document.
	 * @param parentStringBuilder the rule document builder.
	 * @throws ParseException if the UDR has invalid formatting.
	 */
	public void buildConditionsAndApppend(
			final StringBuilder parentStringBuilder) throws ParseException{
		
		if(travelerConditionBuilder.isEmpty()){
			if(!documentConditionBuilder.isEmpty()){
			  flightConditionBuilder.addLinkedTraveler(this.travelerVariableName);
			}
			documentConditionBuilder.setTravelerHasNoRuleCondition(true);
		} else {
			flightConditionBuilder.addLinkedTraveler(this.travelerVariableName);
		}
	    parentStringBuilder.append(travelerConditionBuilder.build());
	    parentStringBuilder.append(documentConditionBuilder.build());
	    parentStringBuilder.append(flightConditionBuilder.build());
	    travelerConditionBuilder.reset();
	    documentConditionBuilder.reset();
	    flightConditionBuilder.reset();
	    
	}
    /**
     * Adds a rule condition to the builder.
     * @param cond the condition to add.
     */
	public void addRuleCondition(final RuleCond cond) {
		//add the hit reason description
		if(conditionDescriptionBuilder == null){
			conditionDescriptionBuilder = new StringBuilder(RuleConditionBuilderHelper.createConditionDescription(cond));
		}else{
		    conditionDescriptionBuilder.append(RuleHitDetail.HIT_REASON_SEPARATOR).append(RuleConditionBuilderHelper.createConditionDescription(cond));
		}
		switch (cond.getEntityName()) {
		case TRAVELER:
			travelerConditionBuilder.addCondition(cond);
			break;
		case DOCUMENT:
			documentConditionBuilder.addCondition(cond);
			break;
		case FLIGHT:
			flightConditionBuilder.addCondition(cond);
			break;
		default:
			break;
		}

	}
//	private static final String ACTION_TRAVELER_HIT = "resultList.add(new RuleHitDetail(%dL, %d, $t.getId()));\n";
	private static final String ACTION_TRAVELER_HIT = "resultList.add(RuleHitDetail.createRuleHitDetail(%dL, %d, \"%s\", %s, \"%s\"));\n";
	public void addRuleAction(StringBuilder ruleStringBuilder, UdrRule parent, Rule rule, String travelerVariableName) {
		String cause = conditionDescriptionBuilder.toString().replace("\"", "'");
		ruleStringBuilder
				.append("then\n")
				.append(String.format(ACTION_TRAVELER_HIT, parent.getId(),
						rule.getRuleIndex(), parent.getTitle(), travelerVariableName, cause)).append("end\n");
		conditionDescriptionBuilder = null;
	}	
}
