package gov.gtas.rule;

import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.BasicErrorHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of utility methods for:
 * <ul>
 * <li> Knowledge management</li>
 * <li> Rule management</li>
 * </ul>
 * @author GTAS3 (AB)
 *
 */
public class RuleUtils {
	private static final Logger logger = LoggerFactory.getLogger(RuleUtils.class);
	 
	/**
	 * Creates a KieSession from a DRL file.<br>
	 * (see for example http://stackoverflow.com/questions/27488034/with-drools-6-x-how-do-i-avoid-maven-and-the-compiler)
	 * @param filePath the input DRL file on the class path.
	 * @param errorHandler error handler.
	 * @return the  created KieBase.
	 * @throws IOException on IO error.
	 */
	public static KieBase createKieBaseFromClasspathFile(final String filePath, final BasicErrorHandler errorHandler) throws IOException{
		File file = new File(filePath);
	    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream( filePath );
	    String kfilepath = RuleServiceConstants.KIE_FILE_SYSTEM_ROOT+file.getName();
	    return createKieBase(kfilepath, is, errorHandler);
	}
	/**
	 * Creates a KieBase from DRL string data.
	 * @param drlString the DRL data as a string.
	 * @param errorHandler error handler.
	 * @return the  created KieBase.
	 * @throws IOException on IO error.
	 */
	public static KieBase createKieBaseFromDrlString(final String drlString, final BasicErrorHandler errorHandler) throws IOException{
	    File file = File.createTempFile("rule","");
	    ByteArrayInputStream bis = new ByteArrayInputStream(drlString.getBytes());
	    String kfilepath = RuleServiceConstants.KIE_FILE_SYSTEM_ROOT+file.getName()+".drl";
	    return createKieBase(kfilepath, bis, errorHandler);
	}
	/**
	 * Thread-safe creation of KieSession from a KieBase.
	 * @param kieBase the source KieBase
	 * @return the created KieSession
	 */
	public static KieSession createSession(KieBase kieBase){
		KieSession ksession = null;
		synchronized(kieBase){
			ksession = kieBase.newKieSession();
		}
		return ksession;
	}
	/**
	 * Converts a KieBase to compressed binary data suitable for caching or saving in a database as a BLOB.
	 * @param kieBase the KieBase to convert.
	 * @return compressed binary data for KieBase.
	 * @throws IOException on IO error.
	 */
	public static byte[] convertKieBaseToBytes(final KieBase kieBase) throws IOException{
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final GZIPOutputStream gzipOutStream = new GZIPOutputStream(bos);
	    final ObjectOutputStream out = new ObjectOutputStream(gzipOutStream);
	    out.writeObject( kieBase );
	    out.close();
	    return bos.toByteArray();		
	}
	
	/**
	 * Creates a KieBase from compressed binary data.
	 * @param kiebaseBytes the binary compressed data to be used for input.
	 * @return the KieBase object constructed from the input data.
	 * @throws ClassNotFoundException if the compressed binary data includes unknown serialized Java Class instances.
	 * @throws IOException on IO error.
	 */
	public static KieBase convertKieBasefromBytes(final byte[] kiebaseBytes) throws ClassNotFoundException, IOException{
		final ByteArrayInputStream bis = new ByteArrayInputStream(kiebaseBytes);
		final GZIPInputStream gzipInStream = new GZIPInputStream(bis);
		final ObjectInputStream in = new ObjectInputStream(gzipInStream);
		KieBase kieBase = (KieBase)in.readObject();
		in.close();		
		return kieBase;
	}
	/**
	 * Creates a KieBase from input stream data. 
	 * @param kfilepath the in memory KieFileSystem name
	 * @param is  the input stream for DRL data
	 * @param errorHandler error handler
	 * @return the created KieBase
	 */
    private static KieBase createKieBase(final String kfilepath, final InputStream is, final BasicErrorHandler errorHandler){
	    KieServices ks = KieServices.Factory.get();
	    KieFileSystem kfs = ks.newKieFileSystem();
	    kfs.write( kfilepath, ks.getResources().newInputStreamResource( is ) );
	    KieBuilder kieBuilder = ks.newKieBuilder( kfs ).buildAll();
	    Results results = kieBuilder.getResults();
	    if( results.hasMessages( Message.Level.ERROR ) ){
	    	List<Message> errors = results.getMessages();
	        for(Message msg:errors){
	        	logger.error(msg.getText());
	        }
	        throw errorHandler.createException(RuleServiceConstants.RULE_COMPILE_ERROR_CODE, kfilepath, errors);
	    }
	    KieContainer kieContainer =
	        ks.newKieContainer( ks.getRepository().getDefaultReleaseId() );

	    // CEP - get the KIE related configuration container and set the EventProcessing (from default cloud) to Stream
	    KieBaseConfiguration config = ks.newKieBaseConfiguration();
	    config.setOption( EventProcessingOption.STREAM );
	    KieBase kieBase = kieContainer.newKieBase( config );
	    
//	    KieBase kieBase = kieContainer.getKieBase();//alternative way to gewt the default KieBase
	    
	    return kieBase;
    	
    }
}
