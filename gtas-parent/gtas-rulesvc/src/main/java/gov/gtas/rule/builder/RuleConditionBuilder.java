package gov.gtas.rule.builder;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.TypeEnum;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.json.QueryTerm;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

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
//	private List<String> causeList;

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
		
//		this.causeList = new LinkedList<String>();
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
			final StringBuilder parentStringBuilder){

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
	 * @param trm
	 *            the condition to add.
	 */
	public void addRuleCondition(final QueryTerm trm){
		// add the hit reason description
		if (conditionDescriptionBuilder == null) {
			conditionDescriptionBuilder = new StringBuilder();
		} else {
			conditionDescriptionBuilder.append(
					RuleHitDetail.HIT_REASON_SEPARATOR);
		}
		
		try{
			RuleConditionBuilderHelper
			.addConditionDescription(trm, conditionDescriptionBuilder);
	
			EntityEnum entity = EntityEnum.getEnum(trm.getEntity());
			TypeEnum attributeType = TypeEnum.getEnum(trm.getType());
			OperatorCodeEnum opCode = OperatorCodeEnum.getEnum(trm.getOperator());
			switch (entity) {
				case PASSENGER:
					passengerConditionBuilder.addCondition(opCode,trm.getField(), attributeType, trm.getValue());
					break;
				case DOCUMENT:
					documentConditionBuilder.addCondition(opCode,trm.getField(), attributeType, trm.getValue());
					break;
				case FLIGHT:
					flightConditionBuilder.addCondition(opCode,trm.getField(), attributeType, trm.getValue());
					break;
				default:
					break;
			}
		} catch (ParseException pe) {
			StringBuilder bldr = new StringBuilder("[");
			for (String val : trm.getValue()) {
				bldr.append(val).append(",");
			}
			bldr.append("]");
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_CODE,
					bldr.toString(), trm.getType(), "Engine Rule Creation");
		} catch (NullPointerException | IllegalArgumentException ex) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE,
					String.format("QueryTerm (operator=%s, type=%s)",
							trm.getOperator(), trm.getType()),
					"Engine Rule Creation");

		}

	}

	//private static final String ACTION_PASSENGER_HIT = "resultList.add(RuleHitDetail.createRuleHitDetail(%dL, %d, \"%s\", %s, \"%s\"));\n";
	private static final String ACTION_PASSENGER_HIT = "resultList.add(new RuleHitDetail(%s, %s, \"%s\", %s, null, \"%s\"));\n";

	public List<String> addRuleAction(StringBuilder ruleStringBuilder, UdrRule parent,
			Rule rule, String passengerVariableName) {
		String cause = conditionDescriptionBuilder.toString()
				.replace("\"", "'");
		ruleStringBuilder
				.append("then\n")
				.append(String.format(ACTION_PASSENGER_HIT, 
						"%dL", //the UDR ID may not be generated for create operation
						"%dL", //the rule ID may not be available
						parent.getTitle(),
						passengerVariableName, cause)).append("end\n");
		conditionDescriptionBuilder = null;
		return Arrays.asList(cause.split(RuleHitDetail.HIT_REASON_SEPARATOR));
	}
}
