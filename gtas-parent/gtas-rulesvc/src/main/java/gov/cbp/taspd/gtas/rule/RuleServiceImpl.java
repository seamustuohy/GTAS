package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.RuleExecutionStatistics;
import gov.cbp.taspd.gtas.bo.RuleServiceRequest;
import gov.cbp.taspd.gtas.bo.RuleServiceRequestType;
import gov.cbp.taspd.gtas.constant.RuleServiceConstants;
import gov.cbp.taspd.gtas.error.RuleServiceErrorHandler;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Flight;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

public class RuleServiceImpl implements RuleService{
	private static final String DEFAULT_RULESET_NAME = "gtas.drl";
	
    @Autowired
    private RuleServiceErrorHandler errorHandler;
	
    @Override
	public RuleServiceResult invokeRuleset(String ruleSetName, RuleServiceRequest req) {
		if(null == req){
			throw errorHandler.createException(RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE, "RuleServiceRequest", "RuleServiceImpl.invokeRuleset()");
		}
		/*
		 * object where execution statistics are collected.
		 */
		final RuleExecutionStatistics stats = new RuleExecutionStatistics();
		
		KieSession ksession = initSessionFromClasspath("GtasKS", createEventListeners(stats));
		
		List<?> reqObjectList = req.getRequestObjects();
		for(Object x:reqObjectList){
			ksession.insert(x);
		}
		
        // and fire the rules
        ksession.fireAllRules();
        
        //extract the result
        final List<?> resList = (List<?>)ksession.getGlobal( "resultList");
        
        RuleServiceResult res = new RuleServiceResult(){
        	public List<?> getResultList(){
        		return resList;
        	}
        	public RuleExecutionStatistics getExecutionStatistics(){
        		return stats;
        	}
        };        

        
        // Remove comment if using logging
        // logger.close();

        // and then dispose the session
        ksession.dispose();
		
		
		return res;
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
		}else{
			//arbitrary Message object
			ret = createRequest(requestMessage);
		}
		return ret;
	}
	/**
	 * Creates a request from a API message.
	 * @param req the API messsage
	 * @return RuleServiceRequest object
	 */
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
	 * Creates a request from an arbitrary object.
	 * @param req the input object
	 * @return RuleServiceRequest object
	 */
    private RuleServiceRequest createRequest(final gov.cbp.taspd.gtas.model.Message req){
    	final List<gov.cbp.taspd.gtas.model.Message> requestList = new ArrayList<gov.cbp.taspd.gtas.model.Message>();
    	requestList.add(req);
    	return new RuleServiceRequest(){
    		public List<?> getRequestObjects(){
    			return requestList;
    		}
    		public RuleServiceRequestType getRequestType(){
    			return RuleServiceRequestType.ANY_MESSAGE;
    		}
    		
    	};
    }
	/**
	 * Creates a list of KieSession event listeners.
	 * @return list of event listeners.
	 */
	private List<EventListener> createEventListeners(final RuleExecutionStatistics stats){
		List<EventListener> eventListenerList = new LinkedList<EventListener>();
		
		eventListenerList.add(new DebugAgendaEventListener(){

			@Override
			public void afterMatchFired(AfterMatchFiredEvent event) {
				stats.addRuleFired(event.getMatch().getRule().getName());
				super.afterMatchFired(event);
			}
			
		});
		eventListenerList.add(new DebugRuleRuntimeEventListener(){

			@Override
			public void objectUpdated(ObjectUpdatedEvent event) {
				stats.addModifiedObject(event.getObject());
				super.objectUpdated(event);
			}

			/* (non-Javadoc)
			 * @see org.kie.api.event.rule.DebugRuleRuntimeEventListener#objectDeleted(org.kie.api.event.rule.ObjectDeletedEvent)
			 */
			@Override
			public void objectDeleted(ObjectDeletedEvent event) {
				stats.addDeletedObject(event.getOldObject());
				super.objectDeleted(event);
			}

			/* (non-Javadoc)
			 * @see org.kie.api.event.rule.DebugRuleRuntimeEventListener#objectInserted(org.kie.api.event.rule.ObjectInsertedEvent)
			 */
			@Override
			public void objectInserted(ObjectInsertedEvent event) {
				stats.addInsertedObject(event.getObject());
				super.objectInserted(event);
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
        
        // From the KIE services, a container is created from the class-path
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
	
}
