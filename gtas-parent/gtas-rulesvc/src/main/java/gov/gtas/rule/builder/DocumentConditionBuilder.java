package gov.gtas.rule.builder;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.querybuilder.mappings.DocumentMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentConditionBuilder extends EntityConditionBuilder {
	/*
	 * The logger for the DocumentConditionBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DocumentConditionBuilder.class);

	private String passengerVariableName;
	private boolean passengerHasNoRuleCondition;

	public DocumentConditionBuilder(final String drlVariableName,
			final String passengerVariableName) {
		super(drlVariableName, EntityEnum.DOCUMENT.getEntityName());
		this.passengerVariableName = passengerVariableName;
		passengerHasNoRuleCondition = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.passengerHasNoRuleCondition = false;
	}

	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
		//NO OP.
		logger.debug("DocumentConditionBuilder.addSpecialConditionsWithoutActualConditions(); - NOOP");
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
		if (passengerHasNoRuleCondition) {
			bldr.append(passengerVariableName).append(":")
					.append(EntityEnum.PASSENGER.getEntityName())
					.append("(id == ").append(getDrlVariableName()).append(".")
					.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName())
					.append(")\n");
		} else {
			bldr.append(EntityEnum.DOCUMENT.getEntityName()).append("(id == ")
					.append(getDrlVariableName()).append(".id, ")
					.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName())
					.append(" == ").append(passengerVariableName)
					.append(".id)\n");
		}
	}


	/**
	 * @param travelerHasNoRuleCondition
	 *            the travelerHasNoRuleCondition to set
	 */
	protected void setPassengerHasNoRuleCondition(
			boolean travelerHasNoRuleCondition) {
		this.passengerHasNoRuleCondition = travelerHasNoRuleCondition;
	}

}
