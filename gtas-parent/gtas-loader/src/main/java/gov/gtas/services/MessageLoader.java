package gov.gtas.services;

import java.io.File;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.vo.MessageVo;

public class MessageLoader {
	public static void processSingleFile(MessageService svc, File f) {
		String filePath = f.getAbsolutePath();
		if (exceedsMaxSize(f)) {
			System.err.println(filePath + " exceeds max file size");
			return;
		}

		System.out.println("processing file " + filePath);

		List<String> messages = null;
		try {
			messages = svc.preprocess(filePath);
		} catch (Exception e) {
			System.out.println("error preprocessing " + filePath);
			return;
		}

		for (String msg : messages) {
			MessageVo m = svc.parse(msg);
			if (m == null) {
				System.out.println("error parsing " + filePath);
				return;
			}

			svc.load(m);
		}
	}

	/**
	 * For APIS messages, Type â€œBâ€� messages are no longer limited to a
	 * length of 3840 bytes. SITA and ARInc now support Type â€œBâ€� message
	 * lengths up to 64,000 bytes.
	 * 
	 * TODO: what about pnr?
	 */
	public static boolean exceedsMaxSize(File f) {
		final int MAX_SIZE = 64000;
		double numBytes = f.length();
		return numBytes > MAX_SIZE;
	}

	public static void main(String[] args) {
		if (args.length <= 1) {
			System.out.println("Usage: MessageLoader [message type] [files]");
			System.out
					.println("Usage: MessageLoader [message type] [incoming dir] [outgoing dir]");
			System.exit(0);
		}
		int exitStatus = 0;
		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				CommonServicesConfig.class)) {
			MessageService svc = null;
			String messageType = args[0];
			switch (messageType.toLowerCase()) {
			case "apis":
				svc = ctx.getBean(ApisMessageService.class);
				exitStatus = processFiles(svc, args);
				break;
			case "pnr":
				svc = ctx.getBean(PnrMessageService.class);
				exitStatus = processFiles(svc, args);
				break;
			default:
				System.out.println("************************************************************");
				System.err.println("GTAS LOADER ERROR: Unknown message type:"+messageType);
				System.out.println("************************************************************");
				exitStatus = -1;
			}
            /*
             * shutdown all the Hazelcast non-daemon threads.
             */
			com.hazelcast.core.Hazelcast.shutdownAll();
			
			System.exit(exitStatus);
		}

	}

	private static int processFiles(MessageService svc, String[] args) {
		int exitStatus = 0;
		File tmp = new File(args[1]);
		if (tmp.isFile()) {
			// assume list of files given; ignore any directories
			for (int i = 0; i < args.length; i++) {
				File f = new File(args[i]);
				if (f.isFile()) {
					processSingleFile(svc, f);
				}
			}

		} else if (tmp.isDirectory() && args.length == 3) {
			File outgoingDir = new File(args[2]);
			if (!outgoingDir.isDirectory()) {
				System.out.println("************************************************************");
				System.out.println("GTAS LOADER ERROR: "+outgoingDir + " is not a directory");
				System.out.println("************************************************************");
			} else {

				File[] listOfFiles = tmp.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					File f = listOfFiles[i];
					if (f.isFile()) {
						processSingleFile(svc, f);
						f.renameTo(new File(outgoingDir + File.separator
								+ f.getName()));
					}
				}
			}
		} else if (tmp.isDirectory() && args.length != 3) {
			System.out.println("************************************************************");
			System.out.println("GTAS LOADER ERROR: expected outgoing dir");
			System.out.println("************************************************************");
		} else {
			System.out.println("************************************************************");
			System.out.println("GTAS LOADER ERROR: unrecognized file or directory: " + args[1]);
			System.out.println("************************************************************");
			exitStatus = -1;
		}
		return exitStatus;
	}

}
