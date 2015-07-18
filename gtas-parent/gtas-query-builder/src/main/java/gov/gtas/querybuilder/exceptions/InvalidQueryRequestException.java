package gov.gtas.querybuilder.exceptions;

import gov.gtas.querybuilder.model.QueryRequest;

public class InvalidQueryRequestException extends Exception {

	private static final long serialVersionUID = 1L;
	private QueryRequest request;
	
	public InvalidQueryRequestException(String message, QueryRequest request) {
		super(message);
		this.request = request;
	}

	public QueryRequest getRequest() {
		return request;
	}
	
}
