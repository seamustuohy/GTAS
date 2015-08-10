package gov.gtas.querybuilder.constants;


public final class Constants {

	public static final String SELECT_DISTINCT = "select distinct";
	public static final String FROM = "from";
	public static final String WHERE = "where";
	public static final String AND = "and";
	public static final String JOIN = " join ";
	public static final String JOIN_FETCH = " join fetch ";
	public static final String FLIGHT_REF = ".flights ";
	public static final String PASSENGER_REF = ".passengers ";
	public static final String DOCUMENT_REF = ".documents ";
	
	// Entities
	public static final String ADDRESS = "ADDRESS";
	public static final String CREDITCARD = "CREDITCARD";
	public static final String DOCUMENT = "DOCUMENT";
	public static final String EMAIL = "EMAIL";
	public static final String FLIGHT = "FLIGHT";
	public static final String FREQUENTFLYER = "FREQUENTFLYER";
	public static final String HITS = "HITS";
	public static final String PASSENGER = "PASSENGER";
	public static final String PHONE = "PHONE";
	public static final String PNR = "PNR";
	public static final String TRAVELAGENCY = "TRAVELAGENCY";
	
	public static final String QUERYOBJECT_OBJECTNAME = "queryObject";
	public static final String USERQUERY_OBJECTNAME = "userQuery";
	
	public static final String QUERY_EXISTS_ERROR_MSG = "A query with the same title already exists. Please rename this query or edit the existing one.";
	public static final String QUERY_DOES_NOT_EXIST_ERROR_MSG = "Query cannot be found.";
}
