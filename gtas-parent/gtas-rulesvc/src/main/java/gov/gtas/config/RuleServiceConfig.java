package gov.gtas.config;

import gov.gtas.rule.RuleService;
import gov.gtas.rule.RuleServiceImpl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
/**
 * The spring configuration class for the Rule Engine.<br>
 * It can be imported into an XML configuration by:<br>
 * &lt;context:annotation-config/&gt;<br>
 * &lt;bean class="gov.gtas.config.RuleServiceConfig"/&gt;
 * 
 * @author GTAS3 (AB)
 *
 */
//@ImportResource("another-application-context.xml")
//@Import(OtherConfiguration.class)
@Configuration
@ComponentScan("gov.gtas")
@PropertySource("classpath:rulesvc.properties")
public class RuleServiceConfig {
    @Resource
    private Environment env;

    @Autowired
    @Value("${some.interesting.property}")
    private String someInterestingProperty;
    
    @Bean(name = "ruleEngine")
	  public RuleService getRuleService() {
	    return new RuleServiceImpl();
	  }


}
