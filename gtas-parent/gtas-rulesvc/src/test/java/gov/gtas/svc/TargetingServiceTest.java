package gov.gtas.svc;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.model.ApisMessage;
import gov.gtas.rule.RuleService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 	Unit tests for the TargetingService using spring support and Mockito.
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
public class TargetingServiceTest {
    @Autowired
    TargetingService targetingService;
    
    @Mock
    private RuleService mockRuleService;
    
    @Before
	public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitialization() {		
		assertNotNull("Autowire of targeting service failed",targetingService);
		assertNotNull("Autowire of rule service failed",ReflectionTestUtils.getField(targetingService, "ruleService"));
	}
	@Test
	public void testAnalyzeApisMessage() {
		ReflectionTestUtils.setField(targetingService, "ruleService", mockRuleService);
		ApisMessage message = new ApisMessage();
		targetingService.analyzeApisMessage(message);
		verify(mockRuleService).invokeRuleEngine(any(RuleServiceRequest.class));
	}

}
