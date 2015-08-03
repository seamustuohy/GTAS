package gov.gtas.querybuilder.service;

import gov.gtas.querybuilder.repository.QueryBuilderRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class QueryBuilderServiceTest {

	@Mock
	QueryBuilderRepository queryRepository;
	@InjectMocks
	QueryBuilderService queryService;
	
	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testSaveQuery() {
		
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
