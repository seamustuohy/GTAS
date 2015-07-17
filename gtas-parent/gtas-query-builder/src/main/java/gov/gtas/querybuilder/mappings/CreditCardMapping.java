package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum CreditCardMapping implements IEntityMapping {

	CREDIT_CARD_NUMBER ("ccNumber", "Number", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private CreditCardMapping(String fieldName, String friendlyName,
			String fieldType) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public String getFieldType() {
		return fieldType;
	}
	
	
}
