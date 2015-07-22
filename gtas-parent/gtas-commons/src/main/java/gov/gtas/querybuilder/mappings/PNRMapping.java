package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum PNRMapping implements IEntityMapping {

	BAG_COUNT ("bag_count", "Bag - Count", TypeEnum.INTEGER.getType()),
	BOOKING_DATE ("booking_date", "Booking Date", TypeEnum.DATE.getType()),
	CARRIER_CODE ("carrier_code", "Carrier Code", TypeEnum.STRING.getType()),
	DAYS_BOOKED_BEFORE_TRAVEL ("days_booked_before_trvl", "Days Booked Before Travel", TypeEnum.INTEGER.getType()),
	DWELL_AIRPORT ("dwell_airport", "Dwell - Airport", TypeEnum.STRING.getType()),
	DWELL_COUNTRY ("dwell_country", "Dwell - Country", TypeEnum.STRING.getType()),
	DWELL_DURATION ("dwell_duration", "Dwell - Duration", TypeEnum.INTEGER.getType()),
	DWELL_TOTAL_DURATION ("dwell_total_duration", "Dwell - Total Duration", TypeEnum.INTEGER.getType()),
	FORM_OF_PAYMENT ("form_of_payment", "Form of Payment", TypeEnum.STRING.getType()),
	FIRST_NAME ("first_name", "Name - First", TypeEnum.STRING.getType()),
	LAST_NAME ("last_name", "Name - Last", TypeEnum.STRING.getType()),
	MIDDLE_NAME ("middle_name", "Name - Middle", TypeEnum.STRING.getType()),
	ORIGIN_AIRPORT ("origin_airport", "Origin - Airport", TypeEnum.STRING.getType()),
	ORIGIN_COUNTRY ("origin_country", "Origin - Country", TypeEnum.STRING.getType()),
	PASSENGER_COUNT ("passenger_count", "Passenger Count", TypeEnum.INTEGER.getType()),
	RECORD_LOCATOR ("record_locator", "Record Locator", TypeEnum.STRING.getType()),
	ROUTE ("route", "Route", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private PNRMapping(String fieldName, String friendlyName, String fieldType) {
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
