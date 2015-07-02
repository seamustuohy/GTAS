package gov.gtas.model.udr.json.util;

import gov.gtas.model.udr.ConditionValueTypeEnum;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.EntityLookupEnum;
import gov.gtas.model.udr.OperatorCodeEnum;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryConditionEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class UdrSpecificationBuilder {
	private Stack<QueryObject> queryObjectStack;
	private Long id;
	private MetaData meta;

	public UdrSpecificationBuilder(Long id) {
		this.id = id;
		queryObjectStack = new Stack<QueryObject>();
	}

	public UdrSpecificationBuilder(Long id, QueryConditionEnum condition) {
		this.id = id;
		queryObjectStack = new Stack<QueryObject>();
		this.addNestedQueryObject(condition);
	}

	public UdrSpecification build() {
		while (queryObjectStack.size() > 1) {
			QueryObject obj = queryObjectStack.pop();
			queryObjectStack.peek().getRules().add(obj);
		}
		QueryObject obj = null;
		if (queryObjectStack.size() > 0) {
			obj = queryObjectStack.pop();
		}
		return new UdrSpecification(id, obj, meta);
	}

	public UdrSpecificationBuilder addMeta(String title, String descr,
			Date startDate, Date endDate, boolean enabled, String author) {
		meta = new MetaData(title, descr, startDate, author);
		meta.setAuthor(author);
		meta.setEnabled(enabled);
		meta.setEndDate(endDate);
		return this;
	}

	public UdrSpecificationBuilder addTerm(EntityLookupEnum entity,
			String attr, ConditionValueTypeEnum type, OperatorCodeEnum op,
			String[] val) {
		queryObjectStack
				.peek()
				.getRules()
				.add(new QueryTerm(entity.toString(), attr, type.toString(), op
						.toString(), val));
		return this;
	}

	public UdrSpecificationBuilder addNestedQueryObject(
			QueryConditionEnum condition) {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition(condition.toString());
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		queryObject.setRules(rules);
		queryObjectStack.push(queryObject);
		return this;
	}

	public UdrSpecificationBuilder addSiblingQueryObject(
			QueryConditionEnum condition) {
		if (queryObjectStack.size() <= 1) {
			throw new RuntimeException(
					"UdrSpecificationBuilder.addSiblingQueryObject() - Cannot add sibling to stack size ="
							+ queryObjectStack.size());
		}
		// first add the previous sibling to parent
		QueryObject lastChild = queryObjectStack.pop();
		queryObjectStack.peek().getRules().add(lastChild);

		QueryObject queryObject = new QueryObject();
		queryObject.setCondition(condition.toString());
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		queryObject.setRules(rules);
		queryObjectStack.push(queryObject);
		return this;
	}

	/**
	 * Creates a sample UDR specification JSON object.
	 * 
	 * @return UDR JSON object.
	 */
	public static UdrSpecification createSampleSpec() {
		return createSampleSpec("jpjones");
	}
	public static UdrSpecification createSampleSpec(String userId) {
		final UdrSpecificationBuilder bldr = new UdrSpecificationBuilder(null,
				QueryConditionEnum.OR);
		bldr.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_DOB,
				ConditionValueTypeEnum.DATE, OperatorCodeEnum.EQUAL,
				new String[] { new Date().toString() });
		bldr.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_LAST_NAME,
				ConditionValueTypeEnum.STRING, OperatorCodeEnum.EQUAL,
				new String[] { "Jones" });
		bldr.addNestedQueryObject(QueryConditionEnum.AND);
		bldr.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME,
				ConditionValueTypeEnum.STRING, OperatorCodeEnum.IN,
				new String[] { "DBY", "PKY", "FLT" });
		bldr.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_DEBARKATION_AIRPORT_NAME,
				ConditionValueTypeEnum.STRING, OperatorCodeEnum.EQUAL,
				new String[] { "IAD" });
		bldr.addMeta("Hello Rule 1", "This is a test", new Date(), null, true,
				userId);
		return bldr.build();
	}
}
