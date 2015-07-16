package gov.gtas.web.querybuilder.model;

public class TravelAgency extends BaseQueryBuilderModel {

	public TravelAgency() {
		Column column = new Column("city", "City", "string");
		getColumns().add(column);
		column = new Column("name", "Name", "string");
		getColumns().add(column);
		column = new Column("phone", "Phone", "string");
		getColumns().add(column);
	}
}
