package gov.gtas.querybuilder.model;

import gov.gtas.model.udr.json.QueryObject;

public class QueryRequest {
	private int pageNumber;
	private int pageSize;
	private QueryObject query;
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public QueryObject getQuery() {
		return query;
	}
	
	public void setQuery(QueryObject query) {
		this.query = query;
	}
}
