package gov.gtas.constant;

public class WatchlistConstants {
	public static final String WL_KNOWLEDGE_BASE_NAME = "WL Knowledge Base";
	public static final String DELETE_OP_NAME = "delete";
	///////////////////////////////////////////////////////////////////////////////////
	// ERRORS
	///////////////////////////////////////////////////////////////////////////////////
	/*
	 * This is the error code indicating that the watch list to be deleted is not empty.
	 */
	public static final String CANNOT_DELETE_NONEMPTY_WATCHLIST_ERROR_CODE = "CANNOT_DELETE_NONEMPTY_WATCHLIST";

	/*
	 * This is the error message indicating that the watch list to be deleted is not empty.
	 */
	public static final String CANNOT_DELETE_NONEMPTY_WATCHLIST_ERROR_MESSAGE = "Watch list '%s' cannot be deleted since it is not empty.";
}
