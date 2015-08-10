package gov.gtas.model.udr.json.util;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonValidationException;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.User;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleCondPk;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.enumtype.YesNoEnum;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;
import gov.gtas.util.ValidationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.validation.Errors;

/**
 * Utility functions to convert JSON objects to JPA domain objects.
 * 
 * @author GTAS3 (AB)
 *
 */
public class JsonToDomainObjectConverter {
	/**
	 * Creates a meta data domain object from the JSON UDR specification object.
	 * 
	 * @param inputJson
	 *            the UDR JSON object.
	 * @return RuleMeta domain object
	 */

	private static RuleMeta extractRuleMeta(final UdrSpecification inputJson,
			final boolean checkStartDate) {
		final MetaData metaData = inputJson.getSummary();

		JsonValidationUtils.validateMetaData(metaData, checkStartDate);

		final Long id = inputJson.getId();
		final String title = metaData.getTitle();
		final Date startDate = metaData.getStartDate();
		final String descr = metaData.getDescription();
		final YesNoEnum enabled = metaData.isEnabled() ? YesNoEnum.Y
				: YesNoEnum.N;
		final Date endDate = metaData.getEndDate();

		RuleMeta ret = createRuleMeta(id, title, descr, startDate, endDate,
				enabled);
		return ret;
	}

	public static RuleMeta extractRuleMeta(final UdrSpecification inputJson) {
		return extractRuleMeta(inputJson, true);
	}

	public static RuleMeta extractRuleMetaUpdates(UdrSpecification inputJson) {
		return extractRuleMeta(inputJson, false);
	}

	/**
	 * Converts the rule details portion of the UDR JSON specifications object
	 * (i.e., QueryObject) object to a blob.
	 * 
	 * @param qObj
	 *            the QueryObject part of JSON UDR object.
	 * @return the binary BLOB data corresponding to the QueryObject.
	 * @throws IOException
	 *             on error
	 * @throws ClassNotFoundException
	 *             on error
	 */
	public static byte[] convertQueryObjectToBlob(QueryObject qObj)
			throws IOException, ClassNotFoundException {
		if (qObj != null) {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final GZIPOutputStream gzipOutStream = new GZIPOutputStream(bos);
			final ObjectOutputStream out = new ObjectOutputStream(gzipOutStream);
			out.writeObject(qObj);
			out.close();
			byte[] bytes = bos.toByteArray();
			return bytes;
		} else {
			return null;
		}
	}

	/**
	 * Extracts the JSON UdrSpecification object from the UdrRule domain object
	 * fetched from the DB.
	 * 
	 * @param rule
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static UdrSpecification getJsonFromUdrRule(UdrRule rule)
			throws IOException, ClassNotFoundException {
		QueryObject qObj = null;
		MetaData meta = createMetadataFromUdrRule(rule);
		if (rule.getUdrConditionObject() != null) {
			final ByteArrayInputStream bis = new ByteArrayInputStream(
					rule.getUdrConditionObject());
			final GZIPInputStream gzipInStream = new GZIPInputStream(bis);
			final ObjectInputStream in = new ObjectInputStream(gzipInStream);
			qObj = (QueryObject) in.readObject();
			in.close();
		}
		// create the UDR spec object
		UdrSpecification ret = new UdrSpecification(rule.getId(), qObj, meta);

		return ret;
	}

	/**
	 * Creates a JSON meta data object from the meta data information in a
	 * domain UDR rule object.
	 * 
	 * @param uRule
	 *            the domain UDR rule object.
	 * @return JSON meta data object (i.e., the summary item)
	 */
	private static MetaData createMetadataFromUdrRule(UdrRule uRule) {
		RuleMeta ruleMeta = uRule.getMetaData();
		String authorUserId = uRule.getAuthor().getUserId();
		final MetaData ret = new MetaData(ruleMeta.getTitle(),
				ruleMeta.getDescription(), ruleMeta.getStartDt(), authorUserId);

		ret.setEnabled(ruleMeta.getEnabled() == YesNoEnum.Y ? true : false);
		ret.setEndDate(ruleMeta.getEndDt());

		return ret;
	}

	/**
	 * Creates a domain UdrRule object from the JSON UdrSpecification object.
	 * (Note: this object can be inserted/updated into the DB.)
	 * 
	 * @param inputJson
	 * @return
	 * @throws IOException
	 */
	public static UdrRule createUdrRuleFromJson(UdrSpecification inputJson,
			User author) throws IOException {
		if (inputJson == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, "inputJson",
					"JsonToDomainObjectConverter.createUdrRuleFromJson()");
		}

		final RuleMeta metaData = extractRuleMeta(inputJson);

		final UdrRule rule = createUdrRule(inputJson.getId(), metaData,
				createQueryObjectBlob(inputJson), author);
		createEngineRules(rule, inputJson);

