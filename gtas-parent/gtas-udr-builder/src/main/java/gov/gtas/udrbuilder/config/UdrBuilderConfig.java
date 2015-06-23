package gov.gtas.udrbuilder.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "gov.gtas.udrbuilder.service" })
@EnableJpaRepositories("gov.gtas.udrbuilder.repository")
public class UdrBuilderConfig {

}
