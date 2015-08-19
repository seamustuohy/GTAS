package gov.gtas.querybuilder.service;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.config.QueryBuilderAppConfig;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.model.QueryPassengerResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *	Query Builder Service Integration Test 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonServicesConfig.class, QueryBuilderAppConfig.class})
public class QueryBuilderServiceIT {

	@PersistenceContext 
 	private EntityManager entityManager;
	@Autowired
	private QueryBuilderService queryService;
	private static final String TITLE = "Sample Query";
	private static final String DESCRIPTION = "A simple query";
	private static final String USER_ID = "ladebiyi";
	
	private QueryObject query;
	
	@Before
	public void setUp() throws Exception {
		query = buildSimpleBetweenQuery();
	}

	@After
	public void tearDown() throws Exception {
		query = null;
		
		// delete all records from the user_query table
		// thereby creating isolated integration test
//		deleteAllRecords();
	}
	

	@Transactional
	private void deleteAllRecords() {
		// delete all records from the user_query table
		Query deleteQuery = entityManager.createQuery("delete from UserQuery");
		deleteQuery.executeUpdate();
	}

//	@Test
//	@Transactional
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
//		QueryRequest request = new QueryRequest();
//		
//		request.setTitle(TITLE);
//		request.setDescription(DESCRIPTION);
//		request.setQuery(query);
//		request.setUserId(USER_ID);
//		
//		UserQuery result = queryService.saveQuery(request);
//		assertNotNull(result.getId());
	}
	
//	@Test(expected = QueryAlreadyExistsException.class)
//	@Transactional
	public void testSaveDuplicateQuery() throws QueryAlreadyExistsException, InvalidQueryException {
//		QueryRequest request = new QueryRequest();
//		
//		request.setTitle(TITLE + "1");
//		request.setDescription(DESCRIPTION);
//		request.setQuery(buildSimpleBetweenQuery());
//		request.setUserId(USER_ID);
//		
//		UserQuery result = queryService.saveQuery(request);
//		queryService.saveQuery(request);
//		assertNotNull(result.getId());
//		
//		queryService.saveQuery(request);
	}
	
//	@Test
	public void testSaveInvalidQuery() {
		
	}
	
	//----------------------------------------
	// Flight Queries
	//----------------------------------------
//	@Test
	public void testRunQueryAgainstFlights() throws InvalidQueryException  {
	}

//	@Test
	public void testSimpleDateQueryAgainstFlights() throws InvalidQueryException {
	}
	
//	@Test
	public void testSimpleIsNullQueryAgainstFlights() throws InvalidQueryException {
	}
	
//	@Test
	public void testSimpleContainsQueryAgainstFlights() throws InvalidQueryException {
	}
	
//	@Test
	public void testSimpleBetweenQuery() throws InvalidQueryException {
	}
	
//	@Test
	public void testComplexQueryAgainstFlights() throws JsonProcessingException {
	}
	//-------------------------------
	// Passenger Queries
	//-------------------------------
//	@Test
	public void testRunQueryAgainstPassengers() throws InvalidQueryException {
//		
	}
	
//	@Test
	public void testSimpleIsNullQueryAgainstPassengers() throws InvalidQueryException {
	}
	
//	@Test
	public void testSimpleContainsQueryAgainstPassengers() throws InvalidQueryException {
	}
	
//	@Test
	public void testSimpleBetweenQueryAgainstPassengers() throws InvalidQueryException {
	}
	
	
//	@Test
	public void display() throws JsonProcessingException {
	}
	
//	@Test
	public void addDuplicateQuery() throws JsonProcessingException, QueryAlreadyExistsException, InterruptedException {
	}
	
//	@Test 
	public void testEditQuery() throws JsonProcessingException, QueryAlreadyExistsException {
	}
	
//	@Test
	public void testListQueryByUser() {
	}

//	@Test
	public void testDeleteQuery() {
	}
	
	//---------------------------------------
	// Build Query Objects
	//---------------------------------------
	private QueryObject buildSimpleBetweenQuery() {
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		QueryObject query = new QueryObject();
		List<String> values = new ArrayList<>();
		
		values.add("20");
		values.add("40");
		
		rule.setEntity("Passenger");
		rule.setField("age");
		rule.setOperator("between");
		rule.setType("integer");
		rule.setValue(values.toArray(new String[values.size()]));
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
}
