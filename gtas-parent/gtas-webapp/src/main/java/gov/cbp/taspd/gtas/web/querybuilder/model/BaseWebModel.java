package gov.cbp.taspd.gtas.web.querybuilder.model;

import java.util.List;

public abstract class BaseWebModel implements IQueryBuilderModel {

	private String label;
	private List<Column> columns;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Column> getColumns() {
		return columns;
	}
	
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
}
