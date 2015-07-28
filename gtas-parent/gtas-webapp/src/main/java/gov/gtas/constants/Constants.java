package gov.gtas.constants;

public final class Constants {

	// Query URI
	public static final String QUERY_SERVICE = "/query";
	public static final String INIT = "/init";
	public static final String RUN_QUERY_FLIGHT_URI = "/queryFlights";
	public static final String RUN_QUERY_PASSENGER_URI = "/queryPassengers";
	public static final String SAVE_QUERY_URI = "/saveQuery";
	public static final String LIST_QUERY_URI = "/listQuery";
	public static final String EDIT_QUERY_URI = "/editQuery";
	public static final String DELETE_QUERY_URI = "/deleteQuery";
	
	//UDR URI
	public static final String UDR_ROOT = "/udr";
	public static final String UDR_GET = "/{userId}/{title}";
	public static final String UDR_GET_BY_ID = "/get/{id}";
	public static final String UDR_GETALL = "/list/{userId}";
	public static final String UDR_GETDRL = "/drl";
	public static final String UDR_GETDRL_BY_NAME = "/drl/{kbName}";
	public static final String UDR_POST = "/{userId}";
	public static final String UDR_PUT = "/{userId}";
	public static final String UDR_DELETE = "/{userId}/{id}";
	public static final String UDR_TEST = "/testUdr";
	public static final String UDR_TARGET = "/apis/{id}";
	//public static final String UDR_RULE = "/rules";
	
	// Query Messages
	public static final String QUERY_SAVED_SUCCESS_MSG = "Query saved successfully";
	public static final String QUERY_EDITED_SUCCESS_MSG = "Query updated successfully";
	public static final String QUERY_DELETED_SUCCESS_MSG = "Query deleted successfully";
	public static final String QUERY_SERVICE_ERROR_MSG = "An error occurred while trying to process your request";
	
	public static final String QUERYOBJECT_OBJECTNAME = "queryObject";
	public static final String QUERYREQUEST_OBJECTNAME = "queryRequest";
}
