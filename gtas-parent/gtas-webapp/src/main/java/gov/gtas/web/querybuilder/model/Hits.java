package gov.gtas.web.querybuilder.model;

public class Hits extends BaseQueryBuilderModel {

	public Hits() {
		Column column = new Column("has_hits", "Has Hits", "string");
		getColumns().add(column);
		column = new Column("has_list_rule_hit", "Has List Rule Hit", "string");
		getColumns().add(column);
		column = new Column("has_rule_hit", "Has Rule Hit", "string");
		getColumns().add(column);
		column = new Column("master_list_id", "List Rules - Master List Id", "string");
		getColumns().add(column);
		column = new Column("sub_list_id", "Sub List Id", "string");
		getColumns().add(column);
		column = new Column("rule_id", "Rules - Rule Id", "string");
		getColumns().add(column);
	}
}
