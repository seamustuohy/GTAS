package gov.cbp.taspd.gtas.querybuilder.config;

import gov.cbp.taspd.gtas.querybuilder.repository.FlightDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.service.QueryBuilderService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "gov.cbp.taspd.gtas")
public class AppConfig {

	@Bean
    public QueryBuilderService queryService() {
        return new QueryBuilderService();
    }
	
}
