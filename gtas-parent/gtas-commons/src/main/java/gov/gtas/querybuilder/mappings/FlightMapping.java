package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum FlightMapping implements IEntityMapping {

	AIRPORT_DESTINATION ("destination.iata", "Airport - Destination", TypeEnum.STRING.getType()),
	AIRPORT_ORIGIN ("origin.iata", "Airport - Origin", TypeEnum.STRING.getType()),
	CARRIER ("carrier.iata", "Carrier", TypeEnum.STRING.getType()),
	COUNTRY_DESTINATION ("destinationCountry.iso3", "Country - Destination", TypeEnum.STRING.getType()),
	COUNTRY_ORIGIN ("originCountry.iso3", "Country - Origin", TypeEnum.STRING.getType()),
	DIRECTION ("direction", "Direction", TypeEnum.STRING.getType()),
	ETA ("eta", "ETA", TypeEnum.DATE.getType()),
	ETD ("etd", "ETD", TypeEnum.DATE.getType()),
	FLIGHT_DATE ("flightDate", "Flight Date", TypeEnum.DATE.getType(), false),	
	FLIGHT_NUMBER ("flightNumber", "Number", TypeEnum.STRING.getType()),
	THRU ("", "Thru", TypeEnum.STRING.getType()); // missing field
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private FlightMapping(String fieldName, String friendlyName, String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
	}
	private FlightMapping(String fieldName, String friendlyName, String fieldType) {
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
