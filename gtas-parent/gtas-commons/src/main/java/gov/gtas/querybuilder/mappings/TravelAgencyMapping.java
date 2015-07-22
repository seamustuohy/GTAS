package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum TravelAgencyMapping implements IEntityMapping {

	CITY ("city", "City", TypeEnum.STRING.getType()),
	NAME ("name", "Name", TypeEnum.STRING.getType()),
	PHONE ("phone", "Phone", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private TravelAgencyMapping(String fieldName, String friendlyName,
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
