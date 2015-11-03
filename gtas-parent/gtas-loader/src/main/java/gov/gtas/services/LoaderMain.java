package gov.gtas.services;

import static gov.gtas.constant.GtasSecurityConstants.GTAS_APPLICATION_USERID;

import java.io.File;
import java.util.Date;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.enumtype.AuditActionType;

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

		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				CommonServicesConfig.class)) {
			loader = ctx.getBean(Loader.class);
			processFiles(args, isListOfFiles, ctx);
			com.hazelcast.core.Hazelcast.shutdownAll();
		}
	}

	private static void processFiles(String[] args, boolean isListOfFiles,
			ConfigurableApplicationContext ctx) {
		/*
		 * Loader statistics variable.
		 */
        LoaderStatistics stats = new LoaderStatistics();
        
		if (isListOfFiles) {
			// ignore any directories
			for (int i = 0; i < args.length; i++) {
				File f = new File(args[i]);
				if (f.isFile()) {
					processSingleFile(f, stats);
				}
			}
		} else {
			File incomingDir = new File(args[0]);
			File outgoingDir = new File(args[1]);
			File[] listOfFiles = incomingDir.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				File f = listOfFiles[i];
				if (f.isFile()) {
					processSingleFile(f, stats);
					f.renameTo(new File(outgoingDir + File.separator
							+ f.getName()));
				}
			}
		}
		writeAuditLog(ctx, stats);
	}

	/**
	 * Writes the audit log with run statistics.
	 * @param ctx the spring application context to obtain the audit log service.
	 * @param stats the statistics bean.
	 */
	private static void writeAuditLog(ConfigurableApplicationContext ctx, LoaderStatistics stats) {
		try{
			AuditLogPersistenceService auditLogSvc = ctx.getBean(AuditLogPersistenceService.class);
			StringBuilder bldr = new StringBuilder("{");
			bldr.append("totalFilesProcessed:").append(stats.getNumFilesProcessed())
			    .append(", totalFilesAborted:").append(stats.getNumFilesAborted())
			    .append(", totalMessagesProcessed:").append(stats.getNumMessagesProcessed())
			    .append(", totalMessagesInError:").append(stats.getNumMessagesFailed())
			    .append("}");
			
			String message = "Message Loader run on " + new Date();
			auditLogSvc.create(AuditActionType.LOADER_RUN,
					"GTAS Message Loader Run", bldr.toString(),
					message, GTAS_APPLICATION_USERID);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private static void processSingleFile(File f, LoaderStatistics stats) {
		System.out.println(String.format("Processing %s", f.getAbsolutePath()));
		int[] result = loader.processMessage(f);
		// update loader statistics.
		if (result != null) {
			stats.incrementNumFilesProcessed();
			stats.incrementNumMessagesProcessed(result[0]);
			stats.incrementNumMessagesFailed(result[1]);
		} else {
			stats.incrementNumFilesAborted();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: MessageLoader [files]");
		System.out
				.println("Usage: MessageLoader [incoming dir] [outgoing dir]");
	}
	
}
