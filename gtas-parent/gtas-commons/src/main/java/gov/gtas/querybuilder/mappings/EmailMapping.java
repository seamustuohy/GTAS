package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum EmailMapping implements IEntityMapping {
	EMAIL_ADDRESS ("email_address", "Address", TypeEnum.STRING.getType()),
	DOMAIN ("domain", "Domain", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private EmailMapping(String fieldName, String friendlyName, String fieldType) {
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
