package gov.gtas.security.filters;


import gov.gtas.security.service.UserDaoImpl;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component("authenticationProvider")
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserDaoImpl userDetailsDao;
 
	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
	
	
	@Override
	public Authentication authenticate(Authentication authentication) 
          throws AuthenticationException {
 
	  try {
 logger.info(" BEFORE AUTH >>>> ");
		Authentication auth = super.authenticate(authentication);
		logger.info(" AFTER AUTH >>>> ");
		//if reach here, means login success, else an exception will be thrown
		//reset the user_attempts
//		userDetailsDao.resetFailAttempts(authentication.getName());
 
		return auth;
 
	  } catch (BadCredentialsException e) {	
 
		//invalid login, update to user_attempts
	//	userDetailsDao.updateFailAttempts(authentication.getName());
		throw e;
 
	  } catch (LockedException e){
 
	/*	//this user is locked!
		String error = "";
		UserAttempts userAttempts = 
                    userDetailsDao.getUserAttempts(authentication.getName());
 
               if(userAttempts!=null){
			Date lastAttempts = userAttempts.getLastModified();
			error = "User account is locked! <br><br>Username : " 
                           + authentication.getName() + "<br>Last Attempts : " + lastAttempts;
		}else{
			error = e.getMessage();
		}
 */
	  throw new LockedException("");
	}
 
	}
 
	
}
