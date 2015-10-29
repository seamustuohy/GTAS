package gov.gtas.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.util.FileUtils;

public class MessageLoader {
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
            File outgoingDir = new File(args[1]);
            if (outgoingDir.isDirectory()) {
                isListOfFiles = false;
            } else {
                System.err.println(outgoingDir + " is not a directory");
                System.exit(-1);
            }
		} else {
            System.err.println("Invalid argument(s)");
            printUsage();            
            System.exit(-1);		    
		}
		
		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
		    processFiles(ctx, args, isListOfFiles);
			com.hazelcast.core.Hazelcast.shutdownAll();
		}
	}

	private static void processFiles(ConfigurableApplicationContext ctx, String[] args, boolean isListOfFiles) {
		if (isListOfFiles) {
			// ignore any directories
			for (int i = 0; i < args.length; i++) {
				File f = new File(args[i]);
				if (f.isFile()) {
					processSingleFile(ctx, f);
				}
			}
		} else {
	        File incomingDir = new File(args[0]);
			File outgoingDir = new File(args[1]);
		    File[] listOfFiles = incomingDir.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File f = listOfFiles[i];
                if (f.isFile()) {
                    processSingleFile(ctx, f);
                    f.renameTo(new File(outgoingDir + File.separator + f.getName()));
                }
            }
		}
	}

	private static void processSingleFile(ConfigurableApplicationContext ctx, File f) {
        String filePath = f.getAbsolutePath();
        if (exceedsMaxSize(f)) {
            amitError(filePath + " exceeds max file size");
            return;
        }

        byte[] raw = null;
        try {
            raw = FileUtils.readSmallFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String messageText = new String(raw, StandardCharsets.US_ASCII);        

        MessageService svc = null;
        String messageType = null;
        if (messageText.contains("PAXLST")) {
            messageType = "APIS";
            svc = ctx.getBean(ApisMessageService.class);
        } else if (messageText.contains("PNRGOV")) {
            messageType = "PNR";
            svc = ctx.getBean(PnrMessageService.class);
        } else {
            amitError("Unknown file type " + filePath);
            System.exit(-1);
        }
        
        System.out.println(String.format("Processing %s file %s", messageType, filePath));
        svc.processMessage(filePath);
    }

    private static boolean exceedsMaxSize(File f) {
        final int MAX_SIZE = 64000;  // bytes
        double numBytes = f.length();
        return numBytes > MAX_SIZE;
    }

	private static void printUsage() {
        System.out.println("Usage: MessageLoader [files]");
        System.out.println("Usage: MessageLoader [incoming dir] [outgoing dir]");
	}
	
	private static void amitError(String err) {
        System.err.println("************************************************************");
        System.err.println(String.format("GTAS LOADER ERROR: %s", err));
        System.err.println("************************************************************");
	}
}
