package gov.gtas.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

/**
 * The configuration class can be imported into an XML configuration by:<br>
 * <context:annotation-config/> <bean
 * class="gov.gtas.config.CommonServicesConfig"/>
 * 
 * @author GTAS4
 *
 */

@Configuration
@ComponentScan("gov.gtas")
@PropertySource({ "classpath:commonservices.properties",
		"classpath:hibernate.properties" })
@EnableJpaRepositories("gov.gtas")
@EnableTransactionManagement
@EnableCaching
public class CommonServicesConfig {

	private static final String PROPERTY_NAME_DATABASE_DRIVER = "hibernate.connection.driver_class";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD = "hibernate.connection.password";
	private static final String PROPERTY_NAME_DATABASE_URL = "hibernate.connection.url";
	private static final String PROPERTY_NAME_DATABASE_USERNAME = "hibernate.connection.username";
	private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_SECOND_LEVEL_CACHE = "hibernate.cache.use_second_level_cache";
	private static final String PROPERTY_NAME_HIBERNATE_CACHE_FACTORY = "hibernate.cache.region.factory_class";
	private static final String PROPERTY_NAME_CONFIGURATION_RESOURCE_PATH = "hibernate.cache.provider_configuration_file_resource_path";
	private static final String PROPERTY_NAME_HIBERNATE_QUERY_CACHE = "hibernate.cache.use_query_cache";
	private static final String PROPERTY_NAME_HIBERNATE_USE_MINIMAL_PUTS = "hibernate.cache.use_minimal_puts";
	private static final String PROPERTY_NAME_SHAREDCACHE_MODE = "javax.persistence.sharedCache.mode";

	private Properties hibProperties() {
		Properties properties = new Properties();
		properties.put(PROPERTY_NAME_HIBERNATE_DIALECT,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
		properties.put(PROPERTY_NAME_HIBERNATE_QUERY_CACHE,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_QUERY_CACHE));
		properties.put(PROPERTY_NAME_SECOND_LEVEL_CACHE,
				env.getRequiredProperty(PROPERTY_NAME_SECOND_LEVEL_CACHE));
		properties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
		properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
		properties.put(PROPERTY_NAME_HIBERNATE_CACHE_FACTORY,
				env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_CACHE_FACTORY));
		properties
				.put(PROPERTY_NAME_CONFIGURATION_RESOURCE_PATH,
						env.getRequiredProperty(PROPERTY_NAME_CONFIGURATION_RESOURCE_PATH));
		properties.put(PROPERTY_NAME_SHAREDCACHE_MODE,
				env.getRequiredProperty(PROPERTY_NAME_SHAREDCACHE_MODE));
		properties.put(PROPERTY_NAME_HIBERNATE_USE_MINIMAL_PUTS, env
				.getRequiredProperty(PROPERTY_NAME_HIBERNATE_USE_MINIMAL_PUTS));

		return properties;
	}

	@Resource
	private Environment env;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env
				.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
		dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
		dataSource.setUsername(env
				.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
		dataSource.setPassword(env
				.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean
				.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean
				.setPackagesToScan(env
						.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));

		entityManagerFactoryBean.setJpaProperties(hibProperties());

		return entityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory()
				.getObject());
		return transactionManager;
	}

	@Bean(name = "cacheManager")
	HazelcastCacheManager hazelcastcacheManager() throws Exception {
		return new HazelcastCacheManager(hazelcastInstance());
	}

	@Bean
	HazelcastInstance hazelcastInstance() throws Exception {
		return Hazelcast.newHazelcastInstance();
	}
}
