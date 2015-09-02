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
	public static final String UDR_GET_BY_AUTHOR_TITLE = "/{authorId}/{title}";
	public static final String UDR_GET_BY_ID = "/get/{id}";
	public static final String UDR_GETALL = "/list/{userId}";
	public static final String UDR_GETDRL = "/drl";
	public static final String UDR_GETDRL_BY_NAME = "/drl/{kbName}";
	public static final String UDR_POST = "/{userId}";
	public static final String UDR_PUT = "/{userId}";
	public static final String UDR_DELETE = "/{userId}/{id}";
	public static final String UDR_TEST = "/testUdr";

	//Targeting URI
	public static final String TARGET_ONE_APIS_MSG = "/apis/{id}";
	public static final String TARGET_ALL_APIS = "/apis";
	public static final String TARGET_ALL_PNR = "/pnr";
	public static final String TARGET_ALL_MSG = "/target";
	
	//WATCH LIST URI
	public static final String WL_ROOT = "/wl";
	public static final String WL_GET_BY_NAME = "/{name}";
	public static final String WL_GETALL = "/list";
	public static final String WL_GETDRL = "/drl";
	public static final String WL_POST = "/{userId}";
	public static final String WL_PUT = "/{userId}";
	public static final String WL_DELETE = "/{name}";
	public static final String WL_COMPILE = "/compile";
	public static final String WL_TEST = "/testwl";

	
	// Query Messages
	public static final String QUERY_SAVED_SUCCESS_MSG = "Query saved successfully";
	public static final String QUERY_EDITED_SUCCESS_MSG = "Query updated successfully";
	public static final String QUERY_DELETED_SUCCESS_MSG = "Query deleted successfully";
	public static final String QUERY_SERVICE_ERROR_MSG = "An error occurred while trying to process your request";
	
	public static final String QUERYOBJECT_OBJECTNAME = "queryObject";
	public static final String QUERYREQUEST_OBJECTNAME = "queryRequest";
	
	// Hits Summary
	public static final String HITS_SUMMARY_SERVICE = "/hit";
	public static final String HITS_SUMMARY_RULES_BY_TRAVELER_ID = "/traveler/{id}";
	
	//Security Roles
	public static final String MANAGE_RULES_ROLE = "MANAGE_RULES";
	public static final String MANAGE_QUERIES_ROLE = "MANAGE_QUERIES";
	public static final String VIEW_FLIGHT_PASSENGERS_ROLE = "VIEW_FLIGHT_PASSENGERS";
	public static final String MANAGE_WATCHLIST_ROLE = "MANAGE_WATCHLIST";
	public static final String ADMIN_ROLE = "ADMIN";
	
	//Security URL Paths
	public static final String LOGIN_PAGE = "/login.jsp";
	public static final String HOME_PAGE = "/home.action";
	public static final String LOGOUT_MAPPING = "/logout.action";
	

}
