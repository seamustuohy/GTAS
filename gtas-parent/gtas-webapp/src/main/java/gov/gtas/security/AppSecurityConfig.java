package gov.gtas.security;


import gov.gtas.security.filters.UserAuthenticationFilter;
import gov.gtas.security.service.AuthUserDetailsService;

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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

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
       
    @Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("MAC: configuring spring security");
        
        http
        .authorizeRequests()
        .antMatchers("/resources/**/*").permitAll()
        .antMatchers(HttpMethod.POST, "/user").permitAll()
        .antMatchers("/login.jsp").permitAll()
        .antMatchers("/home.action").hasAnyAuthority("MANAGE_RULES","MANAGE_QUERIES","VIEW_FLIGHT_PASSENGERS","MANAGE_WATCHLIST","ADMIN")
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .formLogin()
        .defaultSuccessUrl("/home.action")
        .loginProcessingUrl("/j_spring_security_check")
        .usernameParameter("j_username")
        .passwordParameter("j_password")
        .failureHandler(new SimpleUrlAuthenticationFailureHandler()).failureUrl("/login.jsp")
        .successHandler(new AjaxAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler()))
        .defaultSuccessUrl("/home.action")
        .loginPage("/login.jsp")
        .and().logout().logoutUrl("/logout.action").logoutSuccessUrl("/login.jsp").permitAll().and();

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
