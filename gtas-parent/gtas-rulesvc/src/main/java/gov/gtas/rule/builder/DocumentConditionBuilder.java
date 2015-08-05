package gov.gtas.rule.builder;

import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.mappings.DocumentMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentConditionBuilder extends EntityConditionBuilder {
	/*
	 * The logger for the DocumentConditionBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DocumentConditionBuilder.class);

	private String travelerVariableName;
	private boolean travelerHasNoRuleCondition;

	public DocumentConditionBuilder(final String drlVariableName,
			final String travelerVariableName) {
		super(drlVariableName, EntityEnum.DOCUMENT.getEntityName());
		this.travelerVariableName = travelerVariableName;
		travelerHasNoRuleCondition = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.travelerHasNoRuleCondition = false;
	}

	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
		//NO OP.
		logger.debug("DocumentConditionBuilder.addSpecialConditionsWithoutActualConditions(); - NOOP");
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
		if (travelerHasNoRuleCondition) {
			bldr.append(travelerVariableName).append(":")
					.append(EntityEnum.TRAVELER.getEntityName())
					.append("(id == ").append(getDrlVariableName()).append(".")
					.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName())
					.append(")\n");
		} else {
			bldr.append(EntityEnum.DOCUMENT.getEntityName()).append("(id == ")
					.append(getDrlVariableName()).append(".id, ")
					.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName())
					.append(" == ").append(travelerVariableName)
					.append(".id)\n");
		}
	}


	/**
	 * @param travelerHasNoRuleCondition
	 *            the travelerHasNoRuleCondition to set
	 */
	protected void setTravelerHasNoRuleCondition(
			boolean travelerHasNoRuleCondition) {
		this.travelerHasNoRuleCondition = travelerHasNoRuleCondition;
	}

}
