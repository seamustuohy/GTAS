package gov.gtas.error;

import gov.gtas.constant.RuleServiceConstants;

/**
 * Error Handler for the UDR Service.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrServiceErrorHandler extends BasicErrorHandler {
	public UdrServiceErrorHandler() {
		super();
		super.addErrorCodeToHandlerMap(
				RuleServiceConstants.INCOMPLETE_TREE_ERROR_CODE,
				RuleServiceConstants.INCOMPLETE_TREE_ERROR_MESSAGE);

	}
}
