package gov.gtas.querybuilder.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.IQueryResult;
import gov.gtas.querybuilder.model.IUserQueryResult;
import gov.gtas.querybuilder.model.QueryPassengerResult;
import gov.gtas.querybuilder.model.UserQueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;
import gov.gtas.vo.passenger.FlightVo;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit Test class for the Query Builder Service
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class QueryBuilderServiceTest {

	@InjectMocks
	private QueryBuilderService queryService;
	@Mock
	private QueryBuilderRepository queryRepository;
	
	private static final String userId = "testUser";
	private static final int queryId = 1;
	private static UserQueryRequest request;
	private static QueryAlreadyExistsRepositoryException queryExistsRepoException;
	private static QueryDoesNotExistRepositoryException queryNotExistRepoException;
	private static InvalidQueryRepositoryException invalidQueryRepoException;
	
	private static QueryObject queryObject;
	
	@BeforeClass
	public static void setUp() throws Exception {
		queryObject = buildSimpleBetweenQuery();
		request = new UserQueryRequest();
		queryExistsRepoException = new QueryAlreadyExistsRepositoryException(Constants.QUERY_EXISTS_ERROR_MSG, new UserQuery());
		queryNotExistRepoException = new QueryDoesNotExistRepositoryException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, new UserQuery());
		invalidQueryRepoException = new InvalidQueryRepositoryException();
	}
	
	@Test
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, 
		QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException {
		UserQuery expected = new UserQuery();
		expected.setId(1);
		
		when(queryRepository.saveQuery(any(UserQuery.class))).thenReturn(expected);
		IUserQueryResult actual = queryService.saveQuery(userId,request);
		
		assertEquals(expected.getId().intValue(), actual.getId());
	}

	@Test(expected = QueryAlreadyExistsException.class)
	public void testSaveDuplicateQuery() throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, 
		QueryAlreadyExistsException, InvalidQueryException {
		when(queryRepository.saveQuery(any(UserQuery.class))).thenThrow(queryExistsRepoException);
		
		queryService.saveQuery(userId,request);
	}
		
	@Test(expected = InvalidQueryException.class)
	public void testSaveInvalidQuery() throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, 
		QueryAlreadyExistsException, InvalidQueryException {
		when(queryRepository.saveQuery(any(UserQuery.class))).thenThrow(invalidQueryRepoException);
		
		queryService.saveQuery(userId,request);
	}
	
	@Test
	public void testEditQuery() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		UserQuery expected = new UserQuery();
		expected.setId(1);
		
		when(queryRepository.editQuery(any(UserQuery.class))).thenReturn(expected);
		IUserQueryResult actual = queryService.editQuery(userId,request);
		
		assertEquals(expected.getId().intValue(), actual.getId());
	}
	
	@Test(expected = QueryAlreadyExistsException.class)
	public void testEditQueryDuplicateTitle() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(queryExistsRepoException);
		
		queryService.editQuery(userId,request);
	}
	
	@Test(expected = QueryDoesNotExistException.class)
	public void testEditQueryDoesNotExist() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(queryNotExistRepoException);
		
		queryService.editQuery(userId,request);
	}
	
	@Test(expected = InvalidQueryException.class)
	public void testEditQueryInvalidQuery() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(invalidQueryRepoException);
		
		queryService.editQuery(userId,request);
	}

	@Test
	public void testListQueryByUser() throws InvalidQueryException {
		List<UserQuery> expected = new ArrayList<>();
		UserQuery userQuery = new UserQuery();
		userQuery.setId(1);
		expected.add(userQuery);
		
		when(queryRepository.listQueryByUser(userId)).thenReturn(expected);
		List<IUserQueryResult> actual = queryService.listQueryByUser(userId);
		
		assertEquals(expected.get(0).getId().intValue(), actual.get(0).getId());
	}
	
	@Test(expected = QueryDoesNotExistException.class)
	public void testDeleteQueryDoesNotExist() throws QueryDoesNotExistRepositoryException, QueryDoesNotExistException {
		doThrow(queryNotExistRepoException).when(queryRepository).deleteQuery(userId, queryId);

		queryService.deleteQuery(userId, queryId);
	}
	
	@Test
	public void testRunFlightQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		/*List<Flight> expected = new ArrayList<>();
		Flight flight = new Flight();
		flight.setId(1L);
		expected.add(flight);
		
		when(queryRepository.getFlightsByDynamicQuery(queryObject)).thenReturn(expected);
		List<FlightVo> result = queryService.runFlightQuery(queryObject);
		
		assertEquals(expected.get(0).getId(), result.get(0).getId());*/
	}
	
//	@Test(expected = InvalidQueryException.class)
	public void testRunFlightQueryInvalidQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		/*when(queryRepository.getFlightsByDynamicQuery(queryObject)).thenThrow(invalidQueryRepoException);
		
		queryService.runFlightQuery(queryObject);*/
	}
	
	@Test
	public void testRunPassengerQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		/*List<Object[]> expected = new ArrayList<>();
		Passenger passenger = new Passenger();
		passenger.setId(1L);
		Flight flight = new Flight();
		flight.setFlightNumber("123");
		Document doc = new Document();
		doc.setDocumentNumber("ABC");;
		expected.add(new Object[]{passenger, flight, doc});
		
		Passenger expectedPassenger = (Passenger) expected.get(0)[0];
		Flight expectedFlight = (Flight) expected.get(0)[1];
		Document expectedDoc = (Document) expected.get(0)[2];
		
		when(queryRepository.getPassengersByDynamicQuery(queryObject)).thenReturn(expected);
		List<IQueryResult> result = queryService.runPassengerQuery(queryObject);
		QueryPassengerResult actual = (QueryPassengerResult) result.get(0);
		
		assertEquals(expectedPassenger.getId(), actual.getId());
		assertEquals(expectedFlight.getFlightNumber(), actual.getFlightNumber());
		assertEquals(expectedDoc.getDocumentNumber(), actual.getDocumentNumber());*/
	}

//	@Test(expected = InvalidQueryException.class)
	public void testRunPassengerQueryInvalidQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		/*when(queryRepository.getPassengersByDynamicQuery(queryObject)).thenThrow(invalidQueryRepoException);
		
		queryService.runPassengerQuery(queryObject);*/
	}
	
	private static QueryObject buildSimpleBetweenQuery() {
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
