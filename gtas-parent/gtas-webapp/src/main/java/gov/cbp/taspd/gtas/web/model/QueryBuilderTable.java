package gov.cbp.taspd.gtas.web.model;

import java.util.Map;

public class QueryBuilderTable {

	private String tableName;
	private Map<String, String> column;
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Map<String, String> getColumn() {
		return column;
	}
	
	public void setColumn(Map<String, String> column) {
		this.column = column;
	}
}
