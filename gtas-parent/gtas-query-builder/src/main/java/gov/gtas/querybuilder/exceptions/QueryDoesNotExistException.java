package gov.gtas.querybuilder.exceptions;

import gov.gtas.querybuilder.model.QueryRequest;

public class QueryDoesNotExistException extends QueryBuilderException {

	private static final long serialVersionUID = 1L;
	private QueryRequest queryRequest;
	
	public QueryDoesNotExistException(String message, QueryRequest queryRequest) {
		super(message, queryRequest);
		this.queryRequest = queryRequest;
	}

	public QueryRequest getQueryRequest() {
		return queryRequest;
	}

	public void setQueryRequest(QueryRequest queryRequest) {
		this.queryRequest = queryRequest;
	}
	
}
