/**
 * 
 */
package gov.gtas.querybuilder.service;

import static org.junit.Assert.assertEquals;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.model.UserQuery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author GTAS5
 *
 */
@Ignore
 
public class QueryBuilderServiceTest {

	QueryBuilderService service;
	 
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		service = new QueryBuilderService();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		service = null;
	}

	
//	@Test()
	public void test() {
		UserQuery query = new UserQuery();
		User user = new User();
		user.setUserId("ladebiyi");
		
		query.setCreatedBy(user);
		query.setCreatedDt(new Date());
		query.setTitle("Test Query");
		query.setDescription("Test description");
//		query.setQuery(buildSimpleQuery());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("Query: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(query));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test()
	public void testGetQueryForFlightsWithSimpleQuery() throws Exception {
		final String expectedQuery = "";
//		final String expectedQuery = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
//				" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias() +
//				QueryBuilderUtil.getJoinCondition(EntityEnum.PAX) + " " + Constants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);

		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.FLIGHT);
		System.out.println("actualQuery: " + actualQuery);
//		assertEquals(expectedQuery, actualQuery);
	}
	
//	@Test()
	public void testGetQueryForPassengersWithSimpleQuery() throws Exception {
		final String expectedQuery = Constants.SELECT_DISTINCT + " " + EntityEnum.TRAVELER.getAlias() + 
				" " + Constants.FROM + " " + EntityEnum.TRAVELER.getEntityName() + " " + EntityEnum.TRAVELER.getAlias() +
				" " + Constants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);
		
		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.TRAVELER);
		
		assertEquals(expectedQuery, actualQuery);
		
	}
	
//	@Test()
	public void testGetQueryForFlightsWithSimpleDateQuery() throws Exception {
		final String expectedQuery = "";
//		final String expectedQuery = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
//				" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias() +
//				QueryBuilderUtil.getJoinCondition(EntityEnum.PAX) + " " + Constants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleDateQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);

		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.FLIGHT);
		
		System.out.println("actual Query: " + actualQuery);
		
//		assertEquals(expectedQuery, actualQuery);
	}
	
//	@Test()
	public void testGetQueryForFlightsWithSimpleIsNullQuery() throws Exception {
		final String expectedQuery = "";
//		final String expectedQuery = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
//				" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias() +
//				QueryBuilderUtil.getJoinCondition(EntityEnum.PAX) + " " + Constants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleIsNullQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);

		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.FLIGHT);
		
		System.out.println("actual Query: " + actualQuery);
		
//		assertEquals(expectedQuery, actualQuery);
	}
	
//	@Test()
	public void testGetQueryForFlightsWithSimpleContainsQuery() throws Exception {
		final String expectedQuery = "";
//		final String expectedQuery = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
//				" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias() +
//				QueryBuilderUtil.getJoinCondition(EntityEnum.PAX) + " " + Constants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleContainsQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);

		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.FLIGHT);
		
		System.out.println("actual Query: " + actualQuery);
		
//		assertEquals(expectedQuery, actualQuery);
	}
	
	private QueryObject buildSimpleQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("equal");
		rule.setType("string");
		rule.setValue("DAVID");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleDateQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Flight");
		rule.setField("eta");
		rule.setOperator("equal");
		rule.setType("date");
		rule.setValue("05/11/2014");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleIsNullQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("middleName");
		rule.setOperator("is_null");
		rule.setType("boolean");
		rule.setValue("");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleContainsQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("contains");
		rule.setType("string");
		rule.setValue("avi");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildNestedQuery() {
		QueryObject query = new QueryObject();
		
		return query;
	}
}
