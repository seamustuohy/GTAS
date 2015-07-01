package gov.gtas.constants;

public final class Constants {

	public static final String QUERY_SERVICE = "/query";
	public static final String INIT = "/init";
	public static final String RUN_QUERY_FLIGHT_URI = "/queryFlights";
	public static final String RUN_QUERY_PASSENGER_URI = "/queryPassengers";
	public static final String SAVE_QUERY_URI = "/saveQuery";
	public static final String VIEW_QUERY_URI = "/viewQuery";
	public static final String EDIT_QUERY_URI = "/editQuery";
	public static final String DELETE_QUERY_URI = "/deleteQuery";
	
	//UDR URI
	public static final String UDR_ROOT = "/udr";
	public static final String UDR_GET = "/{userId}/{title}";
	public static final String UDR_GET_BY_ID = "/get/{id}";
	public static final String UDR_GETALL = "/list/{userId}";
	public static final String UDR_POST = "/{userId}";
	public static final String UDR_PUT = "/{userId}";
	public static final String UDR_DELETE = "/{userId}/{id}";
	public static final String UDR_TEST = "/testUdr";
	public static final String UDR_RULE = "/rules";
	
}
