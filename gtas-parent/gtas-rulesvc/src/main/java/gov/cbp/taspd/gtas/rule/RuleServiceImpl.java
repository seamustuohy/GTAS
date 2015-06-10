package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.ApiMesssage;
import gov.cbp.taspd.gtas.bo.RuleServiceRequest;
import gov.cbp.taspd.gtas.bo.RuleServiceRequestType;
import gov.cbp.taspd.gtas.error.RuleServiceException;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Flight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RuleServiceImpl implements RuleService{
	private static final String DEFAULT_RULESET_NAME = "gtas.drl";
	
    @Override
	public RuleServiceResult invokeRuleset(String ruleSetName, RuleServiceRequest req) {
		if(null == req){
			throw new RuleServiceException("Input Request cannot be null");
		}
		
		KieSession ksession = initSessionFromClasspath("GtasKS", createEventListeners());
		
		
        final ApiMesssage message = new ApiMesssage();
        message.setMessage( "Hello World" );
        message.setStatus( ApiMesssage.HELLO );
        ksession.insert( message );

        // and fire the rules
        ksession.fireAllRules();
        
        // Remove comment if using logging
        // logger.close();

        // and then dispose the session
        ksession.dispose();
		
		
		return null;
	}
	
    @Override
	public RuleServiceResult invokeRuleset(RuleServiceRequest req) {
    	return invokeRuleset(DEFAULT_RULESET_NAME, req);
	}
    
	@Override
	public RuleServiceRequest createRuleServiceRequest(
			final gov.cbp.taspd.gtas.model.Message requestMessage) {
		RuleServiceRequest ret = null;
		if(requestMessage instanceof ApisMessage){
			ret = createApisRequest((ApisMessage)requestMessage);
		}
		return ret;
	}
    private RuleServiceRequest createApisRequest(final ApisMessage req){
    	final List<Flight> requestList = new ArrayList<Flight>(req.getFlights());
    	return new RuleServiceRequest(){
    		public List<?> getRequestObjects(){
    			return requestList;
    		}
    		public RuleServiceRequestType getRequestType(){
    			return RuleServiceRequestType.APIS_MESSAGE;
    		}
    		
    	};
    }
	/**
	 * Creates a list of KieSession event listeners.
	 * @return list of event listeners.
	 */
	private List<EventListener> createEventListeners(){
		List<EventListener> eventListenerList = new LinkedList<EventListener>();
		
		eventListenerList.add(new DebugAgendaEventListener(){

			@Override
			public void afterMatchFired(AfterMatchFiredEvent event) {
				System.out.println("Fired rule name ="+ event.getMatch().getRule().getName());
				super.afterMatchFired(event);
			}
			
		});
		eventListenerList.add(new DebugRuleRuntimeEventListener(){

			@Override
			public void objectUpdated(ObjectUpdatedEvent event) {
				System.out.println("Object Updated by Rule:"+event.getRule().getName());
				super.objectUpdated(event);
			}
			
		});
		
		return eventListenerList;
	}
    /**
     * Creates a simple rule session from the provided session name.
     * Note: The session name must be configured in the KieModule config file META-INF/kmodule.xml
     * @param sessionName the session name
     * @param eventListenerList the list of event listeners to attach to the session
     * @return the created session
     */
	private KieSession initSessionFromClasspath(final String sessionName, final List<EventListener> eventListenerList){
        // KieServices is the factory for all KIE services 
        KieServices ks = KieServices.Factory.get();
        
        // From the kie services, a container is created from the classpath
        KieContainer kc = ks.getKieClasspathContainer();
        
        // From the container, a session is created based on  
        // its definition and configuration in the META-INF/kmodule.xml file 
        KieSession ksession = kc.newKieSession(sessionName);
        
        // Once the session is created, the application can interact with it
        // In this case it is setting a global as defined in the 
        // gov/cbp/taspd/gtas/rule/gtas.drl file
        ksession.setGlobal( "resultList",
                            new ArrayList<Object>() );

        // The application can also setup listeners
        if(eventListenerList != null){
        	for(EventListener el: eventListenerList){
        		if(el instanceof AgendaEventListener){
        		   ksession.addEventListener((AgendaEventListener)el);
        		}else if(el instanceof RuleRuntimeEventListener){
        			ksession.addEventListener((RuleRuntimeEventListener)el);
        		}
        	}
        }

        // To setup a file based audit logger, uncomment the next line 
        // KieRuntimeLogger logger = ks.getLoggers().newFileLogger( ksession, "./helloworld" );
        
        // To setup a ThreadedFileLogger, so that the audit view reflects events whilst debugging,
        // uncomment the next line
        // KieRuntimeLogger logger = ks.getLoggers().newThreadedFileLogger( ksession, "./helloworld", 1000 );

        
        // Remove comment if using logging
        // logger.close();

		
        return ksession;
	}
	
	/**
	 * Creates a KieSession from a DRL file.
	 * @see http://stackoverflow.com/questions/27488034/with-drools-6-x-how-do-i-avoid-maven-and-the-compiler
	 * @param filePath
	 * @param outFilePath
	 * @return
	 * @throws FileNotFoundException
	 */
	private KieSession initSessionFromFile(final String filePath, final String outFilePath) throws FileNotFoundException, IOException{
	    KieServices ks = KieServices.Factory.get();
	    KieFileSystem kfs = ks.newKieFileSystem();
	    FileInputStream fis = new FileInputStream( filePath );
	    kfs.write( "src/main/resources/sale.drl",
	                   ks.getResources().newInputStreamResource( fis ) );
	    KieBuilder kieBuilder = ks.newKieBuilder( kfs ).buildAll();
	    Results results = kieBuilder.getResults();
	    if( results.hasMessages( Message.Level.ERROR ) ){
	        System.out.println( results.getMessages() );
	        throw new IllegalStateException( "### errors ###" );
	    }
	    KieContainer kieContainer =
	        ks.newKieContainer( ks.getRepository().getDefaultReleaseId() );

	    // CEP - get the KIE related configuration container and set the EventProcessing (from default cloud) to Stream
	    KieBaseConfiguration config = ks.newKieBaseConfiguration();
	    config.setOption( EventProcessingOption.STREAM );
	    KieBase kieBase = kieContainer.newKieBase( config );
	    //      KieSession kieSession = kieContainer.newKieSession();
	    KieSession kieSession = kieBase.newKieSession();
	    
	    //save the KieBase so that sessions can be created fast in the future.
	    ObjectOutputStream out =
	            new ObjectOutputStream( new FileOutputStream( outFilePath ) );
	    out.writeObject( kieBase );
	    out.close();
	    	    
	    return kieSession;
	}
	/**
	 * Creates a session from cached KieBase.
	 * @param objectFilePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private KieSession getSessionfromCachedKieBase(final String objectFilePath) throws FileNotFoundException, ClassNotFoundException, IOException{
		ObjectInputStream in =
			       new ObjectInputStream( new FileInputStream( objectFilePath ) );
//			@SuppressWarnings( "unchecked" )
			KieBase kieBase = (KieBase)in.readObject();
			in.close();		
		    KieSession kieSession = kieBase.newKieSession();
		    return kieSession;
	}
}
