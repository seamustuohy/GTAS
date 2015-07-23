package gov.gtas.rule.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;

import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleConditionBuilderTest {
	private RuleConditionBuilder testTarget;

	@Before
	public void setUp() throws Exception {
		testTarget = new RuleConditionBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSingleConditionPassenger() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_DOB,
				OperatorCodeEnum.BETWEEN, new String[]{"1990-01-01","1998-12-31"}, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$p:Pax("+EntityAttributeConstants.PAX_ATTTR_DOB+" >= \"01-Jan-1990\", "
		+EntityAttributeConstants.PAX_ATTTR_DOB+" <= \"31-Dec-1998\")", result.toString().trim());
	}

	@Test
	public void testSingleConditionFlight2() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Flight,
				EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER,
				OperatorCodeEnum.EQUAL, "12345", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		System.out.println(result);
		assertEquals("$f:Flight("+EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER+" == \"12345\")\n"
				+"$p:Pax() from $f.passengers",
				result.toString().trim());
	}

	@Test
	public void testSingleConditionFlight() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Flight,
				EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME,
				OperatorCodeEnum.IN, new String[]{"DBY","XYZ","PQR"}, ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$f:Flight("+EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME+" in (\"DBY\",\"XYZ\",\"PQR\"))\n"
				+"$p:Pax() from $f.passengers",
				result.toString().trim());
	}
	@Test
	public void testSingleConditionDocument() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Document,
				EntityAttributeConstants.DOCUMENT_ATTR_ISO2,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+EntityAttributeConstants.DOCUMENT_ATTR_ISO2+" != \"US\")\n"
				+"$p:Pax(id == $d.traveler.id)", 
				result.toString().trim());
	}
	@Test
	public void testMultipleConditionsDocument() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Document,
				EntityAttributeConstants.DOCUMENT_ATTR_ISO2,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Document,
				EntityAttributeConstants.DOCUMENT_ATTR_ISSUANCE_DATE,
				OperatorCodeEnum.GREATER_OR_EQUAL, "2010-01-01", ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+EntityAttributeConstants.DOCUMENT_ATTR_ISO2+" != \"US\", "
		+EntityAttributeConstants.DOCUMENT_ATTR_ISSUANCE_DATE+" >= \"01-Jan-2010\")\n"
		+"$p:Pax(id == $d.traveler.id)", 
		result.toString().trim());
	}
	@Test
	public void testMultipleConditionsPersonFlightDocument() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Document,
				EntityAttributeConstants.DOCUMENT_ATTR_ISO2,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Document,
				EntityAttributeConstants.DOCUMENT_ATTR_ISSUANCE_DATE,
				OperatorCodeEnum.GREATER_OR_EQUAL, "2010-01-01", ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_DOB,
				OperatorCodeEnum.BETWEEN, new String[]{"1990-01-01","1998-12-31"}, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Flight,
				EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME,
				OperatorCodeEnum.EQUAL, "DBY", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Flight,
				EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER,
				OperatorCodeEnum.EQUAL, "2231", ValueTypesEnum.INTEGER);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createRuleCondition(EntityLookupEnum.Pax,
				EntityAttributeConstants.PAX_ATTTR_LAST_NAME,
				OperatorCodeEnum.EQUAL, "Jones", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
				
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		System.out.println(result.toString());
		assertEquals(
				"$d:Document("+EntityAttributeConstants.DOCUMENT_ATTR_ISO2+" != \"US\", "
		            +EntityAttributeConstants.DOCUMENT_ATTR_ISSUANCE_DATE+" >= \"01-Jan-2010\")\n"
		        + "$f:Flight("+EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME+" == \"DBY\", "
		           +EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER+" == 2231)\n"
		           				+"$p:Pax("
						+EntityAttributeConstants.PAX_ATTTR_DOB+" >= \"01-Jan-1990\", "
						+EntityAttributeConstants.PAX_ATTTR_DOB+" <= \"31-Dec-1998\", "
		        +EntityAttributeConstants.PAX_ATTTR_LAST_NAME+" == \"Jones\", "
		           +"id == $d.traveler.id)\n"
		        +"Pax(id == $p.id) from $f.passengers",
		result.toString().trim());
	}
}
