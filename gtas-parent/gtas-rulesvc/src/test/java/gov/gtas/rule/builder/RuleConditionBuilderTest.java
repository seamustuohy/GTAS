package gov.gtas.rule.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.mappings.DocumentMapping;
import gov.gtas.querybuilder.mappings.FlightMapping;
import gov.gtas.querybuilder.mappings.TravelerMapping;

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
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.TRAVELER,
				TravelerMapping.DOB,
				OperatorCodeEnum.BETWEEN, new String[]{"1990-01-01","1998-12-31"}, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$t:Traveler("+TravelerMapping.DOB.getFieldName()+" >= \"01-Jan-1990\", "
		+TravelerMapping.DOB.getFieldName()+" <= \"31-Dec-1998\")", result.toString().trim());
	}

	@Test
	public void testSingleConditionFlight2() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.FLIGHT,
				FlightMapping.FLIGHT_NUMBER,
				OperatorCodeEnum.EQUAL, "12345", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		System.out.println(result);
		assertEquals("$f:Flight("+FlightMapping.FLIGHT_NUMBER.getFieldName()+" == \"12345\")\n"
				+"$t:Traveler() from $f.passengers",
				result.toString().trim());
	}

	@Test
	public void testSingleConditionFlight() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.FLIGHT,
				FlightMapping.AIRPORT_DESTINATION,
				OperatorCodeEnum.IN, new String[]{"DBY","XYZ","PQR"}, ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$f:Flight("+FlightMapping.AIRPORT_DESTINATION.getFieldName()+" in (\"DBY\",\"XYZ\",\"PQR\"))\n"
				+"$t:Traveler() from $f.passengers",
				result.toString().trim());
	}
	@Test
	public void testSingleConditionDocument() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY.getFieldName()+" != \"US\")\n"
				+"$t:Traveler(id == $d.traveler.id)", 
				result.toString().trim());
	}
	@Test
	public void testMultipleConditionsDocument() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_DATE,
				OperatorCodeEnum.GREATER_OR_EQUAL, "2010-01-01", ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		System.out.println(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY.getFieldName()+" != \"US\", "
		+DocumentMapping.ISSUANCE_DATE.getFieldName()+" >= \"01-Jan-2010\")\n"
		+"$t:Traveler(id == $d.traveler.id)", 
		result.toString().trim());
	}
	@Test
	public void testMultipleConditionsDocument2() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.DOCUMENT_TYPE,
				OperatorCodeEnum.EQUAL, "P", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY.getFieldName()+" != \"US\")\n"
		+"Passport(id == $d.id)\n"
		+"$t:Traveler(id == $d.traveler.id)", 
		result.toString().trim());
	}
	@Test
	public void testMultipleConditionsPersonFlightDocument() throws ParseException {
		RuleCond cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_DATE,
				OperatorCodeEnum.GREATER_OR_EQUAL, "2010-01-01", ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.TRAVELER,
				TravelerMapping.DOB,
				OperatorCodeEnum.BETWEEN, new String[]{"1990-01-01","1998-12-31"}, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.FLIGHT,
				FlightMapping.AIRPORT_DESTINATION,
				OperatorCodeEnum.EQUAL, "DBY", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.FLIGHT,
				FlightMapping.FLIGHT_NUMBER,
				OperatorCodeEnum.EQUAL, "2231", ValueTypesEnum.INTEGER);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createRuleCond(EntityEnum.TRAVELER,
				TravelerMapping.LAST_NAME,
				OperatorCodeEnum.EQUAL, "Jones", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
				
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		System.out.println(result.toString());
		assertEquals(
   				"$t:Traveler("
					+TravelerMapping.DOB.getFieldName()+" >= \"01-Jan-1990\", "
					+TravelerMapping.DOB.getFieldName()+" <= \"31-Dec-1998\", "
					+TravelerMapping.LAST_NAME.getFieldName()+" == \"Jones\")\n"
				+"$d:Document("+DocumentMapping.ISSUANCE_OR_CITIZENSHIP_COUNTRY.getFieldName()+" != \"US\", "
		            +DocumentMapping.ISSUANCE_DATE.getFieldName()+" >= \"01-Jan-2010\")\n"
		        + "Document(id == $d.id, traveler.id == $t.id)\n"
		        + "$f:Flight("+FlightMapping.AIRPORT_DESTINATION.getFieldName()+" == \"DBY\", "
		           +FlightMapping.FLIGHT_NUMBER.getFieldName()+" == 2231)\n"
		        +"Traveler(id == $t.id) from $f.passengers",
		result.toString().trim());
	}
}
