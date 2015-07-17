package gov.gtas.querybuilder.exceptions;

import gov.gtas.querybuilder.model.QueryRequest;

public class QueryAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;
	private QueryRequest queryRequest;
		
	public QueryAlreadyExistsException() {
	}
	
	public QueryAlreadyExistsException(String message, QueryRequest queryRequest) {
		super(message);
		this.queryRequest = queryRequest;
	}

	public QueryRequest getQueryRequest() {
		return queryRequest;
	}

	public void setQueryRequest(QueryRequest queryRequest) {
		this.queryRequest = queryRequest;
	}
	
}
