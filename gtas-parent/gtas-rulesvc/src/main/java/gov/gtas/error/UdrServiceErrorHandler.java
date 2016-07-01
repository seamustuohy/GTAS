/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.error;

import gov.gtas.constant.RuleServiceConstants;

/**
 * Error Handler for the UDR Service.
 */
public class UdrServiceErrorHandler extends BasicErrorHandler {
    public UdrServiceErrorHandler() {
        super();
        super.addErrorCodeToHandlerMap(
                RuleServiceConstants.INCOMPLETE_TREE_ERROR_CODE,
                RuleServiceConstants.INCOMPLETE_TREE_ERROR_MESSAGE);

    }
}
