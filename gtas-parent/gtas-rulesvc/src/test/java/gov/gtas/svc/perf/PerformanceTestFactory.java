package gov.gtas.svc.perf;

import org.springframework.context.ConfigurableApplicationContext;

public interface PerformanceTestFactory {
	PerformanceTest createTest(ConfigurableApplicationContext ctx);
}
