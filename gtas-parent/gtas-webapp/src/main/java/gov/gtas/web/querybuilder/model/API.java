package gov.gtas.web.querybuilder.model;

public class API extends BaseQueryBuilderModel {

	public API() {
		
		Column column = new Column("flightDirection", "Flight Direction", "string");
		getColumns().add(column);
	}
}
