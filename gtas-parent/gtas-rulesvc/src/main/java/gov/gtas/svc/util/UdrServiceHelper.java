package gov.gtas.svc.util;

import static gov.gtas.rule.builder.RuleTemplateConstants.NEW_LINE;
import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.error.CommonValidationException;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;
import gov.gtas.rule.builder.RuleConditionBuilder;
import gov.gtas.rule.builder.RuleTemplateConstants;
import gov.gtas.rule.builder.util.RuleVariablesUtil;
import gov.gtas.rule.builder.util.UdrSplitterUtils;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.validation.Errors;

/**
 * Helper class for the UDR service.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrServiceHelper {
	/**
	 * Creates the header including the rule title. 
	 * @param parent
	 * @param rule
	 * @param bldr
	 */
	private static void addRuleHeader(UdrRule parent, Rule rule,
			StringBuilder bldr) {
		bldr.append("rule \"").append(parent.getTitle()).append(":")
		        .append(parent.getAuthor().getUserId()).append(":")
				.append(rule.getRuleIndex()).append("\"").append(NEW_LINE)
				.append("when\n");
	}

	public static void addEngineRulesToUdrRule(UdrRule parent,
			UdrSpecification inputJson) {
		// validate and create minterms
		List<List<QueryTerm>> mintermList = createRuleMinterms(inputJson);
		int indx = 0;
		for (List<QueryTerm> minterm : mintermList) {
			Rule rule = createEngineRule(minterm, parent, indx++);
			parent.addEngineRule(rule);
		}
	}

	/**
	 * Creates engine rules from "minterms" (i.e., sets of AND conditions). This
	 * method is called from the UDR service when a new UDR is being created.
	 * 
	 * @param parent
	 *            the parent UDR
	 * @param inputJson
	 * 
	 *            the JSON UDR object
	 * @throws ParseException
	 *             on error
	 */
	public static List<List<QueryTerm>> createRuleMinterms(
			UdrSpecification inputJson) {
		QueryObject qobj = inputJson.getDetails();
		if (qobj == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, "details",
					"Create UDR");
		}
		// validate the input JSON object
		Errors errors = QueryValidationUtils.validateQueryObject(qobj);
		if (errors.hasErrors()) {
			throw new CommonValidationException(
					"JsonToDomainObjectConverter.createEngineRules() - validation errors:",
					errors);
		}
		List<List<QueryTerm>> ruleDataList = UdrSplitterUtils
				.createFlattenedList(qobj);
		return ruleDataList;
	}

	/**
	 * Creates a new list of engine rules when a UDR is being updated.
	 * 
	 * @param parent
	 *            the UDR.
	 * @param inputJson
	 *            the update JSON object.
	 * @return list of engine rules to replace the existing rules.
	 */
	public static List<Rule> listEngineRules(UdrRule parent,
			UdrSpecification inputJson) {
		List<Rule> ret = new LinkedList<Rule>();
		List<List<QueryTerm>> ruleDataList = createRuleMinterms(inputJson);
		int indx = 0;
		for (List<QueryTerm> ruleData : ruleDataList) {
			Rule r = createEngineRule(ruleData, parent, indx);
			r.setParent(parent);
			ret.add(r);
			++indx;
		}
		return ret;
	}

	/**
	 * Creates a single engine rule from a minterm.
	 * 
	 * @param ruleData
	 *            the minterm.
	 * @param parent
	 *            the parent UDR rule.
	 * @param indx
	 *            the ordering index of the rule with respect to the parent.
	 * @return the engine rule created.
	 * @throws ParseException
	 *             parse exception.
	 */
	public static Rule createEngineRule(List<QueryTerm> ruleData,
			UdrRule parent, int indx) {

		StringBuilder stringBuilder = new StringBuilder();
		RuleConditionBuilder ruleConditionBuilder = new RuleConditionBuilder(
				RuleVariablesUtil.createEngineRuleVariableMap());

		Rule ret = new Rule(parent, indx, null);
		addRuleHeader(parent, ret, stringBuilder);
		for (QueryTerm trm : ruleData) {
			ruleConditionBuilder.addRuleCondition(trm);
		}
		ruleConditionBuilder.buildConditionsAndApppend(stringBuilder);
		List<String> causes = ruleConditionBuilder.addRuleAction(stringBuilder,
				parent, ret, RuleTemplateConstants.PASSENGER_VARIABLE_NAME);

		ret.setRuleDrl(stringBuilder.toString());
		ret.addRuleCriteria(causes);

		return ret;
	}
}
