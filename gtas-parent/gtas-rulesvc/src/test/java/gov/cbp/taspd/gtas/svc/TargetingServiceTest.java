package gov.cbp.taspd.gtas.svc;

import gov.cbp.taspd.gtas.config.RuleServiceConfig;
import gov.cbp.taspd.gtas.rule.RuleService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
public class TargetingServiceTest {
    @Autowired
    TargetingService targetingService;
    
    @Autowired
    RuleService ruleService;
    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull("Autowire of targeting service failed",targetingService);
		assertNotNull("Autowire of rule service failed",ruleService);
	}

}
