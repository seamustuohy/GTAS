package gov.gtas.rule.builder;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;

import java.text.ParseException;

/**
 * Generates the "when" part of a DRL rule.
 * 
 * @author GTAS3 (AB)
 */
public class RuleConditionBuilder {

	private PassengerConditionBuilder passengerConditionBuilder;
	private DocumentConditionBuilder documentConditionBuilder;
	private FlightConditionBuilder flightConditionBuilder;

	private String passengerVariableName;

	private StringBuilder conditionDescriptionBuilder;

	/**
	 * Constructor for the Simple Rules:<br>
	 * (i.e., One Passenger, one document, one flight.)
	 * 
	 */
	public RuleConditionBuilder(final String passengerVariableName,
			final String flightVariableName, final String documentVariableName) {
		this.passengerVariableName = passengerVariableName;
		this.passengerConditionBuilder = new PassengerConditionBuilder(
				passengerVariableName);
		this.documentConditionBuilder = new DocumentConditionBuilder(
				documentVariableName, passengerVariableName);
		this.flightConditionBuilder = new FlightConditionBuilder(
				flightVariableName, passengerVariableName);
	}

	/**
	 * Appends the generated "when" part (i.e., the LHS) of the rule to the rule document.
	 * 
	 * @param parentStringBuilder
	 *            the rule document builder.
	 * @throws ParseException
	 *             if the UDR has invalid formatting.
	 */
	public void buildConditionsAndApppend(
			final StringBuilder parentStringBuilder) throws ParseException {

		if (passengerConditionBuilder.isEmpty()) {
			if (!documentConditionBuilder.isEmpty()) {
				flightConditionBuilder
						.addLinkedPassenger(this.passengerVariableName);
			}
			documentConditionBuilder.setPassengerHasNoRuleCondition(true);
		} else {
			flightConditionBuilder.addLinkedPassenger(this.passengerVariableName);
		}
		parentStringBuilder.append(passengerConditionBuilder.build());
		parentStringBuilder.append(documentConditionBuilder.build());
		parentStringBuilder.append(flightConditionBuilder.build());
		passengerConditionBuilder.reset();
		documentConditionBuilder.reset();
		flightConditionBuilder.reset();

	}

	/**
	 * Adds a rule condition to the builder.
	 * 
	 * @param cond
	 *            the condition to add.
	 */
	public void addRuleCondition(final RuleCond cond) {
		// add the hit reason description
		if (conditionDescriptionBuilder == null) {
			conditionDescriptionBuilder = new StringBuilder(
					RuleConditionBuilderHelper.createConditionDescription(cond));
		} else {
			conditionDescriptionBuilder.append(
					RuleHitDetail.HIT_REASON_SEPARATOR)
					.append(RuleConditionBuilderHelper
							.createConditionDescription(cond));
		}
		switch (cond.getEntityName()) {
		case PASSENGER:
			passengerConditionBuilder.addCondition(cond);
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

	private static final String ACTION_PASSENGER_HIT = "resultList.add(RuleHitDetail.createRuleHitDetail(%dL, %d, \"%s\", %s, \"%s\"));\n";

	public void addRuleAction(StringBuilder ruleStringBuilder, UdrRule parent,
			Rule rule, String passengerVariableName) {
		String cause = conditionDescriptionBuilder.toString()
				.replace("\"", "'");
		ruleStringBuilder
				.append("then\n")
				.append(String.format(ACTION_PASSENGER_HIT, parent.getId(),
						rule.getRuleIndex(), parent.getTitle(),
						passengerVariableName, cause)).append("end\n");
		conditionDescriptionBuilder = null;
	}
}
