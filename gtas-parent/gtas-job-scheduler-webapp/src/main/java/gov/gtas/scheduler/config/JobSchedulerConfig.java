package gov.gtas.scheduler.config;

import gov.gtas.job.LoaderScheduler;
import gov.gtas.rule.RuleRunnerScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class JobSchedulerConfig implements SchedulingConfigurer {
	@Bean
	public RuleRunnerScheduler ruleRunnerSchedulerbean() {
		return new RuleRunnerScheduler();
	}

	@Bean
	public LoaderScheduler loaderReaderbean() {
		return new LoaderScheduler();
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		return Executors.newScheduledThreadPool(20);
	}

}
