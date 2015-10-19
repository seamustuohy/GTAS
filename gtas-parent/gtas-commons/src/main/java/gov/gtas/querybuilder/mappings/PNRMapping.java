package gov.gtas.querybuilder.mappings;

import gov.gtas.enumtype.TypeEnum;

public enum PNRMapping implements IEntityMapping {

	BAG_COUNT ("bagCount", "Bag - Count", TypeEnum.INTEGER.getType()),
	BOOKING_DATE ("dateBooked", "Booking Date", TypeEnum.DATE.getType()), 
	CARRIER_CODE ("carrier", "Carrier Code", TypeEnum.STRING.getType()),
	DAYS_BOOKED_BEFORE_TRAVEL ("daysBookedBeforeTravel", "Days Booked Before Travel", TypeEnum.INTEGER.getType()),
	FORM_OF_PAYMENT ("formOfPayment", "Form of Payment", TypeEnum.STRING.getType()),
	ORIGIN_AIRPORT ("origin", "Origin - Airport", TypeEnum.STRING.getType()),
	ORIGIN_COUNTRY ("originCountry", "Origin - Country", TypeEnum.STRING.getType()),
	PASSENGER_COUNT ("passengerCount", "Passenger Count", TypeEnum.INTEGER.getType()),
	RECORD_LOCATOR ("recordLocator", "Record Locator", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private PNRMapping(String fieldName, String friendlyName, String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
		this.displayField = displayField;
	}
	private PNRMapping(String fieldName, String friendlyName, String fieldType) {
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
