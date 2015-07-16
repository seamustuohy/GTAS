package gov.gtas.web.querybuilder.model;

public class Phone extends BaseQueryBuilderModel {

	public Phone() {
		Column column = new Column("phone_number", "Number", "string");
		getColumns().add(column);
	}
}
