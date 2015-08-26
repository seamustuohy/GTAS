package gov.gtas.querybuilder.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.config.QueryBuilderAppConfig;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.model.IUserQueryResult;
import gov.gtas.querybuilder.model.QueryRequest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
	private static final String TITLE = "Integration Test";
	private static final String UPDATED_TITLE = "Updated Int. Test";
	private static final String DESCRIPTION = "A simple query created during integration test";
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
		// after each test method thereby creating 
		// isolated integration test
		deleteAllRecords();
	}
	

	@Transactional
	private void deleteAllRecords() {
		// delete all records from the user_query table
		Query deleteQuery = entityManager.createQuery("delete from UserQuery");
		deleteQuery.executeUpdate();
	}

	@Test
	@Transactional
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		IUserQueryResult result = queryService.saveQuery(request);
		assertNotNull(result.getId());
	}
	
	@Test(expected = QueryAlreadyExistsException.class)
	@Transactional
	public void testSaveDuplicateQuery() throws QueryAlreadyExistsException, InvalidQueryException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		// create a user query
		IUserQueryResult result = queryService.saveQuery(request);
		assertNotNull(result.getId());
		
		// try to create a duplicate query
		// this call should fail and throw exception
		queryService.saveQuery(request);
	}
	
	@Test(expected = InvalidQueryException.class)
	@Transactional
	public void testSaveInvalidQuery() throws QueryAlreadyExistsException, InvalidQueryException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(null);
		request.setUserId(USER_ID);
		
		// create a user query
		queryService.saveQuery(request);
	}
	
	@Test
	@Transactional
	public void testMaxTitle() {
		
	}
	
	@Test
	@Transactional
	public void TestMaxDescription() {
		
	}
	
	@Test
	@Transactional
	public void testEditQuery() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		// create a new query
		IUserQueryResult result = queryService.saveQuery(request);
		
		// update the query
		request.setId(result.getId());
		request.setTitle(UPDATED_TITLE);
		IUserQueryResult updatedResult = queryService.editQuery(request);
		
		assertEquals(result.getId(), updatedResult.getId());
		assertEquals(UPDATED_TITLE, updatedResult.getTitle());
	}
	
	@Test(expected = InvalidQueryException.class)
	@Transactional
	public void testEditInvalidQuery() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		// create a new query
		IUserQueryResult result = queryService.saveQuery(request);
		
		// update the query
		request.setId(result.getId());
		request.setTitle(UPDATED_TITLE);
		request.setQuery(null);
		
		// try updating the query with an invalid user query
		queryService.editQuery(request);
	}
	
	@Test(expected = QueryDoesNotExistException.class)
	@Transactional
	public void testEditDoesNotExistQuery() throws QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		QueryRequest request = new QueryRequest();
		
		request.setId(1);
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		queryService.editQuery(request);
	}
	
	@Test(expected = QueryAlreadyExistsException.class)
	@Transactional
	public void testEditQueryAlreadyExists() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		// create a new query
		queryService.saveQuery(request);
		
		// create another query
		request.setTitle(TITLE + "2");
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		IUserQueryResult secondResult = queryService.saveQuery(request);
		
		// try to update the second query using
		// the same title in the first query
		// thereby trying to create a duplicate query
		// for the same user, which is not allowed
		request.setTitle(TITLE);
		request.setId(secondResult.getId());
		queryService.editQuery(request);
	}
	
	@Test
	@Transactional
	public void testListQueryByUser() throws QueryAlreadyExistsException, InvalidQueryException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(query);
		request.setUserId(USER_ID);
		
		// create a new query
		queryService.saveQuery(request);
		
		List<IUserQueryResult> result = queryService.listQueryByUser(USER_ID);
		
		assertNotNull(result);
		assertEquals(1, result.size());
	}

//	@Test
	public void testDeleteQuery() {
		
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
