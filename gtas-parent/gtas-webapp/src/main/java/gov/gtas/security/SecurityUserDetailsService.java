package gov.gtas.security;


import gov.gtas.model.Authorities;
import gov.gtas.model.User;
import gov.gtas.services.UserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * UserDetails service that reads the user credentials from the database, using a JPA repository.
 *
 */
@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findById(username);

        if (user == null) {
            String message = "Username not found: " + username;
            logger.info(message);
            throw new UsernameNotFoundException(message);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        Iterator<Authorities> tempIter = user.getAuthorities().iterator();
        
        while(tempIter.hasNext()){
        authorities.add(new SimpleGrantedAuthority(((Authorities)tempIter.next()).getUserRole().getRoleDescription()));
        }
        

        logger.info("Found user in database: " + user);

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
