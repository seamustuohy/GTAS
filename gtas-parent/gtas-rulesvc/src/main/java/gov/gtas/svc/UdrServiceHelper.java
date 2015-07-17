package gov.gtas.svc;

import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.UdrConstants;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.rule.RuleUtils;
import gov.gtas.rule.builder.DrlRuleFileBuilder;
import gov.gtas.services.udr.RulePersistenceService;

import java.io.IOException;
import java.util.List;

import org.kie.api.KieBase;
import org.springframework.util.CollectionUtils;

/**
 * Helper class for the UDR service.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrServiceHelper {
	public static JsonServiceResponse createResponse(boolean success,
			String op, UdrRule rule) {
		JsonServiceResponse resp = null;
		if (success) {
			resp = new JsonServiceResponse(
					JsonServiceResponse.SUCCESS_RESPONSE,
					"UDR Service",
					op,
					String.format(
							op
									+ " on UDR Rule with title='%s' and ID='%s' was successful.",
							rule.getTitle(), rule.getId()));
			resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute(
					UdrConstants.UDR_ID_ATTRIBUTE_NAME, String.valueOf(rule
							.getId())));
			resp.addResponseDetails(new JsonServiceResponse.ServiceResponseDetailAttribute(
					UdrConstants.UDR_TITLE_ATTRIBUTE_NAME, String.valueOf(rule
							.getTitle())));
		} else {
			if (rule != null) {
				resp = new JsonServiceResponse(
						JsonServiceResponse.FAILURE_RESPONSE,
						"UDR Service",
						op,
						String.format(
								op
										+ " on UDR Rule with title='%s' and ID='%s' failed.",
								rule.getTitle(), rule.getId()));
			} else {
				resp = new JsonServiceResponse(
						JsonServiceResponse.FAILURE_RESPONSE, "UDR Service",
						op, op + " failed.");

			}

		}
		return resp;
	}

	/**
	 * Generates DROOLS Knowledge Base from generate3d rules and persists them
	 * in the DB.
	 * 
	 * @param rulePersistenceService the rule persistence service to use.
	 */
	public static void processRuleGeneration(
			RulePersistenceService rulePersistenceService) {
		List<UdrRule> ruleList = rulePersistenceService.findAll();
		if (!CollectionUtils.isEmpty(ruleList)) {
			DrlRuleFileBuilder ruleFileBuilder = new DrlRuleFileBuilder();
			for (UdrRule rule : ruleList) {
				ruleFileBuilder.addRule(rule);
			}
			try {
				String rules = ruleFileBuilder.build();
				KieBase kieBase = RuleUtils.createKieBaseFromDrlString(rules);
				byte[] kbBlob = RuleUtils.convertKieBaseToBytes(kieBase);
				KnowledgeBase kb = rulePersistenceService
						.findUdrKnowledgeBase();
				if (kb == null) {
					kb = new KnowledgeBase();
				}
				kb.setRulesBlob(rules
						.getBytes(UdrConstants.UDR_EXTERNAL_CHARACTER_ENCODING));
				kb.setKbBlob(kbBlob);
				rulePersistenceService.saveKnowledgeBase(kb);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.SYSTEM_ERROR_CODE,
						System.currentTimeMillis());
			}
		}
	}

}
