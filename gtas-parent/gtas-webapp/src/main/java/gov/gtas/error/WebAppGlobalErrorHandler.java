package gov.gtas.error;

import static gov.gtas.constant.DomainModelConstants.UDR_UNIQUE_CONSTRAINT_NAME;
import gov.gtas.json.JsonServiceResponse;
import gov.gtas.model.udr.json.error.GtasJsonError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebAppGlobalErrorHandler {
	/*
	 * The logger for the Webapp Global Error Handler
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WebAppGlobalErrorHandler.class);
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CommonServiceException.class)
	public @ResponseBody JsonServiceResponse handleError(CommonServiceException ex) {
		ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
		ErrorDetails err = errorHandler.processError(ex);
		return new JsonServiceResponse(err.getFatalErrorCode(),
				err.getFatalErrorMessage(), null);
	}
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public @ResponseBody JsonServiceResponse handleError(HttpMessageNotReadableException ex) {
		return new JsonServiceResponse(WebappErrorConstants.MALFORMED_JSON_ERROR_CODE,
				String.format(WebappErrorConstants.MALFORMED_JSON_ERROR_MESSAGE, ex.getMessage()), null);		
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(JpaSystemException.class)
	public @ResponseBody JsonServiceResponse handleError(JpaSystemException ex) {
		if(ErrorUtils.isExceptionOfType(ex, "SQLGrammarException")){
		   logger.error("GTAS Webapp:SQLGrammarException - "+ex.getMessage());
			return new JsonServiceResponse("DB_ERROR",
					"There was a data base Error:"
							+ ex.getMessage(), null);

		} else if(ErrorUtils.isConstraintViolationException(ex, UDR_UNIQUE_CONSTRAINT_NAME)){
			logger.error("GTAS Webapp:ConstraintViolationException - "+ex.getMessage());
			return new JsonServiceResponse("DUPLICATE_UDR_TITLE",
					"This author has already created a UDR with this title:"
							+ ex.getMessage(), null);
		}
		
		ex.printStackTrace();
		return new JsonServiceResponse("UNKNOWN_DB_ERROR",
				"There was a backend DB error:"
						+ ex.getMessage(), null);
		
	}
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
	public @ResponseBody JsonServiceResponse handleError(TypeMismatchException ex) {
		ex.printStackTrace();
		return new JsonServiceResponse("INVALID_INPUT_URL",
		"The REST path variable could not be parsed:"
				+ ex.getMessage(), null);
	}
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public @ResponseBody JsonServiceResponse handleError(HttpRequestMethodNotSupportedException ex) {
		ex.printStackTrace();
		return new JsonServiceResponse("INVALID_INPUT_URL_OR_UNKNOWN_METHOD",
		"The URL could not be dispatched:"
				+ ex.getMessage(), null);
	}
   
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public @ResponseBody JsonServiceResponse handleError(Exception ex) {
		ex.printStackTrace();
		ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
		ErrorDetails err = errorHandler.processError(ex);
		return new JsonServiceResponse(err.getFatalErrorCode(),
					err.getFatalErrorMessage(), err.getErrorDetails());			
	}

}
