package gov.gtas.svc.perf.test;

import org.springframework.context.ConfigurableApplicationContext;

public interface PerformanceTestFactory {
	PerformanceTest createTest(ConfigurableApplicationContext ctx);
}
