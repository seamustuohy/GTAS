package gov.gtas.web.querybuilder.model;

public class NameOrigin extends BaseQueryBuilderModel {

	public NameOrigin() {
		Column column = new Column("first_name", "First Name", "string");
		getColumns().add(column);
		column = new Column("first_or_last_name", "First Name", "string");
		getColumns().add(column);
		column = new Column("last_name", "Last Name", "string");
		getColumns().add(column);
	}
}
