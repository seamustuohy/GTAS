package gov.gtas.controller.config;

import gov.gtas.repository.udr.UdrRuleRepository;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRestServiceConfig {

	 @Bean
	 public UserService userServiceMock() {
	 return Mockito.mock(UserService.class);
	 }

	@Bean
	public RulePersistenceService rulePersistenceServiceMock() {
		return Mockito.mock(RulePersistenceService.class);
	}

	@Bean
	public UdrRuleRepository udrRuleRepositoryMock() {
		return Mockito.mock(UdrRuleRepository.class);
	}

}
