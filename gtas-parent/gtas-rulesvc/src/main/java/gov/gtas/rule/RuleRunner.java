package gov.gtas.rule;

import java.util.Set;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.config.RuleRunnerConfig;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.svc.TargetingService;

/**
 * A Java application for running the Rule Engine in stand alone mode.
 *
 */
public class RuleRunner {
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				CommonServicesConfig.class, RuleServiceConfig.class,
				RuleRunnerConfig.class);
		TargetingService targetingService = (TargetingService) ctx.getBean("targetingServiceImpl");
		Set<Long> uniqueFlights = targetingService.runningRuleEngine();
        System.out.println("updating hit counts for flight ids " + uniqueFlights);
        targetingService.updateFlightHitCounts(uniqueFlights);

		ctx.close();
		System.exit(0);
	}
}
