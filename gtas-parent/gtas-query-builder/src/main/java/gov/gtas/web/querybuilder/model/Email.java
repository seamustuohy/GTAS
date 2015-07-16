package gov.gtas.web.querybuilder.model;

public class Email extends BaseQueryBuilderModel {

	public Email() {
		Column column = new Column("email_address", "Address", "string");
		getColumns().add(column);
		column = new Column("domain", "Domain", "string");
		getColumns().add(column);
	}
}
