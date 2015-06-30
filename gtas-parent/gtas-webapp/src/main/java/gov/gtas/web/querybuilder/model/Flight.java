package gov.gtas.web.querybuilder.model;

public class Flight extends BaseQueryBuilderModel {

	public Flight() {
		Column column = new Column("destination.iata", "Airport - Destination", "string");
		getColumns().add(column);
		column = new Column("origin.iata", "Airport - Origin", "string");
		getColumns().add(column);
		column = new Column("carrier.iata", "Carrier", "string");
		getColumns().add(column);
		column = new Column("destinationCountry.iso3", "Country - Destination", "string");
		getColumns().add(column);
		column = new Column("originCountry.iso3", "Country - Origin", "string");
		getColumns().add(column);
		column = new Column("inbound", "Direction - Inbound", "string");
		getColumns().add(column);
		column = new Column("outbound", "Direction - Outbound", "string");
		getColumns().add(column);
		column = new Column("eta", "ETA", "datetime");
		getColumns().add(column);
		column = new Column("etd", "ETD", "datetime");
		getColumns().add(column);
		column = new Column("flightNumber", "Number", "string");
		getColumns().add(column);
		column = new Column("thru", "Thru", "string");
		getColumns().add(column);
		
	}
}
