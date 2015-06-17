package gov.gtas.web.querybuilder.model;

import java.util.List;

public interface IQueryBuilderModel {
	
	public String getLabel();
	public void setLabel(String label);
	public List<Column> getColumns() ;
	public void setColumns(List<Column> columns);
	
}
