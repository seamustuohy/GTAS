package gov.gtas.model.udr.json.error;

import java.io.Serializable;

public class GtasJsonError implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -2847857082079847482L;
	
	private String errorCode;
	private String errorMessage;
	
	public GtasJsonError(final String code, final String msg){
		this.errorCode = code;
		this.errorMessage = msg;
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
