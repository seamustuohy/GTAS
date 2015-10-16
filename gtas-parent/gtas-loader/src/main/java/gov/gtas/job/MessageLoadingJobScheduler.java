package gov.gtas.job;

import gov.gtas.config.CommonServicesConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageLoadingJobScheduler {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class);
		@SuppressWarnings("unused")
		FileReader mover = (FileReader)ctx.getBean("fileReader"); 
	}
}
