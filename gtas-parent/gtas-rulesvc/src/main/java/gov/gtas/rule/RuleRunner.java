package gov.gtas.rule;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.config.RuleRunnerConfig;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.svc.TargetingService;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * A Java application for running the Rule Engine in stand alone mode.
 *
 */
public class RuleRunner {
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				CommonServicesConfig.class, RuleServiceConfig.class,
				RuleRunnerConfig.class);
		TargetingService targetingService = (TargetingService) ctx
				.getBean("targetingServiceImpl");
		targetingService.runningRuleEngine();
		System.out.println("\n Running as a separate process\n");

		ctx.close();
		System.exit(0);
	}
}
