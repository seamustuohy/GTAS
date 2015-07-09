package gov.gtas.security.filters;

import gov.gtas.security.service.AuthUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



public class UserAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthUserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		// TODO Auto-generated method stub
		
//		logger.info("USER -- "+request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY));
//		
//		logger.info("USERAuthFilter -- "+super.getAuthenticationManager());
		
	//	logger.info("Return String"+userDetailsService.getUsersByUsernameQuery());
		
	//	UserDetails userDetails = userDetailsService.loadUserByUsername(SPRING_SECURITY_FORM_USERNAME_KEY);
		
		
		return super.attemptAuthentication(request, response);
	}
	
	@Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
	
	@Override
	protected String obtainPassword(HttpServletRequest request) {
		
		return super.obtainPassword(request);
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		
		return super.obtainUsername(request);
	}

	
	
	
}
