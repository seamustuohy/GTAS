package gov.gtas.querybuilder.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
	private QueryObject queryObject;
	private QueryRequest request;
	private UserQuery query;
	private QueryAlreadyExistsRepositoryException queryExistsRepoException;
	private QueryDoesNotExistRepositoryException queryNotExistRepoException;
	private InvalidQueryRepositoryException invalidQueryRepoException;
	
	@Before
	public void init() {
		queryObject = new QueryObject();
		request = new QueryRequest();
		query = new UserQuery();
		queryExistsRepoException = new QueryAlreadyExistsRepositoryException(Constants.QUERY_EXISTS_ERROR_MSG, query);
		queryNotExistRepoException = new QueryDoesNotExistRepositoryException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, query);
		invalidQueryRepoException = new InvalidQueryRepositoryException();
	}

	@Test
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, 
		QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException {
		UserQuery expected = new UserQuery();
		expected.setId(1);
		
		when(queryRepository.saveQuery(any(UserQuery.class))).thenReturn(expected);
		UserQuery actual = queryService.saveQuery(request);
		
		assertEquals(expected, actual);
	}

	@Test(expected = QueryAlreadyExistsException.class)
	public void testSaveDuplicateQuery() throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, 
		QueryAlreadyExistsException, InvalidQueryException {
		when(queryRepository.saveQuery(any(UserQuery.class))).thenThrow(queryExistsRepoException);
		
		queryService.saveQuery(request);
	}
		
	@Test(expected = InvalidQueryException.class)
	public void testSaveInvalidQuery() throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, 
		QueryAlreadyExistsException, InvalidQueryException {
		when(queryRepository.saveQuery(any(UserQuery.class))).thenThrow(invalidQueryRepoException);
		
		queryService.saveQuery(request);
	}
	
	@Test
	public void testEditQuery() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		UserQuery expected = new UserQuery();
		expected.setId(1);
		
		when(queryRepository.editQuery(any(UserQuery.class))).thenReturn(expected);
		UserQuery actual = queryService.editQuery(request);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = QueryAlreadyExistsException.class)
	public void testEditQueryDuplicateTitle() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(queryExistsRepoException);
		
		queryService.editQuery(request);
	}
	
	@Test(expected = QueryDoesNotExistException.class)
	public void testEditQueryDoesNotExist() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(queryNotExistRepoException);
		
		queryService.editQuery(request);
	}
	
	@Test(expected = InvalidQueryException.class)
	public void testEditQueryInvalidQuery() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
		InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(invalidQueryRepoException);
		
		queryService.editQuery(request);
	}

	@Test
	public void testListQueryByUser() {
		List<UserQuery> expected = new ArrayList<>();
		
		when(queryRepository.listQueryByUser(userId)).thenReturn(expected);
		List<UserQuery> actual = queryService.listQueryByUser(userId);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = QueryDoesNotExistException.class)
	public void testDeleteQueryDoesNotExist() throws QueryDoesNotExistRepositoryException, QueryDoesNotExistException {
		doThrow(queryNotExistRepoException).when(queryRepository).deleteQuery(userId, queryId);

		queryService.deleteQuery(userId, queryId);
	}
	
	@Test
	public void testRunFlightQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		List<Flight> expected = new ArrayList<>();
		
		when(queryRepository.getFlightsByDynamicQuery(queryObject)).thenReturn(expected);
		List<Flight> actual = queryService.runFlightQuery(queryObject);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = InvalidQueryException.class)
	public void testRunFlightQueryInvalidQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		when(queryRepository.getFlightsByDynamicQuery(queryObject)).thenThrow(invalidQueryRepoException);
		
		queryService.runFlightQuery(queryObject);
	}
	
	@Test
	public void testRunPassengerQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		List<Passenger> expected = new ArrayList<>();
		
		when(queryRepository.getPassengersByDynamicQuery(queryObject)).thenReturn(expected);
		List<Passenger> actual = queryService.runPassengerQuery(queryObject);
		
		assertEquals(expected, actual);
	}

	@Test(expected = InvalidQueryException.class)
	public void testRunPassengerQueryInvalidQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
		when(queryRepository.getPassengersByDynamicQuery(queryObject)).thenThrow(invalidQueryRepoException);
		
		queryService.runPassengerQuery(queryObject);
	}
}
