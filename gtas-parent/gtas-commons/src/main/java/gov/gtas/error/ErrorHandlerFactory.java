package gov.gtas.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating error handlers.
 * 
 * @author GTAS3 (AB)
 *
 */
public class ErrorHandlerFactory {
	/*
	 * The logger for the Error Handler factory.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ErrorHandlerFactory.class);
	/*
	 * This is the first element in the error handler chain. It is expected that
	 * different modules will create their own specialized error handler and
	 * attach it to the chain by calling registerErrorHandler.
	 */
	private static final ErrorHandler errorHandler = new BasicErrorHandler();

	/**
	 * Creates the error handler chain.<br>
	 * Note: It is expected that different modules will create their own
	 * specialized error handler by deriving from the BasicErrorHandler class
	 * and adding error codes and exception processors. This method is expected
	 * to be called in the @PostConstruct method of a spring bean. Otherwise
	 * this method should be called from the static initializer of the calling class.
	 * @see gov.gtas.controller.UdrManagementController#addErrorHandlerDelegate(gov.gtas.error.GtasErrorHandler) 
	 * @param errorHandler
	 *            the handler to register.
	 */
	public static synchronized void registerErrorHandler(
			ErrorHandler errorHandler) {
		
			ErrorHandlerFactory.errorHandler
					.addErrorHandlerDelegate(errorHandler);
	}

	/**
	 * Gets the error handler.
	 * 
	 * @return the error handler.
	 */
	public static ErrorHandler getErrorHandler() {
		if (ErrorHandlerFactory.errorHandler != null) {
			return ErrorHandlerFactory.errorHandler;
		} else {
			logger.info("GtasErrorHandlerFactory - no error handler registered.");
			return new BasicErrorHandler();
		}
	}
}
