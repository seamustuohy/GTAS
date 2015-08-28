package gov.gtas.svc;

import gov.gtas.constant.UdrConstants;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.Watchlist;

import org.apache.commons.lang3.StringUtils;

/** 
 * Helper class for the UDR service response generation.
 * 
 * @author GTAS3 (AB)
 *
 */
public class WatchlistServiceJsonResponseHelper {
	public static JsonServiceResponse createResponse(boolean success,
			String op, Watchlist wl) {
		return createResponse(success, op, wl, null);
	}
	public static JsonServiceResponse createResponse(boolean success,
			String op, Watchlist wl, String failureReason) {
		JsonServiceResponse resp = null;
		if (success) {
			resp = new JsonServiceResponse(
					JsonServiceResponse.SUCCESS_RESPONSE,
					"Watch List Service",
					op,
					String.format(
							op
									+ " on Watch list with name='%s' and ID='%s' was successful.",
							wl.getWatchlistName(), wl.getId()));
			resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute(
					UdrConstants.UDR_ID_ATTRIBUTE_NAME, String.valueOf(wl
							.getId())));
			resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute(
					UdrConstants.UDR_TITLE_ATTRIBUTE_NAME, String.valueOf(wl
							.getWatchlistName())));
		} else {
			if (wl != null) {
				resp = new JsonServiceResponse(
						JsonServiceResponse.FAILURE_RESPONSE,
						"Watch List Service",
						op,
						String.format(
								op
										+ " on Watch List with name='%s' and ID='%s' failed.",
								wl.getWatchlistName(), wl.getId()));
			} else {
				String msg =null;
				if(StringUtils.isEmpty(failureReason)){
					msg = op + " failed.";
				} else {
					msg = op + " failed " + failureReason + ".";
				}
				resp = new JsonServiceResponse(
						JsonServiceResponse.FAILURE_RESPONSE, "Watch List Service",
						op, msg);
			}

		}
		return resp;
	}
}
