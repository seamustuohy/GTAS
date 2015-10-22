package gov.gtas.config;

import javax.annotation.Resource;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The spring configuration class for the Rule Engine.<br>
 * It can be imported into an XML configuration by:<br>
 * &lt;context:annotation-config/&gt;<br>
 * &lt;bean class="gov.gtas.config.RuleServiceConfig"/&gt;
 * 
 * @author GTAS3 (AB)
 *
 */
// @ImportResource("another-application-context.xml")
// @Import(OtherConfiguration.class)
@Configuration
@ComponentScan("gov.gtas")
@PropertySource("classpath:rulesvc.properties")
@EnableTransactionManagement
@EnableCaching
public class RuleServiceConfig {
	@Resource
	private Environment env;

//	@Bean(name = "cacheManager")
//	HazelcastCacheManager hazelcastcacheManager() throws Exception {
//		return new HazelcastCacheManager(hazelcastInstance());
//	}
//
}
