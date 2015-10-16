package gov.gtas.security;


import java.io.IOException;

import gov.gtas.constants.Constants;
import gov.gtas.security.filters.UserAuthenticationFilter;
import gov.gtas.security.service.AuthUserDetailsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 *
 * The Spring Security configuration for the application - its a form login config with authentication via session cookie (once logged in),
 * with fallback to HTTP Basic for non-browser clients.
 *
 * The CSRF token is put on the reply as a header via a filter, as there is no server-side rendering on this app.
 *
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AppSecurityConfig.class);

    @Autowired
  //  private AuthUserDetailsService userDetailsService;
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    DataSource dataSource;

   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	   auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

       
    @Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    
        // Custom Success and Failure Authentication Handler
    	//
        SavedRequestAwareAuthenticationSuccessHandler savedReqHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        savedReqHandler.setAlwaysUseDefaultTargetUrl(true);
        savedReqHandler.setDefaultTargetUrl(Constants.HOME_PAGE);
        savedReqHandler.setTargetUrlParameter(Constants.HOME_PAGE);
        
        // Custom Redirect Strategy to cope with merged URLs, as well as dictate custom forwarding behavior
        //
        
        AuthenticationEntryPoint indexEntryPoint = new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request,
					HttpServletResponse response, AuthenticationException authException)
					throws IOException, ServletException {
				
				RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
				redirectStrategy.sendRedirect(request, response, Constants.HOME_PAGE);
				return;
			}
		};
        
	  // Application specific Security mappings go here. Even though we can create multiple HTTP Security Authorization instances,  
	  // for better maintainability, we will add/edit all the settings in one HTTP Security instance.
		
      http
        .authorizeRequests()
	        .antMatchers("/**/*").permitAll()
	        .antMatchers(Constants.LOGIN_PAGE).permitAll()
	        .antMatchers(Constants.HOME_PAGE).hasAnyAuthority(Constants.MANAGE_RULES_ROLE,Constants.MANAGE_QUERIES_ROLE,Constants.VIEW_FLIGHT_PASSENGERS_ROLE,Constants.MANAGE_WATCHLIST_ROLE,Constants.ADMIN_ROLE)
			.antMatchers("/travelers").hasAnyAuthority(Constants.MANAGE_RULES_ROLE,Constants.MANAGE_QUERIES_ROLE,Constants.VIEW_FLIGHT_PASSENGERS_ROLE,Constants.MANAGE_WATCHLIST_ROLE,Constants.ADMIN_ROLE)
			.antMatchers("/query**").hasAnyAuthority(Constants.MANAGE_QUERIES_ROLE, Constants.ADMIN_ROLE)
			.antMatchers("/index.html").denyAll()//.access("/home.action")//
			.anyRequest().authenticated()
        .and()
        .csrf().disable()
        .formLogin()
        .defaultSuccessUrl(Constants.HOME_PAGE)
        .loginProcessingUrl("/j_spring_security_check")
        .usernameParameter("j_username")
        .passwordParameter("j_password")
        .failureHandler(new SimpleUrlAuthenticationFailureHandler()).failureUrl(Constants.LOGIN_PAGE)
		.successHandler(new AjaxAuthenticationSuccessHandler(savedReqHandler))
        .defaultSuccessUrl(Constants.HOME_PAGE)
        .loginPage(Constants.LOGIN_PAGE)
        .and().logout().logoutUrl(Constants.LOGOUT_MAPPING).logoutSuccessUrl(Constants.LOGIN_PAGE).permitAll()
        .and().httpBasic()
       	;
      
      	// Map any specific URL pattern Exception Handling here 
      	//
        http.exceptionHandling().defaultAuthenticationEntryPointFor(indexEntryPoint, new AntPathRequestMatcher("/index.html")).and();
        
    }
    
    
    
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserDetails authenticatedUserDetails() {
    	
        SecurityContextHolder.getContext().getAuthentication();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                return (UserDetails) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
            }
            if (authentication instanceof RememberMeAuthenticationToken) {
                return (UserDetails) ((RememberMeAuthenticationToken) authentication).getPrincipal();
            }
        }
       return null;
    }
}
