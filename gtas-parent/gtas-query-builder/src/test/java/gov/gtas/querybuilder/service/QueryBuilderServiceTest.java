/**
 * 
 */
package gov.gtas.querybuilder.service;

import static org.junit.Assert.assertEquals;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.util.EntityEnum;
import gov.gtas.querybuilder.util.QueryBuilderConstants;
import gov.gtas.querybuilder.util.QueryBuilderUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author GTAS5
 *
 */
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

	
	@Test()
	public void testGetQueryForFlightsWithSimpleQuery() throws Exception {
		final String expectedQuery = QueryBuilderConstants.SELECT_DISTINCT + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.FLIGHT) + 
				" " + QueryBuilderConstants.FROM + " " + EntityEnum.FLIGHT.getName() + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.FLIGHT) +
				QueryBuilderUtil.getJoinCondition(EntityEnum.PASSENGER) + " " + QueryBuilderConstants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);

		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.FLIGHT);
		
		assertEquals(expectedQuery, actualQuery);
	}
	
	@Test()
	public void testGetQueryForPassengersWithSimpleQuery() throws Exception {
		final String expectedQuery = QueryBuilderConstants.SELECT_DISTINCT + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.PASSENGER) + 
				" " + QueryBuilderConstants.FROM + " " + EntityEnum.PASSENGER.getName() + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.PASSENGER) +
				" " + QueryBuilderConstants.WHERE + " p.firstName = 'DAVID'";
		
		QueryObject query = buildSimpleQuery();
		
		Method privateGetQueryMethod = QueryBuilderService.class.getDeclaredMethod("getQuery", QueryObject.class, EntityEnum.class);
		privateGetQueryMethod.setAccessible(true);
		
		String actualQuery = (String) privateGetQueryMethod.invoke(service, query, EntityEnum.PASSENGER);
		
		assertEquals(expectedQuery, actualQuery);
		
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
	
	private QueryObject QueryObjectNestedQuery() {
		QueryObject query = new QueryObject();
		
		return query;
	}
}
