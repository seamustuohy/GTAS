package gov.gtas.querybuilder.mappings;

import gov.gtas.enumtype.TypeEnum;

public enum TravelAgencyMapping implements IEntityMapping {

	LOCATION ("location", "Location", TypeEnum.STRING.getType()),
	NAME ("name", "Name", TypeEnum.STRING.getType()),
	PHONE ("phone", "Phone", TypeEnum.STRING.getType()); 
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private TravelAgencyMapping(String fieldName, String friendlyName,
			String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
		this.displayField = displayField;
	}
	private TravelAgencyMapping(String fieldName, String friendlyName,
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
	
	public boolean isDisplayField() {
		return displayField;
	}
	
}
