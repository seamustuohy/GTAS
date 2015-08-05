package gov.gtas.error;

import gov.gtas.model.udr.json.error.GtasJsonError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebAppGlobalErrorHandler {
	/*
	 * The logger for the Webapp Global Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WebAppGlobalErrorHandler.class);
	
	@ExceptionHandler(CommonServiceException.class)
	public @ResponseBody GtasJsonError handleError(CommonServiceException ex) {
		ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
		ErrorDetails err = errorHandler.processError(ex);
		return new GtasJsonError(err.getFatalErrorCode(),
				err.getFatalErrorMessage());
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public @ResponseBody GtasJsonError handleError(HttpMessageNotReadableException ex) {
		return new GtasJsonError(WebappErrorConstants.MALFORMED_JSON_ERROR_CODE,
				String.format(WebappErrorConstants.MALFORMED_JSON_ERROR_MESSAGE, ex.getMessage()));		
	}

	@ExceptionHandler(JpaSystemException.class)
	public @ResponseBody GtasJsonError handleError(JpaSystemException ex) {
		if(ErrorUtils.isExceptionOfType(ex, "SQLGrammarException")){
		   logger.error("GTAS Webapp:SQLGrammarException - "+ex.getMessage());
			return new GtasJsonError("DB_ERROR",
					"There was a data base Error:"
							+ ex.getMessage());

		} else if(ErrorUtils.isExceptionOfType(ex, "ConstraintViolationException")){
			logger.error("GTAS Webapp:ConstraintViolationException - "+ex.getMessage());
			return new GtasJsonError("DUPLICATE_UDR_TITLE",
					"This author has already created a UDR with this title:"
							+ ex.getMessage());
		}
		
		ex.printStackTrace();
		return new GtasJsonError("UNKNOWN_DB_ERROR",
				"There was a backend DB error:"
						+ ex.getMessage());
		
	}
    @ExceptionHandler(TypeMismatchException.class)
	public @ResponseBody GtasJsonError handleError(TypeMismatchException ex) {
		ex.printStackTrace();
		return new GtasJsonError("INVALID_INPUT_URL",
		"The REST path variable could not be parsed:"
				+ ex.getMessage());
	}
    
	@ExceptionHandler(Exception.class)
	public @ResponseBody GtasJsonError handleError(Exception ex) {
		ex.printStackTrace();
		ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
		ErrorDetails err = errorHandler.processError(ex);
		return new GtasJsonError(err.getFatalErrorCode(),
				err.getFatalErrorMessage());
	}

}
