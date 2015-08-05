package gov.gtas.rule.builder;

import javax.persistence.EnumType;

import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
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
	
	//TODO NOT PASSPORT and NOT VISA conditions
    private boolean passport;
    private boolean visa;
    private boolean negativeClassCondition;
    
    private String travelerVariableName;
    private boolean travelerHasNoRuleCondition;
	public DocumentConditionBuilder(final String drlVariableName, final String travelerVariableName){
		super(drlVariableName, EntityEnum.DOCUMENT.getEntityName());
		this.travelerVariableName = travelerVariableName;
		passport = false;
		visa = false;
		travelerHasNoRuleCondition = false;
		negativeClassCondition = false;
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && !isPassport() && !isVisa();
	}

	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#init()
	 */
	@Override
	public void init() {
		super.init();
		passport = false;
		visa = false;
		negativeClassCondition = false;
		this.travelerHasNoRuleCondition = false;
	}

	// TODO: Amit review

	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
//		if(travelerHasNoRuleCondition){
//			if(isPassport()){
//				bldr.append(getDrlVariableName()).append(":");
//				bldr.append(EntityLookupEnum.Passport).append("()\n")
//				.append(travelerVariableName).append(":").append(EntityEnum.TRAVELER.getEntityName()).append("(id == ")
//				.append(getDrlVariableName()).append(".")
//				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(")\n");				
//			} else if (isVisa()){
//				bldr.append(getDrlVariableName()).append(":").append(EntityLookupEnum.Visa).append("()\n")
//				.append(travelerVariableName).append(":").append(EntityEnum.TRAVELER.getEntityName()).append("(id == ")
//				.append(getDrlVariableName()).append(".")
//				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(")\n");
//			}
//		} else {
//			if(isPassport()){
//				bldr.append(getDrlVariableName()).append(":");
//				bldr.append(EntityLookupEnum.Passport)
//				.append("(").append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(" == ")
//				.append(travelerVariableName).append(".id)\n");
//			} else if (isVisa()){
//				bldr.append(getDrlVariableName()).append(":").append(EntityLookupEnum.Visa)
//				.append("(").append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(" == ")
//				.append(travelerVariableName).append(".id)\n");
//	
//			}			
//		}
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
		if(travelerHasNoRuleCondition){
			if(isPassport()){
				bldr.append(EntityLookupEnum.Document).append("(documentType == \"P\")\n")
				.append(travelerVariableName).append(":").append(EntityEnum.TRAVELER.getEntityName()).append("(id == ")
				.append(getDrlVariableName()).append(".")
				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(")\n");

			} else if (isVisa()){
				bldr.append(EntityLookupEnum.Document).append("(documentType == \"V\")\n")
				.append(travelerVariableName).append(":").append(EntityEnum.TRAVELER.getEntityName()).append("(id == ")
				.append(getDrlVariableName()).append(".")
				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(")\n");
			} else {
				bldr.append(travelerVariableName).append(":").append(EntityEnum.TRAVELER.getEntityName()).append("(id == ")
				.append(getDrlVariableName()).append(".")
				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(")\n");				
			}
			
		} else {
			if(isPassport()){
				bldr.append(EntityLookupEnum.Document).append("(documentType == \"P\")\n")
				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(" == ")
				.append(travelerVariableName).append(".id)\n");			
			} else if (isVisa()){
				bldr.append(EntityLookupEnum.Document).append("(documentType == \"V\")\n")
				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(" == ")
				.append(travelerVariableName).append(".id)\n");
			} else {
				bldr.append(EntityLookupEnum.Document).append("(id == ").append(getDrlVariableName()).append(".id, ")
				.append(DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()).append(" == ")
				.append(travelerVariableName).append(".id)\n");				
			}
		}
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#addCondition(gov.gtas.model.udr.RuleCond)
	 */
	@Override
	public void addCondition(RuleCond cond) {
		if(cond.getAttrName().equals("class")){
			if(!isPassport() && !isVisa()){
				String val = RuleConditionBuilderHelper.getSingleStringValue(cond);
				if(val != null && "P".equals(val)){
					this.passport = true;
				} else if(val != null && "V".equals(val)){
					this.visa = true;
				} else {
					//warning
					logger.warn("DocumentConditionBuilder.addCondition() - invalid classs value.\n"+cond.toString());					
				}
				if(cond.getOpCode() == OperatorCodeEnum.NOT_EQUAL){
					negativeClassCondition = true;
				}
			} else {
				//warning
				logger.warn("DocumentConditionBuilder.addCondition() - multiple class specifications.\n"+cond.toString());
			}
		} else {
		  super.addCondition(cond);
		}
	}
	/**
	 * @return the passenger
	 */
	public boolean isPassport() {
		return passport;
	}
	/**
	 * @return the crew
	 */
	public boolean isVisa() {
		return visa;
	}
	/**
	 * @param travelerHasNoRuleCondition the travelerHasNoRuleCondition to set
	 */
	protected void setTravelerHasNoRuleCondition(boolean travelerHasNoRuleCondition) {
		this.travelerHasNoRuleCondition = travelerHasNoRuleCondition;
	}
	/**
	 * @return the negativeClassCondition
	 */
	protected boolean isNegativeClassCondition() {
		return negativeClassCondition;
	}

}
