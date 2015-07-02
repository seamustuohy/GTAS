package gov.gtas.model.udr.json;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
	
	public static final String SUCCESS_RESPONSE = "SUCCESS";
	public static final String FAILURE_RESPONSE = "FAILED";

	private String status;
	private String serviceName;
	private String request;
	private String message;
	private List<ServiceResponseDetailAttribute> responseDetails;
	
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
		this.responseDetails = new LinkedList<JsonServiceResponse.ServiceResponseDetailAttribute>();
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
	
	/**
	 * @return the responseDetails
	 */
	public List<ServiceResponseDetailAttribute> getResponseDetails() {
		return Collections.unmodifiableList(responseDetails);
	}
	/**
	 * @param responseDetail the responseDetail to add.
	 */
	public void addResponseDetails(
			ServiceResponseDetailAttribute responseDetail) {
		this.responseDetails.add(responseDetail);
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	public static class ServiceResponseDetailAttribute{
		private String attributeName;
		private String attributeValue;
		public ServiceResponseDetailAttribute(String attrName, String attrValue){
			attributeName = attrName;
			attributeValue = attrValue;
		}
		/**
		 * @return the attributeName
		 */
		public String getAttributeName() {
			return attributeName;
		}
		/**
		 * @return the attributeValue
		 */
		public String getAttributeValue() {
			return attributeValue;
		}
		
	}
}
