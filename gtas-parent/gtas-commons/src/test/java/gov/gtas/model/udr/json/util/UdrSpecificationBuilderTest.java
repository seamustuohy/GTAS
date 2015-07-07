package gov.gtas.model.udr.json.util;

import static org.junit.Assert.*;

import java.util.List;

import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.json.QueryConditionEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UdrSpecificationBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = RuntimeException.class)
	public void testInvalidConstructon() {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(null,
				QueryConditionEnum.OR);
		// cannot add sibling to top level
		builder.addSiblingQueryObject(QueryConditionEnum.AND);
	}

	@Test
	public void testSimple() {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(null,
				QueryConditionEnum.OR);
		// add terms and then another query object
		builder.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_DEBARKATION_AIRPORT_NAME,
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL,
				new String[] { "IAD" });
		builder.addNestedQueryObject(QueryConditionEnum.AND);
		builder.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_LAST_NAME,
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL,
				new String[] { "Jones" });
		builder.addTerm(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME,
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL,
				new String[] { "DBY" });

		UdrSpecification spec = builder.build();
		assertNotNull(spec);
		assertNotNull(spec.getDetails());
		QueryObject topLevel = spec.getDetails();
		assertEquals(QueryConditionEnum.OR.toString(), topLevel.getCondition());
		assertNull(spec.getId());
		assertNull(spec.getSummary());
		
		assertEquals(2, topLevel.getRules().size());
		List<QueryEntity> rules = topLevel.getRules();
		assertEquals(2, rules.size());
		verifyQueryTerm(rules.get(0), EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_DEBARKATION_AIRPORT_NAME,
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL,
				new String[] { "IAD" });
		assertTrue(rules.get(1) instanceof QueryObject);
		QueryObject embedded = (QueryObject) (rules.get(1));
		assertEquals(QueryConditionEnum.AND.toString(), embedded.getCondition());
		rules = embedded.getRules();
		assertEquals(2, rules.size());
		verifyQueryTerm(rules.get(0), EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_LAST_NAME,
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL,
				new String[] { "Jones" });
		verifyQueryTerm(rules.get(1), EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME,
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL,
				new String[] { "DBY" });
	}

	private void verifyQueryTerm(QueryEntity obj, EntityLookupEnum entity,
			String attr, ValueTypesEnum type, OperatorCodeEnum op,
			String[] val) {
		assertTrue(obj instanceof QueryTerm);
		QueryTerm term = (QueryTerm)obj;
		assertEquals("verifyQueryTerm - entity does not match", entity.toString(), term.getEntity());
		assertEquals("verifyQueryTerm - attribute does not match", attr, term.getField());
		assertEquals("verifyQueryTerm - type does not match", type.toString(), term.getType());
		assertEquals("verifyQueryTerm - operator does not match", op.toString(), term.getOperator());
		assertEquals("verifyQueryTerm - value does not match", val[0], term.getValues()[0]);
	}
}
