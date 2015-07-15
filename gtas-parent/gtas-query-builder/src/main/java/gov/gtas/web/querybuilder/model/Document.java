package gov.gtas.web.querybuilder.model;

public class Document extends BaseQueryBuilderModel {

	public Document() {
		Column column = new Column("issuanceCountry", "Citizenship OR Issuance Country", "string");
		getColumns().add(column);
		column = new Column("expirationDate", "Expiration Date", "date");
		getColumns().add(column);
		column = new Column("issuanceCountry", "Issuance Country", "string");
		getColumns().add(column);
		column = new Column("documentNumber", "Number", "string");
		getColumns().add(column);
		column = new Column("documentType", "Type", "string");
		getColumns().add(column);
		
	}
}
