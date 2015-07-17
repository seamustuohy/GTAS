package gov.gtas.querybuilder.mappings;

public enum TravelAgencyMapping implements IEntityMapping {

	CITY ("city", "City", "string"),
	NAME ("name", "Name", "string"),
	PHONE ("phone", "Phone", "string");
	
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
