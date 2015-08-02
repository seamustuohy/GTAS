package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum FlightMapping implements IEntityMapping {

	AIRPORT_DESTINATION ("destination.iata", "Airport - Destination", TypeEnum.STRING.getType()),
	AIRPORT_ORIGIN ("origin.iata", "Airport - Origin", TypeEnum.STRING.getType()),
	CARRIER ("carrier.iata", "Carrier", TypeEnum.STRING.getType()),
	COUNTRY_DESTINATION ("destinationCountry.iso3", "Country - Destination", TypeEnum.STRING.getType()),
	COUNTRY_ORIGIN ("originCountry.iso3", "Country - Origin", TypeEnum.STRING.getType()),
	DIRECTION ("direction", "Direction", TypeEnum.STRING.getType()),
	ETA ("eta", "ETA", TypeEnum.DATETIME.getType()),
	ETD ("etd", "ETD", TypeEnum.DATETIME.getType()),
	FLIGHT_DATE ("flightDate", "Flight Date", TypeEnum.DATE.getType()),	
	FLIGHT_NUMBER ("flightNumber", "Number", TypeEnum.STRING.getType()),
	THRU ("thru", "Thru", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private FlightMapping(String fieldName, String friendlyName, String fieldType) {
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
