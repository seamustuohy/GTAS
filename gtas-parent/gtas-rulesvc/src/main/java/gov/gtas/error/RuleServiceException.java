package gov.gtas.error;
/**
 * Exception class for errors generated during Rule Engine execution.
 * @author GTAS3 (AB)
 *
 */
public class RuleServiceException extends CommonServiceException {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -4115507029260625072L;
    /**
     * Constructor taking underlying exception as argument.
     * @param errCode the rule engine error code.
     * @param msg additional context dependent error message.
     * @param exception the causing exception.
     */
	public RuleServiceException(final String errCode, final String msg, final Throwable exception) {
		super(errCode, msg, exception);
	}
    /**
     * Construction taking error code and context dependent message.
     * @param errCode the rule engine error code.
     * @param errMessage additional context dependent error message.
     */
	public RuleServiceException(final String errCode, final String errMessage) {
		super(errCode, errMessage);
	}
	
}
