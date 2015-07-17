package gov.gtas.rule;

import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.error.RuleServiceErrorHandler;
import gov.gtas.model.ApisMessage;
import gov.gtas.services.udr.RulePersistenceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;

import javax.annotation.PostConstruct;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Rule Engine Service.
 * 
 * @author GTAS3 (AB)
 *
 */
@Service
public class RuleServiceImpl implements RuleService {
	@Autowired
	private RulePersistenceService rulePersistenceService;

	@PostConstruct
	public void initializeErrorHandling() {
		ErrorHandler errorHandler = new RuleServiceErrorHandler();
		ErrorHandlerFactory.registerErrorHandler(errorHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.rule.RuleService#invokeRuleset(java.lang.String,
	 * gov.gtas.bo.RuleServiceRequest)
	 */
	@Override
	public RuleServiceResult invokeAdhocRules(String rulesFilePath,
			RuleServiceRequest req) {
		if (null == req) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"RuleServiceRequest", "RuleServiceImpl.invokeRuleset()");
		}
		KieBase kbase = null;
		try {
			kbase = RuleUtils.createKieBaseFromClasspathFile(rulesFilePath);
		} catch (IOException ioe) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.KB_CREATION_IO_ERROR_CODE, ioe,
					"RuleServiceImpl.invokeAdhocRules() with file:"+rulesFilePath);
		}
		return createSessionAndExecuteRules(kbase, req);
		// /*
		// * object where execution statistics are collected.
		// */
		// final RuleExecutionStatistics stats = new RuleExecutionStatistics();
		//
		// KieSession ksession =
		// initSessionFromClasspath(RuleServiceConstants.KNOWLEDGE_SESSION_NAME,
		// RuleEngineHelper.createEventListeners(stats));
		//
		// Collection<?> requestObjects = req.getRequestObjects();
		// for (Object x : requestObjects) {
		// ksession.insert(x);
		// }
		//
		// // and fire the rules
		// ksession.fireAllRules();
		//
		// // extract the result
		// final List<?> resList = (List<?>)
		// ksession.getGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME);
		//
		// RuleServiceResult res = new RuleServiceResult() {
		// public List<?> getResultList() {
		// return resList;
		// }
		//
		// public RuleExecutionStatistics getExecutionStatistics() {
		// return stats;
		// }
		// };
		//
		// // Remove comment if using logging
		// // logger.close();
		//
		// // and then dispose the session
		// ksession.dispose();
		//
		// return res;
	}

	/**
	 * Creates a session from a KieBase, loads the request objects and fires all
	 * rules.
	 * 
	 * @param kbase
	 *            the KIE knowledge base containing the rules.
	 * @param req
	 *            the request object container.
	 * @return the rule execution result.
	 */
	private RuleServiceResult createSessionAndExecuteRules(KieBase kbase,
			RuleServiceRequest req) {
		KieSession ksession = kbase.newKieSession();
		ksession.setGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME,
				new ArrayList<Object>());

		/*
		 * object where execution statistics are collected.
		 */
		final RuleExecutionStatistics stats = new RuleExecutionStatistics();

		List<EventListener> listeners = RuleEngineHelper
				.createEventListeners(stats);
		RuleEngineHelper.addEventListenersToKieSEssion(ksession, listeners);

		Collection<?> requestObjects = req.getRequestObjects();
		for (Object x : requestObjects) {
			ksession.insert(x);
		}

		// and fire the rules
		ksession.fireAllRules();

		// extract the result
		final List<?> resList = (List<?>) ksession
				.getGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME);

		RuleServiceResult res = new RuleServiceResult() {
			public List<?> getResultList() {
				return resList;
			}

			public RuleExecutionStatistics getExecutionStatistics() {
				return stats;
			}
		};

		// Remove comment if using logging
		// logger.close();

		// and then dispose the session
		ksession.dispose();

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.rule.RuleService#invokeRuleset(gov.gtas.bo.RuleServiceRequest)
	 */
	@Override
	public RuleServiceResult invokeRuleEngine(RuleServiceRequest req) {
		return invokeAdhocRules(RuleServiceConstants.DEFAULT_RULESET_NAME, req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.rule.RuleService#createRuleServiceRequest(gov.gtas.model.Message
	 * )
	 */
	@Override
	public RuleServiceRequest createRuleServiceRequest(
			final gov.gtas.model.Message requestMessage) {
		RuleServiceRequest ret = null;
		if (requestMessage instanceof ApisMessage) {
			ret = RuleEngineHelper
					.createApisRequest((ApisMessage) requestMessage);
		} else {
			// arbitrary Message object
			ret = RuleEngineHelper.createRequest(requestMessage);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.rule.RuleService#invokeAdhocRulesFRomString(java.lang.String,
	 * gov.gtas.bo.RuleServiceRequest)
	 */
	@Override
	public RuleServiceResult invokeAdhocRulesFRomString(String rules,
			RuleServiceRequest req) {
		KieBase kbase = null;
		try {
			kbase = RuleUtils.createKieBaseFromDrlString(rules);
		} catch (IOException ioe) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.KB_CREATION_IO_ERROR_CODE, ioe,
					"RuleServiceImpl.invokeAdhocRulesFRomString()");
		}
		return createSessionAndExecuteRules(kbase, req);
	}

	/**
	 * Creates a simple rule session from the provided session name. Note: The
	 * session name must be configured in the KieModule configuration file
	 * (META-INF/kmodule.xml).
	 * 
	 * @param sessionName
	 *            the session name.
	 * @param eventListenerList
	 *            the list of event listeners to attach to the session.
	 * @return the created session.
	 */
	private KieSession initSessionFromClasspath(final String sessionName,
			final List<EventListener> eventListenerList) {
		// KieServices is the factory for all KIE services
		KieServices ks = KieServices.Factory.get();

		// From the KIE services, a container is created from the class-path
		KieContainer kc = ks.getKieClasspathContainer();

		// From the container, a session is created based on
		// its definition and configuration in the META-INF/kmodule.xml file
		KieSession ksession = kc.newKieSession(sessionName);

		// Once the session is created, the application can interact with it
		// In this case it is setting a global as defined in the
		// gov/gtas/rule/gtas.drl file
		ksession.setGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME,
				new ArrayList<Object>());

		// The application can also setup listeners
		if (eventListenerList != null) {
			for (EventListener el : eventListenerList) {
				if (el instanceof AgendaEventListener) {
					ksession.addEventListener((AgendaEventListener) el);
				} else if (el instanceof RuleRuntimeEventListener) {
					ksession.addEventListener((RuleRuntimeEventListener) el);
				}
			}
		}

		// To setup a file based audit logger, uncomment the next line
		// KieRuntimeLogger logger = ks.getLoggers().newFileLogger( ksession,
		// "./helloworld" );

		// To setup a ThreadedFileLogger, so that the audit view reflects events
		// whilst debugging,
		// uncomment the next line
		// KieRuntimeLogger logger = ks.getLoggers().newThreadedFileLogger(
		// ksession, "./helloworld", 1000 );

		// Remove comment if using logging
		// logger.close();

		return ksession;
	}

}
