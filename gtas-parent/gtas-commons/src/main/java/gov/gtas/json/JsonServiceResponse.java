package gov.gtas.json;

import static gov.gtas.constant.JsonResponseConstants.ATTR_ERROR_CODE;
import static gov.gtas.constant.JsonResponseConstants.ATTR_ERROR_DETAIL;
import static gov.gtas.constant.JsonResponseConstants.ATTR_RESULT;
import gov.gtas.enumtype.Status;

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
	private Status status;
	private String message;
	private List<ServiceResponseDetailAttribute> responseDetails;
	
	/**
	 * Basic constructor.
	 * @param status the status.
	 * @param message response message.
	 */
	public JsonServiceResponse(Status status, String message){
		this.status = status;
		this.message = message;
		this.responseDetails = new LinkedList<JsonServiceResponse.ServiceResponseDetailAttribute>();
	}
	/**
	 * Constructor to support query builder response.
	 * @param status the status.
	 * @param message response message.
	 * @param result the result object.
	 */
	public JsonServiceResponse(Status status, String message, Serializable result){
		this.status = status;
		this.message = message;
		this.responseDetails = new LinkedList<JsonServiceResponse.ServiceResponseDetailAttribute>();
		responseDetails.add(new ServiceResponseDetailAttribute(ATTR_RESULT, result));
	}
	/**
	 * Constructor for error response.
	 * @param request
	 * @param message
	 */
	public JsonServiceResponse(String errorCode, String message, String[] errorDetail){
		this.status = Status.FAILURE;
		this.message = message;
		this.responseDetails = new LinkedList<JsonServiceResponse.ServiceResponseDetailAttribute>();
		responseDetails.add(new ServiceResponseDetailAttribute(ATTR_ERROR_CODE, errorCode));
		if(errorDetail != null && errorDetail.length > 0){
		    responseDetails.add(new ServiceResponseDetailAttribute(ATTR_ERROR_DETAIL, errorDetail));
		}
	}
	/**
	 * Fetches the value of an attribute by name.
	 * @param responseDetailName the name of the attribute to fetch.
	 * @return the attribute value or null if not found.
	 */
	public String findResponseDetailValue(final String responseDetailName){
		String ret = null;
		for(ServiceResponseDetailAttribute attr:this.responseDetails){
			if(responseDetailName.equals(attr.getAttributeName())){
				ret = (String)attr.getAttributeValue();
				break;
			}
		}
		return ret;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
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
		private Serializable attributeValue;
		public ServiceResponseDetailAttribute(String attrName, String attrValue){
			attributeName = attrName;
			attributeValue = attrValue;
		}
		public ServiceResponseDetailAttribute(String attrName, Serializable attrValue){
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
		public Serializable getAttributeValue() {
			return attributeValue;
		}
		
	}
}
