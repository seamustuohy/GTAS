package gov.cbp.taspd.gtas.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import gov.cbp.taspd.gtas.config.RuleServiceConfig;
import gov.cbp.taspd.gtas.error.RuleServiceException;
import gov.cbp.taspd.gtas.model.Message;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
public class RuleRepositoryTest {
	@Autowired
	private RuleService testTarget;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDummyRequest() {
	  Message msg = new Message();
	  msg.setTransmissionSource("Hello");
	  Date transmissionDate = new Date();
	  msg.setTransmissionDate(transmissionDate);
      RuleServiceResult res = testTarget.invokeRuleset(testTarget.createRuleServiceRequest(msg));
      assertNotNull(res);
      assertNotNull(res.getResultList());
      assertEquals("Result list is empty", 1, res.getResultList().size());
      assertEquals("Expected Transmission Date", transmissionDate, res.getResultList().get(0));
    }

	@Test(expected=RuleServiceException.class)
	public void testNullRequest() {
      testTarget.invokeRuleset("gtas.drl", null);
    }
	
//	@Test
//	public void testRuleR1() {
//	  Message msg = new Message();
//	  msg.setTransmissionSource("Hello");
//	  Date transmissionDate = new Date();
//	  msg.setTransmissionDate(transmissionDate);
//      RuleServiceResult res = testTarget.invokeRuleset(testTarget.createRuleServiceRequest(msg));
//      assertNotNull(res);
//      assertNotNull(res.getResultList());
//      assertEquals("Result list is empty", 1, res.getResultList().size());
//      assertEquals("Expected Transmission Date", transmissionDate, res.getResultList().get(0));
//    }

}
