package gov.gtas.svc;

import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.error.RuleServiceErrorHandler;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.UdrConstants;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.rule.RuleUtils;
import gov.gtas.rule.builder.DrlRuleFileBuilder;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.kie.api.KieBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class RuleManagementServiceImpl implements RuleManagementService{
	
	@Autowired
	private RulePersistenceService rulePersistenceService;

	@Autowired
	UserService userService;

	@PostConstruct
	private void initializeErrorHandler() {
		ErrorHandler errorHandler = new RuleServiceErrorHandler();
		ErrorHandlerFactory.registerErrorHandler(errorHandler);
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.RuleManagementService#createKnowledgeBaseFromDRLString(java.lang.String, java.lang.String)
	 */
	@Override
	public KnowledgeBase createKnowledgeBaseFromDRLString(String kbName,
			String drlString) {
		try {
			KieBase kieBase = RuleUtils.createKieBaseFromDrlString(drlString);
			byte[] kbBlob = RuleUtils.convertKieBaseToBytes(kieBase);
			KnowledgeBase kb = rulePersistenceService
					.findUdrKnowledgeBase();
			if (kb == null) {
				kb = new KnowledgeBase();
			}
			kb.setRulesBlob(drlString
					.getBytes(UdrConstants.UDR_EXTERNAL_CHARACTER_ENCODING));
			kb.setKbBlob(kbBlob);
			kb.setKbName(UdrConstants.UDR_KNOWLEDGE_BASE_NAME);
			kb = rulePersistenceService.saveKnowledgeBase(kb);
			return kb;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.SYSTEM_ERROR_CODE,
					System.currentTimeMillis(), ioe);
		}
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.RuleManagementService#fetchDrlRulesFromKnowledgeBase(java.lang.String)
	 */
	@Override
	public String fetchDrlRulesFromKnowledgeBase(String kbName) {
		KnowledgeBase kb = rulePersistenceService.findUdrKnowledgeBase(kbName);
		if(kb == null){
			throw ErrorHandlerFactory.getErrorHandler().createException(RuleServiceConstants.KB_NOT_FOUND_ERROR_CODE, UdrConstants.UDR_KNOWLEDGE_BASE_NAME);
		}
		String drlRules = null;
		try{
		    drlRules = new String(kb.getRulesBlob(), UdrConstants.UDR_EXTERNAL_CHARACTER_ENCODING);
		} catch(UnsupportedEncodingException uee){
			throw ErrorHandlerFactory.getErrorHandler().createException(RuleServiceConstants.KB_INVALID_ERROR_CODE, UdrConstants.UDR_KNOWLEDGE_BASE_NAME, uee);
		}
		return drlRules;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.RuleManagementService#fetchDefaultDrlRulesFromKnowledgeBase()
	 */
	@Override
	public String fetchDefaultDrlRulesFromKnowledgeBase() {
		String drlRules = this.fetchDrlRulesFromKnowledgeBase(UdrConstants.UDR_KNOWLEDGE_BASE_NAME);
		return drlRules;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.RuleManagementService#createKnowledgeBaseFromUdrRules(java.lang.String, java.util.Collection)
	 */
	@Override
	public KnowledgeBase createKnowledgeBaseFromUdrRules(String kbName,
			Collection<UdrRule> rules) {
		if (!CollectionUtils.isEmpty(rules)) {
			DrlRuleFileBuilder ruleFileBuilder = new DrlRuleFileBuilder();
			for (UdrRule rule : rules) {
				ruleFileBuilder.addRule(rule);
			}
			String drlRules = ruleFileBuilder.build();
			return createKnowledgeBaseFromDRLString(kbName, drlRules);
		}else{
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.RuleManagementService#deleteKnowledgeBase(java.lang.String)
	 */
	@Override
	public KnowledgeBase deleteKnowledgeBase(String kbName) {
		KnowledgeBase kb = rulePersistenceService.deleteKnowledgeBase(kbName);
		return kb;
	}

}
