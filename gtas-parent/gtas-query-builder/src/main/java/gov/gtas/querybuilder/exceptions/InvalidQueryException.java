package gov.gtas.querybuilder.exceptions;

public class InvalidQueryException extends QueryBuilderException {

	private static final long serialVersionUID = 1L;
	private Object object;
	
	public InvalidQueryException(String message, Object object) {
		super(message, object);
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
	
}
