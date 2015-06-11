package gov.cbp.taspd.gtas.error;

public class RuleServiceException extends RuntimeException {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -4115507029260625072L;

	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

//	public RuleServiceException(String arg0, Throwable arg1, boolean arg2,
//			boolean arg3) {
//		super(arg0, arg1, arg2, arg3);
//	}

	public RuleServiceException(final String msg, final Throwable exception) {
		super(msg, exception);
	}

	public RuleServiceException(final String errCode, final String errMessage) {
		super(errMessage);
		errorCode = errCode;
	}
	
}
