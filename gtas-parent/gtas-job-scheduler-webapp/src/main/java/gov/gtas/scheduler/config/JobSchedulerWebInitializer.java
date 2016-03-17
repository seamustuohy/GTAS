package gov.gtas.scheduler.config;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.config.RuleRunnerConfig;
import gov.gtas.job.config.ParserConfig;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class JobSchedulerWebInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { CommonServicesConfig.class,
				JobSchedulerConfig.class, RuleRunnerConfig.class,
				ParserConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return null;
	}

}
