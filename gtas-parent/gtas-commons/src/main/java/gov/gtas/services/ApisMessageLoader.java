package gov.gtas.services;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

import java.io.File;
import java.text.ParseException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApisMessageLoader {
    public static void processSingleFile(ApisMessageService svc, String filePath) {
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
            return;
        }

        try {
            svc.loadApisMessage(m);
        } catch (Exception e) {
            System.out.println("error loading " + filePath);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ApisMessageLoader [apis files or folder name]");
            System.exit(0);
        }

        try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
            ApisMessageService svc = ctx.getBean(ApisMessageService.class);
            for (int i = 0; i < args.length; i++) {
                File tmp = new File(args[i]);
                if (tmp.isFile()) {
                    processSingleFile(svc, args[i]);
                } else if (tmp.isDirectory()) {
                    File[] listOfFiles = tmp.listFiles();
                    for (int j = 0; j < listOfFiles.length; j++) {
                        if (listOfFiles[j].isFile()) {
                            processSingleFile(svc, listOfFiles[j].getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
