package gov.gtas.web.querybuilder.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseQueryBuilderModel implements IQueryBuilderModel {

	private List<Column> columns = new ArrayList<>();
	
	public List<Column> getColumns() {
		return columns;
	}
	
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
}
