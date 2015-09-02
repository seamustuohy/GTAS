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

		super.addErrorCodeToHandlerMap(
				WatchlistConstants.MISSING_DELETE_OR_UPDATE_ITEM_ERROR_CODE,
				WatchlistConstants.MISSING_DELETE_OR_UPDATE_ITEM_ERROR_MESSAGE);
		
		super.addErrorCodeToHandlerMap(
				WatchlistConstants.CANNOT_SET_ID_FOR_CREATE_ITEM_ERROR_CODE,
				WatchlistConstants.CANNOT_SET_ID_FOR_CREATE_ITEM_ERROR_MESSAGE);
	}
}
