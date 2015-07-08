package gov.gtas.error;
/**
 * The error Handler
 * @author GTAS3 (AB)
 *
 */
public interface ErrorHandler {
	/**
	 * Creates the exception message for the indicated error.
	 * 
	 * @param errorCode
	 *            the error code.
	 * @param args
	 *            the error arguments providing context for the error.
	 * @return the error exception object.
	 */
	CommonServiceException createException(final String errorCode,
			final Object... args);
	void addErrorHandlerDelegate(ErrorHandler errorHandler);
}
