package gov.gtas.querybuilder.service;

import static org.mockito.Mockito.when;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class QueryBuilderServiceTest {

	@InjectMocks
	private QueryBuilderService queryService;
	@Mock
	private QueryBuilderRepository queryRepository;
	
	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException {
		QueryRequest request = new QueryRequest();
		UserQuery query = new UserQuery();;
		when(queryService.saveQuery(request)).thenReturn(query);
	}

	@Test
	public void testSaveDuplicateQuery() {
//		fail("Not yet implemented");
	}
	
	@Test
	public void testSaveQueryWithNullDescription() {
//		fail("Not yet implemented");
	}
	
	@Test
	public void testSaveInvalidQuery() {
//		fail("Not yet implemented");
	}
	
	@Test
	public void testEditQuery() {
//		fail("Not yet implemented");
	}
	
	@Test
	public void testEditQueryToDuplicateTitle() {
//		fail("Not yet implemented");
	}

	@Test
	public void testListQueryByUser() {
//		fail("Not yet implemented");
	}
	
	@Test
	public void testRunPassengerQuery() {
//		fail("Not yet implemented");
	}

}
