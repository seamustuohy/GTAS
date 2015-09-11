package gov.gtas.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Some utility functions related to spring security.
 * @author GTAS3 (AB)
 *
 */
public class GtasSecurityUtils {
    public static String fetchLoggedInUserId(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String userId = auth.getName(); //get logged in username
        return userId;
    }
}
