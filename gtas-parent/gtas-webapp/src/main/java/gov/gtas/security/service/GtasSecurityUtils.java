/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Some utility functions related to spring security.
 */
public class GtasSecurityUtils {
    public static String fetchLoggedInUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); //get logged in username
        return userId;
    }
}
