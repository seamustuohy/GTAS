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

//	private String passengerVariableName;
//	private boolean passengerHasNoRuleCondition;

	public DocumentConditionBuilder(final String drlVariableName,
			final String passengerVariableName) {
		super(drlVariableName, EntityEnum.DOCUMENT.getEntityName());
//		this.passengerVariableName = passengerVariableName;
//		passengerHasNoRuleCondition = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#reset()
	 */
//	@Override
//	public void reset() {
//		super.reset();
//		this.passengerHasNoRuleCondition = false;
//	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
//		if (passengerHasNoRuleCondition) {
//			bldr.append(passengerVariableName).append(":")
//					.append(EntityEnum.PASSENGER.getEntityName())
//					.append("(id == ").append(getDrlVariableName()).append(".")
//					.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName())
//					.append(")\n");
//		} else {
//			bldr.append(EntityEnum.DOCUMENT.getEntityName()).append("(id == ")
//					.append(getDrlVariableName()).append(".id, ")
//					.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName())
//					.append(" == ").append(passengerVariableName)
//					.append(".id)\n");
//		}
	}

	public String getPassengerIdLinkExpression(){
		return getDrlVariableName()+"."+DocumentMapping.DOCUMENT_OWNER_ID.getFieldName();
	}
	/**
	 * Sets a flag to indicate that the rule being generated does not have any
	 * explicit passenger criteria.
	 * 
	 * @param passengerHasNoRuleCondition
	 */
//	protected void setPassengerHasNoRuleCondition(
//			boolean passengerHasNoRuleCondition) {
//
//		logger.debug("DocumentConditionBuilder.setPassengerHasNoRuleCondition()");
//
//		this.passengerHasNoRuleCondition = passengerHasNoRuleCondition;
//	}

}
