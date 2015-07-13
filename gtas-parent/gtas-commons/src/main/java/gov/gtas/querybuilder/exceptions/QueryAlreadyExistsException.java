package gov.gtas.querybuilder.exceptions;

public class QueryAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public QueryAlreadyExistsException(String message) {
        super(message);
    }
}
