package gov.gtas.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;


public class CsrfHeaderFilter extends OncePerRequestFilter {

	protected static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
    protected static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";
    protected static final String RESPONSE_PARAM_NAME = "X-CSRF-PARAM";
    protected static final String RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN";

	
	
	@Override
	  protected void doFilterInternal(HttpServletRequest request,
	      HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
//	    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
//	        .getName());
//	    
	    CsrfToken csrf = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
	    
	    if (csrf != null) {
	      Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
	      String token = csrf.getToken();
//	     // response.addCookie(new Cookie("X-XSRF-TOKEN", token));
//	      if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
//	        cookie = new Cookie("XSRF-TOKEN", token);
	        //cookie.s
	      cookie = new Cookie("CSRF-TOKEN", token);
//	      if(cookie == null){cookie = WebUtils.getCookie(request, "X-CSRF-TOKEN");}
//	      if(cookie != null){
	        cookie.setPath("/gtas");
	        response.addCookie(cookie);
	      //}
	    	response.setHeader(RESPONSE_HEADER_NAME, csrf.getHeaderName());
            response.setHeader(RESPONSE_PARAM_NAME, csrf.getParameterName());
            response.setHeader(RESPONSE_TOKEN_NAME , csrf.getToken());
	    	
	      }
	    
	    filterChain.doFilter(request, response);
	  }
}