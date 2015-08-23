package gov.gtas.rule.builder;

import static gov.gtas.util.DateCalendarUtils.formatRuleEngineDate;
import static gov.gtas.util.DateCalendarUtils.parseJsonDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.mappings.DocumentMapping;
import gov.gtas.querybuilder.mappings.FlightMapping;
import gov.gtas.querybuilder.mappings.PassengerMapping;
import gov.gtas.svc.UdrServiceHelper;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleConditionBuilderTest {
	
	private RuleConditionBuilder testTarget;

	@Before
	public void setUp() throws Exception {
		testTarget = new RuleConditionBuilder(UdrServiceHelper.createEngineRuleVariableMap());
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
	public void testStringConditionsOnPassenger() throws ParseException {
		for(OperatorCodeEnum op:
			new OperatorCodeEnum[]{
				OperatorCodeEnum.EQUAL, 
				OperatorCodeEnum.NOT_EQUAL,
				OperatorCodeEnum.BEGINS_WITH,
				OperatorCodeEnum.ENDS_WITH,
				OperatorCodeEnum.CONTAINS,
				OperatorCodeEnum.NOT_BEGINS_WITH,
				OperatorCodeEnum.NOT_ENDS_WITH,
				OperatorCodeEnum.NOT_CONTAINS,
				OperatorCodeEnum.NOT_IN,
				OperatorCodeEnum.IN
				}){
			verifyStringConditionOnPassenger(op);
		}
	}
	private void verifyStringConditionOnPassenger(OperatorCodeEnum op) throws ParseException{
		String[] val = new String[]{"Foo", "Bar"};
		String attr = PassengerMapping.LAST_NAME.getFieldName();
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.PASSENGER,
				PassengerMapping.LAST_NAME,
				op, val, ValueTypesEnum.STRING);
		testTarget.addRuleCondition(cond);
		StringBuilder res = new StringBuilder();
		testTarget.buildConditionsAndApppend(res);
		assertTrue(res.length() > 0);
		
		String result = res.toString().trim();
		String prefix = "$p:Passenger(";
		switch(op){
		case EQUAL:
			assertEquals(prefix+attr+" == \"" + val[0] + "\")", result);
			break;
		case NOT_EQUAL:
			assertEquals(prefix+attr+" != \"" + val[0] + "\")", result);
			break;
		case IN:
			assertEquals(prefix+attr+" in " + createStringValueList(val)+")", result);
			break;
		case NOT_IN:
			assertEquals(prefix+attr+" not in " + createStringValueList(val)+")", result);
			break;
		case BEGINS_WITH:
			assertEquals(prefix + attr + " != null, "
					+attr+" str[startsWith] \"" + val[0] + "\")", result);
			break;
		case NOT_BEGINS_WITH:
			assertEquals(prefix + attr + " != null, "
					+attr+" not matches \"" + val[0] + ".*\")", result);
			break;
		case ENDS_WITH:
			assertEquals(prefix + attr + " != null, "
					+attr+" str[endsWith] \"" + val[0] + "\")", result);
			break;
		case NOT_ENDS_WITH:
			assertEquals(prefix + attr + " != null, "+
					attr+" not matches \".*" + val[0] + "\")", result);
			break;
		case CONTAINS:
			assertEquals(prefix + attr + " != null, "
					+attr+" matches \".*" + val[0] + ".*\")", result);
			break;
		case NOT_CONTAINS:
			assertEquals(prefix + attr + " != null, "
					+attr+" not matches \".*" + val[0] + ".*\")", result);
			break;
			default:
				fail("Unknown String operator");
		}
	}
	private String createStringValueList(String[] values){
		List<String> strList = Arrays.asList(values);
		String res = String.join("\", \"", strList);
		return "(\""+res+"\")";
	}
	@Test
	public void testDateConditionsOnPassenger() throws ParseException {
		for(OperatorCodeEnum op:
			new OperatorCodeEnum[]{
				OperatorCodeEnum.EQUAL, 
				OperatorCodeEnum.NOT_EQUAL,
				OperatorCodeEnum.BETWEEN,
				OperatorCodeEnum.NOT_BETWEEN,
				OperatorCodeEnum.GREATER,
				OperatorCodeEnum.GREATER_OR_EQUAL,
				OperatorCodeEnum.LESS,
				OperatorCodeEnum.LESS_OR_EQUAL,
				OperatorCodeEnum.NOT_IN,
				OperatorCodeEnum.IN
				}){
			verifyDateConditionOnPassenger(op);
		}
	}
	private void verifyDateConditionOnPassenger(OperatorCodeEnum op) throws ParseException{
		String[] val = new String[]{"2011-05-24", "2015-01-25"};
		String attr = PassengerMapping.DOB.getFieldName();
		QueryTerm cond = RuleBuilderTestUtils.createQueryTerm(EntityEnum.PASSENGER,
				PassengerMapping.DOB,
				op, val, ValueTypesEnum.DATE);
		testTarget.addRuleCondition(cond);
		StringBuilder res = new StringBuilder();
		testTarget.buildConditionsAndApppend(res);
		assertTrue(res.length() > 0);
		
		String result = res.toString().trim();
		String prefix = "$p:Passenger(";
		switch(op){
		case EQUAL:
			assertEquals(prefix+attr+" == \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\")", result);
			break;
		case NOT_EQUAL:
			assertEquals(prefix+attr+" != \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\")", result);
			break;
		case IN:
			assertEquals(prefix+attr+" in " + createDateValueList(val)+")", result);
			break;
		case NOT_IN:
			assertEquals(prefix+attr+" not in " + createDateValueList(val)+")", result);
			break;
		case GREATER:
			assertEquals(prefix 
					+attr+" > \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\")", result);
			break;
		case GREATER_OR_EQUAL:
			assertEquals(prefix
					+attr+" >= \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\")", result);
			break;
		case LESS:
			assertEquals(prefix
					+attr+" < \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\")", result);
			break;
		case LESS_OR_EQUAL:
			assertEquals(prefix
					+attr+" <= \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\")", result);
			break;
		case BETWEEN:
			assertEquals(prefix
					+attr+" >= \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\", "
					+attr+ " <= \""+ formatRuleEngineDate(parseJsonDate(val[1])) +"\")", result);
			break;
		case NOT_BETWEEN:
			assertEquals(prefix + "("
					+attr+" < \"" + formatRuleEngineDate(parseJsonDate(val[0])) + "\" || "
					+attr+ " > \""+ formatRuleEngineDate(parseJsonDate(val[1])) +"\"))", result);
			break;
			default:
				fail("Unknown String operator");
		}
	}
	private String createDateValueList(String[] values) throws ParseException{
		for(int i = 0; i < values.length; ++i){
			values[i] = formatRuleEngineDate(parseJsonDate(values[i]));
		}
		List<String> strList = Arrays.asList(values);
		String res = String.join("\", \"", strList);
		return "(\""+res+"\")";
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
				"$d:Document("+DocumentMapping.ISSUANCE_COUNTRY.getFieldName()+" != \"US\", "
		            +DocumentMapping.ISSUANCE_DATE.getFieldName()+" >= \"01-Jan-2010\")\n"
   				+"$p:Passenger("
					+PassengerMapping.DOB.getFieldName()+" >= \"01-Jan-1990\", "
					+PassengerMapping.DOB.getFieldName()+" <= \"31-Dec-1998\", "
					+PassengerMapping.LAST_NAME.getFieldName()+" == \"Jones\", "
					+"id == $d.passenger.id)\n"

		        + "$f:Flight("+FlightMapping.AIRPORT_DESTINATION.getFieldName()+" == \"DBY\", "
		           +FlightMapping.FLIGHT_NUMBER.getFieldName()+" == 2231)\n"
		        +"Passenger(id == $p.id) from $f.passengers",
		result.toString().trim());
	}
}
