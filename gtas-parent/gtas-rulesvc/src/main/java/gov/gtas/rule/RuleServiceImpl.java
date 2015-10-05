package gov.gtas.rule;

import gov.gtas.bo.BasicRuleServiceResult;
import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.error.RuleServiceErrorHandler;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.rule.listener.RuleEventListenerUtils;
import gov.gtas.services.udr.RulePersistenceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieBase;
import org.kie.api.runtime.Globals;
import org.kie.api.runtime.StatelessKieSession;
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
					RuleServiceConstants.KB_CREATION_IO_ERROR_CODE,
					ioe,
					"RuleServiceImpl.invokeAdhocRules() with file:"
							+ rulesFilePath);
		}
		return createSessionAndExecuteRules(kbase, req);
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
		StatelessKieSession ksession = kbase.newStatelessKieSession();
		ksession.setGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME,
				new ArrayList<Object>());

		/*
		 * object where execution statistics are collected.
		 */
		final RuleExecutionStatistics stats = new RuleExecutionStatistics();

		List<EventListener> listeners = RuleEventListenerUtils
				.createEventListeners(stats);
		RuleEventListenerUtils.addEventListenersToKieSEssion(ksession,
				listeners);

		Collection<?> requestObjects = req.getRequestObjects();
		ksession.execute(requestObjects);

		Globals globals = ksession.getGlobals();
		Collection<String> keys = globals.getGlobalKeys();

		Iterator<String> iter = keys.iterator();
		RuleServiceResult res = null;

		while (iter.hasNext()) {
			if (iter.next().equals(RuleServiceConstants.RULE_RESULT_LIST_NAME)) {
				@SuppressWarnings("unchecked")
				List<RuleHitDetail> resList = (List<RuleHitDetail>) globals
						.get(RuleServiceConstants.RULE_RESULT_LIST_NAME);
				res = new BasicRuleServiceResult(resList, stats);
			}
		}

		//
		// // extract the result
		// @SuppressWarnings("unchecked")
		// final List<RuleHitDetail> resList = (List<RuleHitDetail>) ksession
		// .getGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME);
		//
		// RuleServiceResult res = new BasicRuleServiceResult(resList, stats);
		//
		// // Remove comment if using logging
		// // logger.close();
		//
		// // and then dispose the session
		// ksession.dispose();

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.rule.RuleService#invokeRuleEngine(gov.gtas.bo.RuleServiceRequest
	 * , java.lang.String)
	 */
	@Override
	public RuleServiceResult invokeRuleEngine(RuleServiceRequest req,
			String kbName) {
		KnowledgeBase kbRecord = null;
		if (StringUtils.isEmpty(kbName)) {
			kbRecord = rulePersistenceService.findUdrKnowledgeBase();
		} else {
			kbRecord = rulePersistenceService.findUdrKnowledgeBase(kbName);
		}
		if (kbRecord == null) {
			return null;
		}
		try {
			KieBase kb = RuleUtils
					.convertKieBasefromBytes(kbRecord.getKbBlob());
			return createSessionAndExecuteRules(kb, req);
		} catch (IOException | ClassNotFoundException ex) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.KB_DESERIALIZATION_ERROR_CODE, ex,
					kbRecord.getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.rule.RuleService#invokeRuleset(gov.gtas.bo.RuleServiceRequest)
	 */
	@Override
	public RuleServiceResult invokeRuleEngine(RuleServiceRequest req) {
		return invokeRuleEngine(req, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.rule.RuleService#invokeAdhocRulesFRomString(java.lang.String,
	 * gov.gtas.bo.RuleServiceRequest)
	 */
	@Override
	public RuleServiceResult invokeAdhocRulesFromString(String rules,
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
}
