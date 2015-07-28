package gov.gtas.error;

import gov.gtas.model.udr.json.error.GtasJsonError;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//@ControllerAdvice
public class WebAppGlobalErrorHandler {
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
		return new GtasJsonError("DUPLICATE_UDR_TITLE",
		"This author has already created a UDR with this title:"
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
