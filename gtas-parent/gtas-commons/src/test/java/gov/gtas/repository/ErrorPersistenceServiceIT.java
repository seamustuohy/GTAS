package gov.gtas.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.error.ErrorDetails;
import gov.gtas.error.ErrorUtils;
import gov.gtas.services.ErrorPersistenceService;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback = true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ErrorPersistenceServiceIT {

	@Autowired
	private ErrorPersistenceService testTarget;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
    @Test
    @Transactional
    public void createErrorTest(){
    	ErrorDetails err = ErrorUtils.createErrorDetails(new NullPointerException("Test Error"));
    	err = testTarget.create(err);
    	assertNotNull(err);
    	assertNotNull(err.getErrorId());
    	assertEquals("Test Error",err.getErrorDescription());
    }

    @Test
    @Transactional
    public void findErrorTest(){
    	ErrorDetails err = ErrorUtils.createErrorDetails(new NullPointerException("Test Error"));
    	err = testTarget.create(err);
    	ErrorDetails err2 = testTarget.findById(err.getErrorId());
    	assertNotNull(err2);
    	assertNotNull(err2.getErrorId());
    	assertEquals("Test Error",err2.getErrorDescription());
    }
}
