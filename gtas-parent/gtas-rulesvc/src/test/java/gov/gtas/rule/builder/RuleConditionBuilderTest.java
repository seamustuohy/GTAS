package gov.gtas.rule.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.mappings.DocumentMapping;
import gov.gtas.querybuilder.mappings.FlightMapping;
import gov.gtas.querybuilder.mappings.PassengerMapping;

import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleConditionBuilderTest {
	private static final String PASSENGER_VARIABLE_NAME="$p";
	private static final String DOCUMENT_VARIABLE_NAME="$d";
	private static final String FLIGHT_VARIABLE_NAME="$f";
	
	private RuleConditionBuilder testTarget;

	@Before
	public void setUp() throws Exception {
		testTarget = new RuleConditionBuilder(PASSENGER_VARIABLE_NAME, FLIGHT_VARIABLE_NAME, DOCUMENT_VARIABLE_NAME);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSingleConditionPassenger() throws ParseException {
		/*
		 * just one passenger condition.
		 * also test BETWEEN operator.
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.PASSENGER,
				PassengerMapping.DOB,
				OperatorCodeEnum.BETWEEN, new String[]{"1990-01-01","1998-12-31"}, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$p:Passenger("+PassengerMapping.DOB.getFieldName()+" >= \"01-Jan-1990\", "
		+PassengerMapping.DOB.getFieldName()+" <= \"31-Dec-1998\")", result.toString().trim());
	}
	@Test
	public void testSingleConditionFlight() throws ParseException {
		/*
		 * just one flight.
		 * also test IN operator.
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.FLIGHT,
				FlightMapping.AIRPORT_DESTINATION,
				OperatorCodeEnum.IN, new String[]{"DBY","XYZ","PQR"}, ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$f:Flight("+FlightMapping.AIRPORT_DESTINATION.getFieldName()+" in (\"DBY\", \"XYZ\", \"PQR\"))\n"
				+"$p:Passenger() from $f.passengers",
				result.toString().trim());
	}


	@Test
	public void testSingleConditionDocument() throws ParseException {
		/*
		 * test just one document condition.
		 * also test NOT_EQUAL operator.
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_COUNTRY.getFieldName()+" != \"US\")\n"
				+"$p:Passenger(id == $d.passenger.id)", 
				result.toString().trim());
	}
	
	@Test
	public void testConditionEqual() throws ParseException {
		/*
		 * test EQUAL operator
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.FLIGHT,
				FlightMapping.FLIGHT_NUMBER,
				OperatorCodeEnum.EQUAL, "12345", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$f:Flight("+FlightMapping.FLIGHT_NUMBER.getFieldName()+" == \"12345\")\n"
				+"$p:Passenger() from $f.passengers",
				result.toString().trim());
	}
	
	@Test
	public void testMultipleConditionsDocument() throws ParseException {
		/*
		 * test multiple document conditions.
		 * also test GREATER_EQUAL and NOT_EQUAL.
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_DATE,
				OperatorCodeEnum.GREATER_OR_EQUAL, "2010-01-01", ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		System.out.println(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_COUNTRY.getFieldName()+" != \"US\", "
		+DocumentMapping.ISSUANCE_DATE.getFieldName()+" >= \"01-Jan-2010\")\n"
		+"$p:Passenger(id == $d.passenger.id)", 
		result.toString().trim());
	}
	
	// TODO: Amit review
	@Test
	public void testDocumentWithTypeEquality() throws ParseException {
		/*
		 * one document condition and one type equality
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.DOCUMENT_TYPE,
				OperatorCodeEnum.EQUAL, "P", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_COUNTRY.getFieldName()+" != \"US\", "
						+ DocumentMapping.DOCUMENT_TYPE.getFieldName()+" == \"P\")\n"
						+"$p:"+ EntityEnum.PASSENGER.getEntityName()+"(id == $d."+DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()+")",
		result.toString().trim());
	}
	@Test
	public void testDocumentTypeEqualityOnly() throws ParseException {
		/*
		 * one document condition and one type equality
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.DOCUMENT_TYPE,
				OperatorCodeEnum.EQUAL, "P", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("	+ DocumentMapping.DOCUMENT_TYPE.getFieldName()+" == \"P\")\n"
				+"$p:"+ EntityEnum.PASSENGER.getEntityName()+"(id == $d."+DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()+")",
		result.toString().trim());
	}
	@Test
	public void testDocumentWithTypeInEquality() throws ParseException {
		/*
		 * one document condition and one type equality
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.DOCUMENT_TYPE,
				OperatorCodeEnum.NOT_EQUAL, "P", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		System.out.println(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("+DocumentMapping.ISSUANCE_COUNTRY.getFieldName()+" != \"US\", "
			+ DocumentMapping.DOCUMENT_TYPE.getFieldName()+" != \"P\")\n"
			+"$p:"+ EntityEnum.PASSENGER.getEntityName()+"(id == $d."+DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()+")",
		result.toString().trim());
	}
	@Test
	public void testDocumentTypeInEqualityOnly() throws ParseException {
		/*
		 * one document condition and one type inequality
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.DOCUMENT_TYPE,
				OperatorCodeEnum.NOT_EQUAL, "P", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		assertEquals("$d:Document("	+ DocumentMapping.DOCUMENT_TYPE.getFieldName()+" != \"P\")\n"
				+"$p:"+ EntityEnum.PASSENGER.getEntityName()+"(id == $d."+DocumentMapping.DOCUMENT_OWNER_ID.getFieldName()+")",
		result.toString().trim());
	}
	@Test
	public void testMultipleConditionsPersonFlightDocument() throws ParseException {
		/*
		 * conditions for passenger, document and Flight.
		 */
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_COUNTRY,
				OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.DOCUMENT,
				DocumentMapping.ISSUANCE_DATE,
				OperatorCodeEnum.GREATER_OR_EQUAL, "2010-01-01", ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.PASSENGER,
				PassengerMapping.DOB,
				OperatorCodeEnum.BETWEEN, new String[]{"1990-01-01","1998-12-31"}, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.FLIGHT,
				FlightMapping.AIRPORT_DESTINATION,
				OperatorCodeEnum.EQUAL, "DBY", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.FLIGHT,
				FlightMapping.FLIGHT_NUMBER,
				OperatorCodeEnum.EQUAL, "2231", ValueTypesEnum.INTEGER);
		testTarget.addRuleCondition(cond);

		cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.PASSENGER,
				PassengerMapping.LAST_NAME,
				OperatorCodeEnum.EQUAL, "Jones", ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
				
		StringBuilder result = new StringBuilder();
		testTarget.buildConditionsAndApppend(result);
		assertTrue(result.length() > 0);
		System.out.println(result.toString());
		assertEquals(
   				"$p:Passenger("
					+PassengerMapping.DOB.getFieldName()+" >= \"01-Jan-1990\", "
					+PassengerMapping.DOB.getFieldName()+" <= \"31-Dec-1998\", "
					+PassengerMapping.LAST_NAME.getFieldName()+" == \"Jones\")\n"
				+"$d:Document("+DocumentMapping.ISSUANCE_COUNTRY.getFieldName()+" != \"US\", "
		            +DocumentMapping.ISSUANCE_DATE.getFieldName()+" >= \"01-Jan-2010\")\n"
		        + "Document(id == $d.id, passenger.id == $p.id)\n"
		        + "$f:Flight("+FlightMapping.AIRPORT_DESTINATION.getFieldName()+" == \"DBY\", "
		           +FlightMapping.FLIGHT_NUMBER.getFieldName()+" == 2231)\n"
		        +"Passenger(id == $p.id) from $f.passengers",
		result.toString().trim());
	}
}
