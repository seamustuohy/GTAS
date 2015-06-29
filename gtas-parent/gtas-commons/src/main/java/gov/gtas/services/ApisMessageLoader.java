package gov.gtas.services;

import gov.gtas.config.CommonServicesConfig;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApisMessageLoader {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ApisMessageLoader [apis files]");
            System.exit(0);
        }
        try (ConfigurableApplicationContext ctx = 
                new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
            ApisMessageService svc = ctx.getBean(ApisMessageService.class);
            svc.parseAndLoadApisFile(args[0]);
        }
    }
}
