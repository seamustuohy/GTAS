package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum PassengerMapping implements IEntityMapping {

	AGE ("age", "Age", TypeEnum.INTEGER.getType()),
	CITIZENSHIP_COUNTRY ("citizenshipCountry.iso2", "Citizenship Country", TypeEnum.STRING.getType()),
	DEBARKATION ("debarkation.iata", "Debarkation", TypeEnum.STRING.getType()),
	DEBARKATION_COUNTRY ("debarkCountry.iso2", "Debarkation Country", TypeEnum.STRING.getType()),
	DOB ("dob", "DOB", TypeEnum.DATE.getType()),
	EMBARKATION ("embarkation.iata", "Embarkation", TypeEnum.STRING.getType()),
	EMBARKATION_COUNTRY ("embarkCountry.iso2", "Embarkation Country", TypeEnum.STRING.getType()),
	GENDER ("gender", "Gender", TypeEnum.STRING.getType()),
	FIRST_NAME ("firstName", "Name - First", TypeEnum.STRING.getType()),
	LAST_NAME ("lastName", "Name - Last", TypeEnum.STRING.getType()),
	MIDDLE_NAME ("middleName", "Name - Middle", TypeEnum.STRING.getType()),
	RESIDENCY_COUNTRY ("residencyCountry.iso2", "Residency Country", TypeEnum.STRING.getType()), // not in Entity
	SEAT ("seat", "Seat", TypeEnum.STRING.getType()), // not in Entity
	PASSENGER_TYPE ("traveler_type", "Type", TypeEnum.STRING.getType()); 
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private PassengerMapping(String fieldName, String friendlyName,
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