		return rule;

	}

	/**
	 * Creates engine rules from "minterms" (i.e., sets of AND conditions). This
	 * method is called from the UDR service when a new UDR is being created.
	 * 
	 * @param parent
	 *            the parent UDR
	 * @param inputJson
	 *            the JSON UDR object
	 * @throws ParseException
	 *             on error
	 */
	public static void createEngineRules(UdrRule parent,
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
		List<List<QueryTerm>> ruleDataList = qobj.createFlattenedList();
		int indx = 0;
		for (List<QueryTerm> ruleData : ruleDataList) {
			parent.addEngineRule(createEngineRule(ruleData, parent, indx));
			++indx;
		}
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
				addValuesToCond(cond, op, type, trm.getValue());
			} catch (ParseException pe) {
				StringBuilder bldr = new StringBuilder("[");
				for (String val : trm.getValue()) {
					bldr.append(val).append(",");
				}
				bldr.append("]");
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.INPUT_JSON_FORMAT_ERROR_CODE,
						bldr.toString(), trm.getType(), "Engine Rule Creation");
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

	/**
	 * Does validation check and adds condition value(s) to the Rule condition
	 * object.
	 * 
	 * @param cond
	 *            the rule condition object
	 * @param op
	 *            the operator.
	 * @param type
	 *            the type of value.
	 * @param value
	 *            single value.
	 * @param values
	 *            multiple values for multi-value operator.
	 * @throws ParseException
	 *             on format exception.
	 */
	private static void addValuesToCond(RuleCond cond, OperatorCodeEnum op,
			ValueTypesEnum type, String[] values) throws ParseException {
		if (op == OperatorCodeEnum.IN || op == OperatorCodeEnum.NOT_IN) {
			if (values != null && values.length > 0) {
				cond.addValuesToCondition(values, type);
			} else {
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE,
						"value", "createEngineRule");
			}
		} else if (op == OperatorCodeEnum.BETWEEN) {
			if (values != null && values.length == 2) {
				cond.addValuesToCondition(values, type);
			} else {
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE,
						"value (requires an array of 2 strings)", "createEngineRule");
			}
		} else {
			String valueToAdd = null;
			if (values != null && values.length == 1) {
				valueToAdd = values[0];
			} else {
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE,
						"value (requires exactly 1 value)", "createEngineRule");
			}
			cond.addValueToCondition(valueToAdd, type);
		}

	}

	/**
	 * Converts a "detail" portion of the UDR JSON object to compressed binary
	 * data for saving in the database as a BLOB.
	 * 
	 * @param json
	 *            the JSON object to serialize.
	 * @return the binary blob object
	 */
	private static byte[] createQueryObjectBlob(UdrSpecification json)
			throws IOException {
		QueryObject qObj = json.getDetails();
		byte[] retBlob = null;
		if (qObj != null) {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final GZIPOutputStream gzipOutStream = new GZIPOutputStream(bos);
			final ObjectOutputStream out = new ObjectOutputStream(gzipOutStream);
			out.writeObject(qObj);
			out.close();
			retBlob = bos.toByteArray();
		}
		return retBlob;
	}

	/**
	 * Creates a UdrRule domain object.
	 * 
	 * @param id
	 *            the Id of the domain UDR Rule object.
	 * @param title
	 *            the title of the rule.
	 * @param descr
	 *            the rule description.
	 * @param enabled
	 *            enabled state of the rule.
	 * @return the UDR rule domain object.
	 */
	private static UdrRule createUdrRule(Long id, RuleMeta meta,
			byte[] queryObjectBlob, User author) {
		UdrRule rule = new UdrRule();
		rule.setId(id);
		rule.setDeleted(YesNoEnum.N);
		rule.setEditDt(new Date());
		rule.setAuthor(author);
		rule.setTitle(meta.getTitle());
		rule.setMetaData(meta);
		rule.setUdrConditionObject(queryObjectBlob);
		return rule;
	}

	/**
	 * Creates the meta data portion of the UdrRule domain object.
	 * 
	 * @param id
	 *            the Id of the domain UDR Rule object.
	 * @param title
	 *            the title of the rule.
	 * @param descr
	 *            the rule description.
	 * @param startDate
	 *            the day the rule should become active (if enabled).
	 * @param endDate
	 *            the day the rule should cease to be active (optional).
	 * @param enabled
	 *            enabled state of the rule.
	 * @return
	 */
	private static RuleMeta createRuleMeta(Long id, String title, String descr,
			Date startDate, Date endDate, YesNoEnum enabled) {
		RuleMeta meta = new RuleMeta();
		if (id != null) {
			meta.setId(id);
		}
		meta.setDescription(descr);
		meta.setEnabled(enabled);
		meta.setHitSharing(YesNoEnum.N);
		meta.setPriorityHigh(YesNoEnum.N);
		meta.setStartDt(startDate);
		meta.setEndDt(endDate);
		meta.setTitle(title);
		return meta;
	}

}
