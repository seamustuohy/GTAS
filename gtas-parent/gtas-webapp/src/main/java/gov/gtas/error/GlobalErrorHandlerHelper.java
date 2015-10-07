package gov.gtas.error;

import static gov.gtas.constant.DomainModelConstants.UDR_UNIQUE_CONSTRAINT_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaSystemException;

import gov.gtas.constants.ErrorConstants;
import gov.gtas.json.JsonServiceResponse;

public class GlobalErrorHandlerHelper {
	/*
	 * The logger for the Webapp Global Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(GlobalErrorHandlerHelper.class);

	public static JsonServiceResponse createDbErrorResponse(JpaSystemException ex){
 		if(ErrorUtils.isExceptionOfType(ex, "SQLGrammarException")){
 		   logger.error("GTAS Webapp:SQLGrammarException - "+ex.getMessage());
 			return new JsonServiceResponse(ErrorConstants.INVALID_SQL_ERROR_CODE,
 					"There was a data base Error:"
 							+ ex.getMessage(), null);

 		} else if(ErrorUtils.isConstraintViolationException(ex, UDR_UNIQUE_CONSTRAINT_NAME)){
 			logger.error("GTAS Webapp:ConstraintViolationException - "+ex.getMessage());
 			return new JsonServiceResponse(ErrorConstants.DUPLICATE_UDR_ERROR_CODE,
 					"This author has already created a UDR with this title:"
 							+ ex.getMessage(), null);
 		}
 		
 		ex.printStackTrace();
 		return new JsonServiceResponse(ErrorConstants.FATAL_DB_ERROR_CODE,
 				"There was a backend DB error:"
 						+ ex.getMessage(), null);
   	 
     }
}
