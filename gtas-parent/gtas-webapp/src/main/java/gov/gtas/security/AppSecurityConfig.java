package gov.gtas.security;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

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
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("MAC: configuring spring security");
        
        http
        .authorizeRequests()
        .antMatchers("/resources/**").permitAll()
        .antMatchers(HttpMethod.POST, "/user").permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .formLogin()
        .defaultSuccessUrl("/index.html")
        .loginProcessingUrl("/authenticate")
        .usernameParameter("username")
        .passwordParameter("password")
        .failureHandler(new AjaxAuthenticationFailureHandler())
        .successHandler(new AjaxAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler()))
        .loginPage("/resources/login.html")
        .and()
        .httpBasic()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/resources/login.html")
        .permitAll();
    }
}
