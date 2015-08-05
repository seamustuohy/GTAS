package gov.gtas.error;
/**
 * Error processing utility functions.
 * 
 * @author GTAS3 (AB)
 *
 */
public class ErrorUtils {
    public static boolean isExceptionOfType(Throwable exception, String typeName){
    	boolean ret = false;
    	Throwable cause = exception;
    	while(cause != null){
    		String name = cause.getClass().getName();
    		ret = name.indexOf(typeName) >= 0;
    		if(ret){
    			break;
    		} else {
    			cause = cause.getCause();
    		}
    	} 
    	return ret;
    }
}
