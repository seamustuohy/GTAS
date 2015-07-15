package gov.gtas.web.querybuilder.model;

public class Address extends BaseQueryBuilderModel {

	public Address() {
		
		Column column = new Column("city", "City", "string");
		getColumns().add(column);
		column = new Column("country", "Country", "string");
		getColumns().add(column);
		column = new Column("line1", "Line 1", "string");
		getColumns().add(column);
		column = new Column("line2", "Line 2", "string");
		getColumns().add(column);
		column = new Column("line3", "Line 3", "string");
		getColumns().add(column);
		column = new Column("postalCode", "Postal Code", "string");
		getColumns().add(column);
		column = new Column("state", "State/Province", "string");
		getColumns().add(column);
		
	}
}
