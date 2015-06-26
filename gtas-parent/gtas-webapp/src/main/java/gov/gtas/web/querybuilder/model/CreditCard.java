package gov.gtas.web.querybuilder.model;

public class CreditCard extends BaseQueryBuilderModel {

	public CreditCard() {
		Column column = new Column("ccNumber", "Number", "string");
		getColumns().add(column);
	}
}
