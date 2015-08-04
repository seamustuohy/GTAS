package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum NameOriginMapping implements IEntityMapping {

	FIRST_NAME ("first_name", "First Name", TypeEnum.STRING.getType()),
	FIRST_OR_LAST_NAME ("first_or_last_name", "First Name", TypeEnum.STRING.getType()),
	LAST_NAME ("last_name", "Last Name", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private NameOriginMapping(String fieldName, String friendlyName,
			String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
		this.displayField = displayField;
	}
	private NameOriginMapping(String fieldName, String friendlyName,
			String fieldType) {
		this(fieldName, friendlyName, fieldType, true);
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
	
	/**
	 * @return the displayField
	 */
	public boolean isDisplayField() {
		return displayField;
	}
	
}
