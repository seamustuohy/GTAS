package gov.gtas.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import gov.gtas.services.ApisMessageService;
import gov.gtas.services.ErrorPersistenceService;
import gov.gtas.services.PnrMessageService;
import gov.gtas.services.MessageLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import gov.gtas.error.ErrorDetailInfo;
import gov.gtas.error.ErrorHandlerFactory;

@Component
public class FileReader {

	private static final Logger logger = LoggerFactory.getLogger(FileReader.class);
	
	@Autowired
	ApisMessageService apisService;
	
	@Autowired
	PnrMessageService pnrService;
	
	@Autowired
	ErrorPersistenceService errorService;

	@Scheduled(initialDelay=6000,fixedRate=180000) 
    public void CheckForNewFile() {
    	logger.info("*************************************************************************************");
    	logger.info("************************* FILE READING JOB BEGIN AT ************************"+new Date());
    	logger.info("*************************************************************************************");	
    	Properties properties = getSchedulerProperties();
    	if(properties != null){
    		File apisFolder = new File(properties.getProperty("apis.dir.origin"));
    		File apisProcessedFolder = new File(properties.getProperty("apis.dir.processed"));
    		File pnrFolder = new File(properties.getProperty("pnr.dir.origin"));
    		File pnrProcessedFolder = new File(properties.getProperty("pnr.dir.processed"));
    		boolean finished=checkAndMoveApisFiles(apisFolder,apisProcessedFolder);
    		if(finished){
    			checkAndMovePnrFiles(pnrFolder,pnrProcessedFolder);
    		}
    	}
    	else{
    		logger.info("**NO PROPERTY FILE FOUND-SHUTTING DOWN THE PROCESS**");
    		logger.info("**PLEASE DEFINE PROPERTY FILE AND RESTART-EXITIN**");
    		System.exit(0);
    	}
    	logger.info("*************************************************************************************");
    	logger.info("************************* FILE READING JOB ENDED AT **********************"+new Date());
    	logger.info("*************************************************************************************");
	}
	
	private void checkAndMovePnrFiles(File folder,File processedFolder){
		List<ErrorDetailInfo> errors=new ArrayList<>();
    	try {
    		logger.info("#################################################################################");
    		logger.info("#####################  CHECKING AND PROCESSING PNR FILES FROM ###################"+folder.getAbsolutePath());
			if(folder.isDirectory()){
				for (final File fileEntry : folder.listFiles()) {
			        if (!fileEntry.isDirectory()) {
						Path moveFrom = FileSystems.getDefault().getPath(fileEntry.getPath());
						Path moveTo = FileSystems.getDefault().getPath(processedFolder.getPath() +File.separator+ fileEntry.getName());
						if(fileEntry.isFile()){
							logger.info("Reading file from : "+moveFrom);
							MessageLoader.processSingleFile(pnrService, fileEntry);
							logger.info("Moving file after processing to : "+moveTo);
							fileEntry.renameTo(moveTo.toFile());
						}
			        }
			    }
			}
			logger.info("#####################  FINISHED PROCESSING PNR FILES ############################");
		} catch (Exception e) {
			handleExceptions(e);
			logger.info("Exception saving PNR GOV message file"+e.getMessage());
		}		
	}
	
	private void handleExceptions(Exception e){
		ErrorDetailInfo errorDetails = ErrorHandlerFactory.createErrorDetails(e);
		try{
		    errorDetails = errorService.create(errorDetails); //add the saved ID
		} catch (Exception exception){
			//possibly DB is down
			exception.printStackTrace();
		}
	}
    private boolean checkAndMoveApisFiles(File folder,File processedFolder){
    	boolean finished=false;
    	try {
    		logger.info("***********************CHECKING AND PROCESSING APIS FILES FROM**********************"+folder.getAbsolutePath());
			if(folder.isDirectory()){
				for (final File fileEntry : folder.listFiles()) {
			        if (!fileEntry.isDirectory()) {
						Path moveFrom = FileSystems.getDefault().getPath(fileEntry.getPath());
						Path moveTo = FileSystems.getDefault().getPath(processedFolder.getPath() +File.separator+ fileEntry.getName());
						if(fileEntry.isFile()){
							logger.info("Reading file from : "+moveFrom);
							MessageLoader.processSingleFile(apisService, fileEntry);
							logger.info("Moving file after processing to : "+moveTo);
							fileEntry.renameTo(moveTo.toFile());
						}
			        }
			    }
			}
			finished=true;
		} catch (Exception e) {
			handleExceptions(e);
			logger.info("Exception saving APIS message file"+e.getMessage());
		}
    	return finished;
    }
    
	private Properties getSchedulerProperties(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
	        File file = new File(classLoader.getResource("scheduler.properties").getFile());
	    	input = new FileInputStream(file);
			prop.load(input);
		} catch (IOException e1) {
			logger.info("Exception loading scheduler.properties"+e1.getMessage());
		}
		return prop;
	}
	
	public ApisMessageService getApisService() {
		return apisService;
	}

	public void setApisService(ApisMessageService apisService) {
		this.apisService = apisService;
	}

	public PnrMessageService getPnrService() {
		return pnrService;
	}

	public void setPnrService(PnrMessageService pnrService) {
		this.pnrService = pnrService;
	}
}