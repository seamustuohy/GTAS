package gov.gtas.web.querybuilder.model;

public class FrequentFlyer extends BaseQueryBuilderModel {

	public FrequentFlyer() {
		Column column = new Column("ff_airline", "Airline", "string");
		getColumns().add(column);
		column = new Column("ff_number", "Number", "string");
		getColumns().add(column);
	}
}
