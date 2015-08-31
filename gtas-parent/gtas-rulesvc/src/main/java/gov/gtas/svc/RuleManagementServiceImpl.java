package gov.gtas.svc;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.constant.RuleConstants;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.error.RuleServiceErrorHandler;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.rule.RuleUtils;
import gov.gtas.rule.builder.DrlRuleFileBuilder;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of the Knowledge Base and Rule management service interface.
 * 
 * @author GTAS3
 *
 */
@Service
public class RuleManagementServiceImpl implements RuleManagementService {

	@Autowired
	private RulePersistenceService rulePersistenceService;

	@Autowired
	UserService userService;

	@PostConstruct
	private void initializeErrorHandler() {
		ErrorHandler errorHandler = new RuleServiceErrorHandler();
		ErrorHandlerFactory.registerErrorHandler(errorHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.RuleManagementService#createKnowledgeBaseFromDRLString(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public KnowledgeBase createKnowledgeBaseFromDRLString(String kbName,
			String drlString) {
		try {
			KieBase kieBase = RuleUtils.createKieBaseFromDrlString(drlString);
			byte[] kbBlob = RuleUtils.convertKieBaseToBytes(kieBase);
			KnowledgeBase kb = rulePersistenceService.findUdrKnowledgeBase(kbName);
			if (kb == null) {
				kb = new KnowledgeBase(kbName);
			}
			kb.setRulesBlob(drlString
					.getBytes(RuleConstants.UDR_EXTERNAL_CHARACTER_ENCODING));
			kb.setKbBlob(kbBlob);
			if(StringUtils.isEmpty(kbName)){
			    kb.setKbName(RuleConstants.UDR_KNOWLEDGE_BASE_NAME);
			}
			kb = rulePersistenceService.saveKnowledgeBase(kb);
			return kb;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.SYSTEM_ERROR_CODE,
					System.currentTimeMillis(), ioe);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.RuleManagementService#fetchDrlRulesFromKnowledgeBase(java
	 * .lang.String)
	 */
	@Override
	public String fetchDrlRulesFromKnowledgeBase(String kbName) {
		KnowledgeBase kb = rulePersistenceService.findUdrKnowledgeBase(kbName);
		if (kb == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.KB_NOT_FOUND_ERROR_CODE,
					RuleConstants.UDR_KNOWLEDGE_BASE_NAME);
		}
		String drlRules = null;
		try {
			drlRules = new String(kb.getRulesBlob(),
					RuleConstants.UDR_EXTERNAL_CHARACTER_ENCODING);
		} catch (UnsupportedEncodingException uee) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.KB_INVALID_ERROR_CODE,
					RuleConstants.UDR_KNOWLEDGE_BASE_NAME, uee);
		}
		return drlRules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.RuleManagementService#fetchDefaultDrlRulesFromKnowledgeBase
	 * ()
	 */
	@Override
	public String fetchDefaultDrlRulesFromKnowledgeBase() {
		String drlRules = this
				.fetchDrlRulesFromKnowledgeBase(RuleConstants.UDR_KNOWLEDGE_BASE_NAME);
		return drlRules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.RuleManagementService#createKnowledgeBaseFromUdrRules(java
	 * .lang.String, java.util.Collection)
	 */
	@Override
	@Transactional(value=TxType.MANDATORY)
	public KnowledgeBase createKnowledgeBaseFromUdrRules(String kbName,
			Collection<UdrRule> rules, String userId) {
		if (!CollectionUtils.isEmpty(rules)) {
			DrlRuleFileBuilder ruleFileBuilder = new DrlRuleFileBuilder();
			for (UdrRule rule : rules) {
				ruleFileBuilder.addRule(rule);
			}
			String drlRules = ruleFileBuilder.build();
			KnowledgeBase kb = createKnowledgeBaseFromDRLString(kbName, drlRules);
			linkRulesToKnowledgeBase(kb, rules);
			return kb;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.RuleManagementService#createKnowledgeBaseFromWatchlistItems(java.lang.String, java.lang.Iterable)
	 */
	@Override
	@Transactional(value=TxType.MANDATORY)
	public KnowledgeBase createKnowledgeBaseFromWatchlistItems(String kbName,
			Iterable<WatchlistItem> rules) {
		if (rules != null) {
			DrlRuleFileBuilder ruleFileBuilder = new DrlRuleFileBuilder();
			for (WatchlistItem rule : rules) {
				ruleFileBuilder.addWatchlistItemRule(rule);
			}
			String drlRules = ruleFileBuilder.build();
			KnowledgeBase kb = createKnowledgeBaseFromDRLString(kbName, drlRules);
			return kb;
		} else {
			return null;
		}
	}

	private void linkRulesToKnowledgeBase(KnowledgeBase kb, Collection<UdrRule> rules){
		if(kb != null && kb.getId() != null){
			List<Rule> ruleList = new LinkedList<Rule>();
			for (UdrRule rule : rules) {
				for(Rule  engineRule:rule.getEngineRules()){
					engineRule.setKnowledgeBase(kb);
				}
				ruleList.addAll(rule.getEngineRules());
			}
			rulePersistenceService.batchUpdate(ruleList);			
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.RuleManagementService#deleteKnowledgeBase(java.lang.String)
	 */
	@Override
	@Transactional(value=TxType.MANDATORY)
	public KnowledgeBase deleteKnowledgeBase(String kbName) {
		KnowledgeBase kb = rulePersistenceService.findUdrKnowledgeBase(kbName);
		if(kb!= null){
			List<Rule> ruleList = rulePersistenceService.findRulesByKnowledgeBaseId(kb.getId());
			for (Rule rule : ruleList) {
				rule.setKnowledgeBase(null);
				ruleList.add(rule);
			}
			rulePersistenceService.batchUpdate(ruleList);			
			kb = rulePersistenceService.deleteKnowledgeBase(kbName);
		}
		return kb;
	}

}
