package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum AddressMapping implements IEntityMapping {
	
	CITY ("city", "City", TypeEnum.STRING.getType()),
	COUNTRY ("country", "Country", TypeEnum.STRING.getType()),
	ADDRESS_LINE_1 ("line1", "Line 1", TypeEnum.STRING.getType()),
	ADDRESS_LINE_2 ("line2", "Line 2", TypeEnum.STRING.getType()),
	ADDRESS_LINE_3 ("line3", "Line 3", TypeEnum.STRING.getType()),
	POSTAL_CODE ("postalCode", "Postal Code", TypeEnum.STRING.getType()),
	STATE ("state", "State/Province", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private AddressMapping(String fieldName, String friendlyName, String fieldType) {
		
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
	
	public AddressMapping getEnum() {
		
		return this;
		
    }
}
