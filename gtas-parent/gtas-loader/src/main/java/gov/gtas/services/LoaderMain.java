package gov.gtas.services;

import java.io.File;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import gov.gtas.config.CommonServicesConfig;

public class LoaderMain {
    private static Loader loader;
    
	public static void main(String[] args) {
		if (args.length == 0) {
		    printUsage();
			System.exit(0);
		}
		
		// determine whether it's a list of files or 2 directories
		boolean isListOfFiles = false;
		File arg1 = new File(args[0]);
	    if (arg1.isFile()) {
	        isListOfFiles = true;
	    } else if (arg1.isDirectory() && args.length == 2) {
            File arg2 = new File(args[1]);
            if (!arg2.isDirectory()) {
                System.err.println(arg2 + " is not a directory");
                System.exit(-1);
            }
		} else {
            System.err.println("Invalid argument(s)");
            printUsage();            
            System.exit(-1);		    
		}
		
		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
		    loader = ctx.getBean(Loader.class);
		    processFiles(args, isListOfFiles);
			com.hazelcast.core.Hazelcast.shutdownAll();
		}
	}

	private static void processFiles(String[] args, boolean isListOfFiles) {
		if (isListOfFiles) {
			// ignore any directories
			for (int i = 0; i < args.length; i++) {
				File f = new File(args[i]);
				if (f.isFile()) {
					processSingleFile(f);
				}
			}
		} else {
	        File incomingDir = new File(args[0]);
			File outgoingDir = new File(args[1]);
		    File[] listOfFiles = incomingDir.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File f = listOfFiles[i];
                if (f.isFile()) {
                    processSingleFile(f);
                    f.renameTo(new File(outgoingDir + File.separator + f.getName()));
                }
            }
		}
	}

	private static void processSingleFile(File f) {
        System.out.println(String.format("Processing %s", f.getAbsolutePath()));
        loader.processMessage(f);
    }

	private static void printUsage() {
        System.out.println("Usage: MessageLoader [files]");
        System.out.println("Usage: MessageLoader [incoming dir] [outgoing dir]");
	}
}
