/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.querybuilder.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.InvalidUserRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.IUserQueryResult;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.model.UserQueryRequest;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;
import gov.gtas.querybuilder.vo.PassengerQueryVo;
import gov.gtas.services.PassengerService;
import gov.gtas.services.dto.FlightsPageDto;
import gov.gtas.services.dto.PassengersPageDto;
import gov.gtas.vo.passenger.PassengerVo;

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
    @Mock
    private PassengerService passengerService;
    
    private static final String userId = "testUser";
    private static final int queryId = 1;
    private static UserQueryRequest request;
    private static QueryAlreadyExistsRepositoryException queryExistsRepoException;
    private static QueryDoesNotExistRepositoryException queryNotExistRepoException;
    private static InvalidQueryRepositoryException invalidQueryRepoException;
    
    private static QueryRequest queryRequest;
    
    @BeforeClass
    public static void setUp() throws Exception {
        queryRequest = buildQueryRequest();
        request = new UserQueryRequest();
        queryExistsRepoException = new QueryAlreadyExistsRepositoryException(Constants.QUERY_EXISTS_ERROR_MSG, new UserQuery());
        queryNotExistRepoException = new QueryDoesNotExistRepositoryException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, new UserQuery());
        invalidQueryRepoException = new InvalidQueryRepositoryException();
    }
    
    @Test
    public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, 
        QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, InvalidUserRepositoryException {
        UserQuery expected = new UserQuery();
        expected.setId(1);
        
        when(queryRepository.saveQuery(any(UserQuery.class))).thenReturn(expected);
        IUserQueryResult actual = queryService.saveQuery(userId,request);
        
        assertEquals(expected.getId().intValue(), actual.getId());
    }

    @Test(expected = QueryAlreadyExistsException.class)
    public void testSaveDuplicateQuery() throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, 
        QueryAlreadyExistsException, InvalidQueryException, InvalidUserRepositoryException {
        when(queryRepository.saveQuery(any(UserQuery.class))).thenThrow(queryExistsRepoException);
        
        queryService.saveQuery(userId,request);
    }
        
    @Test(expected = InvalidQueryException.class)
    public void testSaveInvalidQuery() throws QueryAlreadyExistsRepositoryException, InvalidQueryRepositoryException, 
        QueryAlreadyExistsException, InvalidQueryException, InvalidUserRepositoryException {
        when(queryRepository.saveQuery(any(UserQuery.class))).thenThrow(invalidQueryRepoException);
        
        queryService.saveQuery(userId,request);
    }
    
    @Test
    public void testEditQuery() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
        InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException, InvalidUserRepositoryException {
        UserQuery expected = new UserQuery();
        expected.setId(1);
        
        when(queryRepository.editQuery(any(UserQuery.class))).thenReturn(expected);
        IUserQueryResult actual = queryService.editQuery(userId,request);
        
        assertEquals(expected.getId().intValue(), actual.getId());
    }
    
    @Test(expected = QueryAlreadyExistsException.class)
    public void testEditQueryDuplicateTitle() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
        InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException, InvalidUserRepositoryException {
        when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(queryExistsRepoException);
        
        queryService.editQuery(userId,request);
    }
    
    @Test(expected = QueryDoesNotExistException.class)
    public void testEditQueryDoesNotExist() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
        InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException, InvalidUserRepositoryException {
        when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(queryNotExistRepoException);
        
        queryService.editQuery(userId,request);
    }
    
    @Test(expected = InvalidQueryException.class)
    public void testEditQueryInvalidQuery() throws QueryAlreadyExistsRepositoryException, QueryDoesNotExistRepositoryException, 
        InvalidQueryRepositoryException, QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException, InvalidUserRepositoryException {
        when(queryRepository.editQuery(any(UserQuery.class))).thenThrow(invalidQueryRepoException);
        
        queryService.editQuery(userId,request);
    }

    @Test
    public void testListQueryByUser() throws InvalidQueryException, InvalidUserRepositoryException {
        List<UserQuery> expected = new ArrayList<>();
        UserQuery userQuery = new UserQuery();
        userQuery.setId(1);
        expected.add(userQuery);
        
        when(queryRepository.listQueryByUser(userId)).thenReturn(expected);
        List<IUserQueryResult> actual = queryService.listQueryByUser(userId);
        
        assertEquals(expected.get(0).getId().intValue(), actual.get(0).getId());
    }
    
    @Test(expected = QueryDoesNotExistException.class)
    public void testDeleteQueryDoesNotExist() throws QueryDoesNotExistRepositoryException, QueryDoesNotExistException, InvalidUserRepositoryException, InvalidQueryException {
        doThrow(queryNotExistRepoException).when(queryRepository).deleteQuery(userId, queryId);

        queryService.deleteQuery(userId, queryId);
    }
    
//  @Test
//  public void testRunFlightQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
//      List<Flight> expected = new ArrayList<>();
//      Flight flight = new Flight();
//      flight.setId(1L);
//      expected.add(flight);
//      
//      when(queryRepository.getFlightsByDynamicQuery(queryRequest)).thenReturn(expected);
//      FlightsPageDto result = queryService.runFlightQuery(queryRequest);
//      
//      assertEquals(expected.get(0).getId(), result.getFlights().get(0).getId());
//  }
    
    @Test(expected = InvalidQueryException.class)
    public void testRunFlightQueryInvalidQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
        when(queryRepository.getFlightsByDynamicQuery(queryRequest)).thenThrow(invalidQueryRepoException);
        
        queryService.runFlightQuery(queryRequest);
    }
    
//  @Test
//  public void testRunPassengerQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
//      PassengerQueryVo expected = new PassengerQueryVo();
//      Passenger passenger = new Passenger();
//      passenger.setId(1L);
//      Flight flight = new Flight();
//      flight.setFlightNumber("123");
//      expected.add(new Object[]{1L, passenger, flight});
//      
//      Passenger expectedPassenger = (Passenger) expected.get(0)[1];
//      Flight expectedFlight = (Flight) expected.get(0)[2];
//      
//      when(queryRepository.getPassengersByDynamicQuery(queryRequest)).thenReturn(expected);
//      doNothing().when(passengerService).fillWithHitsInfo(any(PassengerVo.class), anyLong(), anyLong());
//      PassengersPageDto result = queryService.runPassengerQuery(queryRequest);
//      PassengerVo actual = (PassengerVo) result.getPassengers().get(0);
//      
//      assertEquals(expectedPassenger.getId(), actual.getId());
//      assertEquals(expectedFlight.getFlightNumber(), actual.getFlightNumber());
//  }

    @Test(expected = InvalidQueryException.class)
    public void testRunPassengerQueryInvalidQuery() throws InvalidQueryRepositoryException, InvalidQueryException {
        when(queryRepository.getPassengersByDynamicQuery(queryRequest)).thenThrow(invalidQueryRepoException);
        
        queryService.runPassengerQuery(queryRequest);
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
    
    private static QueryRequest buildQueryRequest() {
        QueryRequest req = new QueryRequest();
        
        req.setPageNumber(1);
        req.setPageSize(-1);
        req.setQuery(buildSimpleBetweenQuery());
        return req;
    }
}
