package gov.gtas.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    /**
     * Simply selects the home view to render by returning its name.
     */
    //@RequestMapping(value = "/home.action", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        logger.info("**********Welcome home! The client locale is {}**********", locale);
        
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        model.addAttribute("serverTime", formattedDate );
        
        return "common/main.html";
    }
    
    @PreDestroy
    public void destroy() {
        logger.info("**************************************************************************");
        logger.info("GTAS HOME CONTROLLER SHUTTING DOWN!!!!!!!");
        logger.info("**************************************************************************");
        
        try {
            com.hazelcast.core.Hazelcast.shutdownAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }   
}
