package gov.gtas.model.udr.json.error;

import java.io.Serializable;
/**
 * Class representing JSON error response.
 * @author GTAS3 (AB)
 *
 */
public class GtasJsonError implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -2847857082079847482L;
	
	private String errorCode;
	private String errorMessage;
	private String[] errorDetail;
	
	public GtasJsonError(final String code, final String msg){
		this.errorCode = code;
		this.errorMessage = msg;
	}
	public GtasJsonError(final String code, final String msg, final String[] details){
		this.errorCode = code;
		this.errorMessage = msg;
		this.errorDetail = details;
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
	/**
	 * @return the errorDetail
	 */
	public String[] getErrorDetail() {
		return errorDetail;
	}
	/**
	 * @param errorDetail the errorDetail to set
	 */
	public void setErrorDetail(String[] errorDetail) {
		this.errorDetail = errorDetail;
	}

}
