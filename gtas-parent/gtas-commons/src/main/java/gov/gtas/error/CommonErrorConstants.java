package gov.gtas.error;

/**
 * Common Error Constants for GTAS.
 * 
 * @author GTAS3 (AB)
 *
 */
public class CommonErrorConstants {
	// //////////////////////////////////////////////////////////////////////////////////////
	// ERROR CODES
	// //////////////////////////////////////////////////////////////////////////////////////
	/*
	 * This is the error code for an internal system error indicating invalid
	 * program logic causing a service API method being called with a null
	 * argument.
	 */
	public static final String NULL_ARGUMENT_ERROR_CODE = "NULL_ARGUMENT";
	/*
	 * This is the error code indicating that the
	 * user indicated by a supplied userId cannot be found.
	 */
	public static final String INVALID_USER_ID_ERROR_CODE = "INVALID_USER_ID";

	/*
	 * This is the error code indicating that the
	 * a query returned no result.
	 */
	public static final String QUERY_RESULT_EMPTY_ERROR_CODE = "QUERY_RESULT_EMPTY";
	
	/*
	 * This is the error code for an Unexpected internal system error.
	 */
	public static final String SYSTEM_ERROR_CODE = "SYSTEM_ERROR";

	// //////////////////////////////////////////////////////////////////////////////////////
	// ERROR Messages
	// //////////////////////////////////////////////////////////////////////////////////////
	/*
	 * This is the error message for an internal system error indicating invalid
	 * program logic causing a service API method being called with a null
	 * argument.
	 */
	public static final String NULL_ARGUMENT_ERROR_MESSAGE = "The parameter '%s' passed to the method '%s' should not be null.";
	/*
	 * This is the error message indicating that the
	 * user indicated by a supplied userId cannot be found.
	 */
	public static final String INVALID_USER_ID_ERROR_MESSAGE = "The user id '%s' does not represent a valid user.";
	
	/*
	 * This is the error message indicating that the
	 * a query returned no result.
	 */
	public static final String QUERY_RESULT_EMPTY_ERROR_MESSAGE = "Query for %s using '%s' returned no result.";
	/*
	 * This is the error message for an Unexpected internal system error.
	 */
	public static final String SYSTEM_ERROR_MESSAGE = "There was an Internal System Error with ID %s. Please contact the HelpDesk for details.";
}
