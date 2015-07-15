package gov.gtas.web.querybuilder.model;

public class PNR extends BaseQueryBuilderModel {

	public PNR() {
		Column column = new Column("bag_count", "Bag - Count", "integer");
		getColumns().add(column);
		column = new Column("booking_date", "Booking Date", "string");
		getColumns().add(column);
		column = new Column("carrier_code", "Carrier Code", "string");
		getColumns().add(column);
		column = new Column("days_booked_before_trvl", "Days Booked Before Travel", "integer");
		getColumns().add(column);
		column = new Column("dwell_airport", "Dwell - Airport", "integer");
		getColumns().add(column);
		column = new Column("dwell_country", "Dwell - Country", "integer");
		getColumns().add(column);
		column = new Column("dwell_duration", "Dwell - Duration", "string");
		getColumns().add(column);
		column = new Column("dwell_total_duration", "Dwell - Total Duration", "string");
		getColumns().add(column);
		column = new Column("form_of_payment", "Form of Payment", "string");
		getColumns().add(column);
		column = new Column("first_name", "Name - First", "string");
		getColumns().add(column);
		column = new Column("last_name", "Name - Last", "string");
		getColumns().add(column);
		column = new Column("middle_name", "Name - Middle", "string");
		getColumns().add(column);
		column = new Column("origin_airport", "Origin - Airport", "string");
		getColumns().add(column);
		column = new Column("origin_country", "Origin - Country", "string");
		getColumns().add(column);
		column = new Column("passenger_count", "Passenger Count", "integer");
		getColumns().add(column);
		column = new Column("record_locator", "Record Locator", "string");
		getColumns().add(column);
		column = new Column("route", "Route", "string");
		getColumns().add(column);
		
	}
}
