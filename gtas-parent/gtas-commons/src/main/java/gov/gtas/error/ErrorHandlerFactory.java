package gov.gtas.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating error handlers.
 * @author GTAS3 (AB)
 *
 */
public class ErrorHandlerFactory {
	/*
	 * The logger for the Error Handler factory.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ErrorHandlerFactory.class);
	
	private static ErrorHandler errorHandler;
	/**
	 * Creates the error handler chain.<br>
	 * Note: It is expected that different modules will create their own specialized
	 * error handler as a spring component (i.e., with the @Component annotation).
	 * This method is expected to be called in the @PostConstruct method.
	 * @param errorHandler the handler to register.
	 */
    public static synchronized void registerErrorHandler(ErrorHandler errorHandler){
    	if(ErrorHandlerFactory.errorHandler != null){
    		ErrorHandlerFactory.errorHandler.addErrorHandlerDelegate(errorHandler);
    	}else{
    		ErrorHandlerFactory.errorHandler = errorHandler;
    	}
    }
    /**
     * Gets the error handler.
     * @return the error handler.
     */
    public static ErrorHandler getErrorHandler(){
    	if(ErrorHandlerFactory.errorHandler != null){
    		return ErrorHandlerFactory.errorHandler;
    	}else{
    		logger.info("GtasErrorHandlerFactory - no error handler registered.");
    		return new BasicErrorHandler();
    	}
    }
}
