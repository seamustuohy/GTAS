package gov.gtas.svc;

import gov.gtas.model.udr.UdrConstants;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.JsonServiceResponse;

/** 
 * Helper class for the UDR service response generation.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrServiceJsonResponseHelper {
	public static JsonServiceResponse createResponse(boolean success,
			String op, UdrRule rule) {
		JsonServiceResponse resp = null;
		if (success) {
			resp = new JsonServiceResponse(
					JsonServiceResponse.SUCCESS_RESPONSE,
					"UDR Service",
					op,
					String.format(
							op
									+ " on UDR Rule with title='%s' and ID='%s' was successful.",
							rule.getTitle(), rule.getId()));
			resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute(
					UdrConstants.UDR_ID_ATTRIBUTE_NAME, String.valueOf(rule
							.getId())));
			resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute(
					UdrConstants.UDR_TITLE_ATTRIBUTE_NAME, String.valueOf(rule
							.getTitle())));
		} else {
			if (rule != null) {
				resp = new JsonServiceResponse(
						JsonServiceResponse.FAILURE_RESPONSE,
						"UDR Service",
						op,
						String.format(
								op
										+ " on UDR Rule with title='%s' and ID='%s' failed.",
								rule.getTitle(), rule.getId()));
			} else {
				resp = new JsonServiceResponse(
						JsonServiceResponse.FAILURE_RESPONSE, "UDR Service",
						op, op + " failed.");

			}

		}
		return resp;
	}
}
