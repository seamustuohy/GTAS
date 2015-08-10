package gov.gtas.querybuilder.mappings;

import gov.gtas.enumtype.TypeEnum;

public enum FrequentFlyerMapping implements IEntityMapping {

	AIRLINE ("ff_airline", "Airline", TypeEnum.STRING.getType()),
	FREQUENT_FLYER_NUMBER ("ff_number", "Number", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private FrequentFlyerMapping(String fieldName, String friendlyName,
			String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
		this.displayField = displayField;
	}
	private FrequentFlyerMapping(String fieldName, String friendlyName,
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
