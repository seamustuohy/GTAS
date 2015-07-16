package gov.gtas.web.querybuilder.model;

public class Passenger extends BaseQueryBuilderModel {

	public Passenger() {
		Column column = new Column("age", "Age", "integer");
		getColumns().add(column);
		column = new Column("citizenshipCountry.iso3", "Citizenship Country", "string");
		getColumns().add(column);
		column = new Column("debarkation.iata", "Debarkation", "string");
		getColumns().add(column);
		column = new Column("debarkCountry.iso3", "Debarkation Country", "string");
		getColumns().add(column);
		column = new Column("dob", "DOB", "string");
		getColumns().add(column);
		column = new Column("embarkation.iata", "Embarkation", "string");
		getColumns().add(column);
		column = new Column("embarkCountry.iso3", "Embarkation Country", "string");
		getColumns().add(column);
		column = new Column("gender", "Gender", "string");
		getColumns().add(column);
		column = new Column("firstName", "Name - First", "string");
		getColumns().add(column);
		column = new Column("lastName", "Name - Last", "string");
		getColumns().add(column);
		column = new Column("middleName", "Name - Middle", "string");
		getColumns().add(column);
		column = new Column("residencyCountry", "Residency Country", "string"); // not in Entity
		getColumns().add(column);
		column = new Column("seat", "Seat", "string"); // not in Entity
		getColumns().add(column);
		column = new Column("type", "Type", "string"); // not in Entity
		getColumns().add(column);
		
	}
}
