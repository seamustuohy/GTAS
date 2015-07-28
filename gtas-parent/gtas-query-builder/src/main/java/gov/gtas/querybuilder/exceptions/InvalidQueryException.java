package gov.gtas.querybuilder.exceptions;

public class InvalidQueryException extends Exception {

	private static final long serialVersionUID = 1L;
	private Object object;
	
	public InvalidQueryException(String message, Object object) {
		super(message);
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
	
}
