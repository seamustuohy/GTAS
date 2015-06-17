package gov.gtas.web.querybuilder.model;

import java.util.List;

public abstract class BaseQueryBuilderModel implements IQueryBuilderModel {

	private List<Column> columns;
	
	public List<Column> getColumns() {
		return columns;
	}
	
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
}
