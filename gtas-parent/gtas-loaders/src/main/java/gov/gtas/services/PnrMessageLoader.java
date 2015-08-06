package gov.gtas.services;

import java.io.File;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.pnrgov.PnrMessageVo;

public class PnrMessageLoader {
    public static void processSingleFile(PnrMessageService svc, File f) {
        String filePath = f.getAbsolutePath();
        System.out.println("processing file " + filePath);
        PnrMessageVo m = svc.parsePnrMessage(filePath);
        if (m == null) {
            System.out.println("error parsing " + filePath);
            return;
        }

        System.out.println("MAC: " + m);
        svc.loadPnrMessage(m);
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: PnrMessageLoader [apis files]");
            System.exit(0);
        }

        try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
            PnrMessageService svc = ctx.getBean(PnrMessageService.class);
            File tmp = new File(args[0]);
            if (tmp.isFile()) {
                // assume list of files given; ignore any directories
                for (int i = 0; i < args.length; i++) {
                    File f = new File(args[i]);
                    if (f.isFile()) {
                        processSingleFile(svc, f);
                    }
                }
                
            } else {
                System.out.println("unrecognized file or directory: " + args[0]);
                System.exit(0);                                    
            }
        }
    }
}
