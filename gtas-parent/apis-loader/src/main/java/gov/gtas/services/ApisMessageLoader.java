package gov.gtas.services;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

import java.io.File;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApisMessageLoader {
    public static void processSingleFile(ApisMessageService svc, File f) {
        String filePath = f.getAbsolutePath();
        if (exceedsMaxSize(f)) {
            System.err.println(filePath + " exceeds max file size");
            return;
        }
        
        System.out.println("processing file " + filePath);
        ApisMessageVo m = svc.parseApisMessage(filePath);
        if (m == null) {
            System.out.println("error parsing " + filePath);
            return;
        }

        svc.loadApisMessage(m);
    }
    
    public static boolean exceedsMaxSize(File f) {
        final int MAX_SIZE = 10;  // kilobytes
        double size = (f.length()) / 1024;  
        return size > MAX_SIZE;
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
                        processSingleFile(svc, f);
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
                        processSingleFile(svc, f);
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
