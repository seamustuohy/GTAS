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
		
		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(CommonServicesConfig.class)) {
		    int exitStatus = processFiles(ctx, args);
			com.hazelcast.core.Hazelcast.shutdownAll();
			System.exit(exitStatus);
		}
	}

	private static int processFiles(ConfigurableApplicationContext ctx, String[] args) {
		int exitStatus = 0;
		File arg1 = new File(args[0]);
		if (arg1.isFile()) {
			// assume list of files given; ignore any directories
			for (int i = 0; i < args.length; i++) {
				File f = new File(args[i]);
				if (f.isFile()) {
					processSingleFile(ctx, f);
				}
			}
			
		} else if (arg1.isDirectory() && args.length == 2) {
			File outgoingDir = new File(args[1]);
			if (!outgoingDir.isDirectory()) {
				amitError(outgoingDir + " is not a directory");
	            exitStatus = -1;
			} else {
			    File[] listOfFiles = arg1.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    File f = listOfFiles[i];
                    if (f.isFile()) {
                        processSingleFile(ctx, f);
                        f.renameTo(new File(outgoingDir + File.separator + f.getName()));
                    }
                }
			}
			
		} else {
			printUsage();
			exitStatus = -1;
		}
		
		return exitStatus;
	}

	private static void processSingleFile(ConfigurableApplicationContext ctx, File f) {
        String filePath = f.getAbsolutePath();
        if (exceedsMaxSize(f)) {
            System.err.println(filePath + " exceeds max file size");
            return;
        }

        byte[] raw = null;
        try {
            raw = FileUtils.readSmallFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tmp = new String(raw, StandardCharsets.US_ASCII);        

        MessageService svc = null;
        String type = null;
        if (tmp.contains("PAXLST")) {
            type = "APIS";
            svc = ctx.getBean(ApisMessageService.class);
        } else if (tmp.contains("PNRGOV")) {
            type = "PNR";
            svc = ctx.getBean(PnrMessageService.class);
        } else {
            amitError("Unknown file type " + filePath);
        }
        
        System.out.println(String.format("Processing %s file %s", type, filePath));
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
