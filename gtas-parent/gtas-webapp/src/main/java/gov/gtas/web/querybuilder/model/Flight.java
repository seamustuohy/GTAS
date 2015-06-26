package gov.gtas.web.querybuilder.model;

public class Flight extends BaseQueryBuilderModel {

	public Flight() {
		Column column = new Column("destination", "Airport - Destination", "string");
		getColumns().add(column);
		column = new Column("origin", "Airport - Origin", "string");
		getColumns().add(column);
		column = new Column("carrier", "Carrier", "string");
		getColumns().add(column);
		column = new Column("destinationCountry", "Country - Destination", "string");
		getColumns().add(column);
		column = new Column("originCountry", "Country - Origin", "string");
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
