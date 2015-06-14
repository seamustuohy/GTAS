package gov.cbp.taspd.gtas.error;
/**
 * Exception class for errors generated during Rule Engine execution.
 * @author GTAS3 (AB)
 *
 */
public class RuleServiceException extends RuntimeException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -4115507029260625072L;
    /* The rule engine error code. */
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}
    /**
     * Constructor taking underlying exception as argument.
     * @param errCode the rule engine error code.
     * @param msg additional context dependent error message.
     * @param exception the causing exception.
     */
	public RuleServiceException(final String errCode, final String msg, final Throwable exception) {
		super(msg, exception);
		errorCode = errCode;
	}
    /**
     * Construction taking error code and context dependent message.
     * @param errCode the rule engine error code.
     * @param errMessage additional context dependent error message.
     */
	public RuleServiceException(final String errCode, final String errMessage) {
		super(errMessage);
		errorCode = errCode;
	}
	
}
