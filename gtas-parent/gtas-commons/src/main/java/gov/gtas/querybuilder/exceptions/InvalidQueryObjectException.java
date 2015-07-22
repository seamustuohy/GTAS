package gov.gtas.querybuilder.exceptions;

import gov.gtas.model.udr.json.QueryObject;

public class InvalidQueryObjectException extends Exception {

	private static final long serialVersionUID = 1L;
	private QueryObject queryObject;
	
	public InvalidQueryObjectException(String message, QueryObject queryObject) {
		super(message);
		this.queryObject = queryObject;
	}

	public QueryObject getQueryObject() {
		return queryObject;
	}
	
}
