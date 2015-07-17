package gov.gtas.util;
/**
 * Utility methods for validation.
 * @author GTAS3 (AB)
 *
 */
public class ValidationUtils {
    public static boolean isStringInList(final String target, String... args){
    	boolean ret = false;
    	if(target != null){
    		for(String match:args){
    			if(target.equals(match)){
    				ret = true;
    				break;
    			}
    		}
    	}
    	return ret;
    }
    public static boolean isStringTruthy(final String target){
    	return isStringInList(target, "TRUE","True","T","true","Yes","Y","yes","YES");
    }
    
}
