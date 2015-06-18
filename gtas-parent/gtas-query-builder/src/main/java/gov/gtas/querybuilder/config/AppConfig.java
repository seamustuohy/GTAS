package gov.gtas.querybuilder.config;

import gov.gtas.querybuilder.repository.FlightDisplayRepository;
import gov.gtas.querybuilder.service.QueryBuilderService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "gov.gtas")
public class AppConfig {

	@Bean
    public QueryBuilderService queryService() {
        return new QueryBuilderService();
    }
	
}
