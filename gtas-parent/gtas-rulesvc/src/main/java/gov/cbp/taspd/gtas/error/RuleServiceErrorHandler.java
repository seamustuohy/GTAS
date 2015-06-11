package gov.cbp.taspd.gtas.error;

import gov.cbp.taspd.gtas.constant.RuleServiceConstants;

import org.springframework.stereotype.Component;
/**
 * Error Handler for the Rule Service related functionality.
 * 
 * @author GTAS3 (AB)
 *
 */
@Component
public class RuleServiceErrorHandler {
   public RuleServiceException createException(final String errorCode, final Object... args){
	   RuleServiceException ret = null;
	   switch(errorCode){
	   case RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE:
		   ret = new RuleServiceException(RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE, 
				   String.format(RuleServiceConstants.NULL_ARGUMENT_ERROR_MESSAGE, args));
		   break;
	   case RuleServiceConstants.RULE_COMPILE_ERROR_CODE:
		   ret = new RuleServiceException(RuleServiceConstants.RULE_COMPILE_ERROR_CODE, 
				   String.format(RuleServiceConstants.RULE_COMPILE_ERROR_MESSAGE, args[0]));
		   break;
	    default:
		   break;
	   }
	   
	   return ret;
   }
}
