package gov.gtas.services;

import java.text.ParseException;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApisMessageLoader {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ApisMessageLoader [apis files]");
            System.exit(0);
        }
        
        try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
            ApisMessageService svc = ctx.getBean(ApisMessageService.class);
            for (int i=0; i<args.length; i++) {
                String filePath = args[i];
                System.out.println("processing file " + filePath);
                ApisMessageVo m = null;
                try {
                    m = svc.parseApisMessage(filePath);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                if (m == null) {
                    System.out.println("error parsing " + filePath);
                    continue;
                }
                
                try {
                    svc.loadApisMessage(m);
                } catch (Exception e) {
                    System.out.println("error loading " + filePath);
                }
            }
        }
    }
}
