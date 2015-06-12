package gov.cbp.taspd.gtas.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.cbp.taspd.gtas.bo.RuleExecutionStatistics;
import gov.cbp.taspd.gtas.config.RuleServiceConfig;
import gov.cbp.taspd.gtas.error.RuleServiceException;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Flight;
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
	public void testBasicRequest() {
	  Message msg = new Message();
	  msg.setTransmissionSource("Hello");
	  Date transmissionDate = new Date();
	  msg.setTransmissionDate(transmissionDate);
      RuleServiceResult res = testTarget.invokeRuleset(testTarget.createRuleServiceRequest(msg));
      assertNotNull(res);
      assertNotNull(res.getResultList());
      assertEquals("Result list is empty", 1, res.getResultList().size());
      assertEquals("Expected Transmission Date", transmissionDate, res.getResultList().get(0));
      RuleExecutionStatistics stats = res.getExecutionStatistics();
      assertNotNull(stats);
      assertEquals("Expected 2 rules to be fired", 2, stats.getRuleFiringSequence().size());     
      assertEquals("Expected 1 object to be modified", 1, stats.getModifiedObjectClassNameList().size());      

    }
	@Test
	public void testBasicApisRequest() {
	  Message msg = new Message();
	  msg.setTransmissionSource("Hello");
	  Date transmissionDate = new Date();
	  msg.setTransmissionDate(transmissionDate);
      RuleServiceResult res = testTarget.invokeRuleset(testTarget.createRuleServiceRequest(msg));
      assertNotNull(res);
      assertNotNull(res.getResultList());
      assertEquals("Result list is empty", 1, res.getResultList().size());
      assertEquals("Expected Transmission Date", transmissionDate, res.getResultList().get(0));
      RuleExecutionStatistics stats = res.getExecutionStatistics();
      assertNotNull(stats);
      assertEquals("Expected 2 rules to be fired", 2, stats.getRuleFiringSequence().size());     
      assertEquals("Expected 1 object to be modified", 1, stats.getModifiedObjectClassNameList().size());      

    }

	@Test(expected=RuleServiceException.class)
	public void testNullRequest() {
      testTarget.invokeRuleset("gtas.drl", null);
    }
	
//	@Test
//	public void testFlighSingleRuleHit() {
//	  ApisMessage msg = new ApisMessage();
//	  Flight flt = new Flight();
//	  flt.setFlightNumber("123");
//	  flt.setOrigin("Timbuktu");
//	  msg.getFlights().add(flt);
//      RuleServiceResult res = testTarget.invokeRuleset(testTarget.createRuleServiceRequest(msg));
//      assertNotNull(res);
//      assertNotNull(res.getResultList());
//      assertEquals("Result list is empty", 1, res.getResultList().size());
//      assertEquals("Expected flight in result list", flt, res.getResultList().get(0));
//    }

}
