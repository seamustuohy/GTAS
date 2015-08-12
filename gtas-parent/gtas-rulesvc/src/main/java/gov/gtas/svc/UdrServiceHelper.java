package gov.gtas.svc;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.validation.Errors;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonValidationException;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleCondPk;
import gov.gtas.model.udr.UdrConstants;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.udr.json.QueryConditionEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;
import gov.gtas.rule.builder.util.UdrSplitterUtils;
import gov.gtas.util.ValidationUtils;

/**
 * Helper class for the UDR service.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrServiceHelper {
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
	public static List<List<QueryTerm>> createRuleMinterms(UdrSpecification inputJson) {
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
		List<List<QueryTerm>> ruleDataList = UdrSplitterUtils.createFlattenedList(qobj);
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
		QueryObject qobj = inputJson.getDetails();
		List<List<QueryTerm>> ruleDataList = qobj.createFlattenedList();
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
	private static Rule createEngineRule(List<QueryTerm> ruleData,
			UdrRule parent, int indx) {
		Rule ret = new Rule(parent, indx, null);
		int seq = 0;
		for (QueryTerm trm : ruleData) {
			RuleCond cond = null;
			try {
				OperatorCodeEnum op = OperatorCodeEnum.valueOf(trm
						.getOperator().toUpperCase());
				ValueTypesEnum type = ValueTypesEnum.valueOf(trm.getType()
						.toUpperCase());
				RuleCondPk pk = new RuleCondPk(indx, seq++);
				EntityEnum entity = ValidationUtils
						.convertStringToEnum(trm.getEntity());
				if (entity == null) {
					throw ErrorHandlerFactory
							.getErrorHandler()
							.createException(
									CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE,
									"entity:" + trm.getEntity(),
									"createEngineRule()");
				}
				cond = new RuleCond(pk, entity, trm.getField(), op);
				/////////////////////////////////addValuesToCond(cond, op, type, trm.getValue());
//			} catch (ParseException pe) {
//				StringBuilder bldr = new StringBuilder("[");
//				for (String val : trm.getValue()) {
//					bldr.append(val).append(",");
//				}
//				bldr.append("]");
//				throw ErrorHandlerFactory.getErrorHandler().createException(
//						CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_CODE,
//						bldr.toString(), trm.getType(), "Engine Rule Creation");
			} catch (NullPointerException | IllegalArgumentException ex) {
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE,
						String.format("QueryTerm (operator=%s, type=%s)",
								trm.getOperator(), trm.getType()),
						"Engine Rule Creation");

			}
			ret.addConditionToRule(cond);
		}
		return ret;
	}


}
