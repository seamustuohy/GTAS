package gov.gtas.config;

import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

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

	@Autowired
	@Value("${some.interesting.property}")
	private String someInterestingProperty;

	@Bean(name = "cacheManager")
	HazelcastCacheManager hazelcastcacheManager() throws Exception {
		return new HazelcastCacheManager(hazelcastInstance());
	}

	@Bean
	HazelcastInstance hazelcastInstance() throws Exception {
		return Hazelcast.newHazelcastInstance();
	}

	@Bean(name = "applicationEventMulticaster")
	public ApplicationEventMulticaster applicationEventMulticaster() {
		SimpleApplicationEventMulticaster applicationEventMulticaster = new SimpleApplicationEventMulticaster();
		applicationEventMulticaster.setTaskExecutor(Executors
				.newFixedThreadPool(10));
		return applicationEventMulticaster;
	}
}
