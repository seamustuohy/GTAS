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
            System.out.println("Usage: ApisMessageLoader [apis files]");
            System.out.println("Usage: ApisMessageLoader [incoming dir] [outgoing dir]");
            System.exit(0);
        }

        try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
            ApisMessageService svc = ctx.getBean(ApisMessageService.class);
            File tmp = new File(args[0]);
            if (tmp.isFile()) {
                // assume list of files given; ignore any directories
                for (int i = 0; i < args.length; i++) {
                    File f = new File(args[i]);
                    if (f.isFile()) {
                        processSingleFile(svc, args[i]);
                    }
                }
                
            } else if (tmp.isDirectory()) {
                if (args.length != 2) {
                    System.out.println("error: expected outgoing dir");
                    System.exit(0);                    
                }
                File outgoingDir = new File(args[1]);
                if (!outgoingDir.isDirectory()) {
                    System.out.println(outgoingDir + " is not a directory");
                    System.exit(0);                                        
                }
                
                File[] listOfFiles = tmp.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    File f = listOfFiles[i];
                    if (f.isFile()) {
                        processSingleFile(svc, f.getAbsolutePath());
                        f.renameTo(new File(outgoingDir + File.separator + f.getName()));
                    }
                }
            
            } else {
                System.out.println("unrecognized file or directory: " + args[0]);
                System.exit(0);                                    
            }
        }
    }
}
