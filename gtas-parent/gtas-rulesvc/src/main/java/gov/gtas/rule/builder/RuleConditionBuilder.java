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

	private static final String TRAVELER_VARIABLE_NAME="$t";
	private static final String DOCUMENT_VARIABLE_NAME="$d";
	private static final String FLIGHT_VARIABLE_NAME="$f";
			
	private TravelerConditionBuilder travelerConditionBuilder;
	private DocumentConditionBuilder documentConditionBuilder;
	private FlightConditionBuilder flightConditionBuilder;
	
	private StringBuilder conditionDescriptionBuilder;
	
	/**
	 * Constructor for the Simple Rules:<br>
	 * One Traveler, one document, one flight.
	 * 
	 */
	public RuleConditionBuilder(){
		this.travelerConditionBuilder = new TravelerConditionBuilder(TRAVELER_VARIABLE_NAME);
		this.documentConditionBuilder = new DocumentConditionBuilder(DOCUMENT_VARIABLE_NAME, TRAVELER_VARIABLE_NAME);
		this.flightConditionBuilder = new FlightConditionBuilder(FLIGHT_VARIABLE_NAME, TRAVELER_VARIABLE_NAME);		
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
			  flightConditionBuilder.addLinkedTraveler(TRAVELER_VARIABLE_NAME);
			}
			documentConditionBuilder.setTravelerHasNoRuleCondition(true);
		} else {
			flightConditionBuilder.addLinkedTraveler(TRAVELER_VARIABLE_NAME);
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
}
