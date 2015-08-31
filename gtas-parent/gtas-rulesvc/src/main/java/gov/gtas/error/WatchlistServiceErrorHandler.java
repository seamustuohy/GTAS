package gov.gtas.error;

import gov.gtas.constant.WatchlistConstants;

/**
 * Error Handler for the UDR Service.
 * 
 * @author GTAS3 (AB)
 *
 */
public class WatchlistServiceErrorHandler extends BasicErrorHandler {
	public WatchlistServiceErrorHandler() {
		super();
		super.addErrorCodeToHandlerMap(
				WatchlistConstants.CANNOT_DELETE_NONEMPTY_WATCHLIST_ERROR_CODE,
				WatchlistConstants.CANNOT_DELETE_NONEMPTY_WATCHLIST_ERROR_MESSAGE);

	}
}
