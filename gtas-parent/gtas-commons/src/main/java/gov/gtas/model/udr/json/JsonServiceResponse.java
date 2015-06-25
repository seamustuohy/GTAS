package gov.gtas.model.udr.json;

import java.io.Serializable;
/**
 * Rule meta-data JSON object class.
 * @author GTAS3 (AB)
 *
 */
public class JsonServiceResponse implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -1376823917772400644L;
	
	private String status;
	private String serviceName;
	private String request;
	private String message;
	
	/**
	 * Constructor.
	 * @param status
	 * @param serviceName
	 * @param request
	 * @param message
	 */
	public JsonServiceResponse(String status, String serviceName, String request, String message){
		this.status = status;
		this.serviceName = serviceName;
		this.request = request;
		this.message = message;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
