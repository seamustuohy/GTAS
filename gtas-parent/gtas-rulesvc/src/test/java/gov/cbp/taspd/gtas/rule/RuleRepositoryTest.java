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
import java.util.HashSet;

import gov.cbp.taspd.gtas.model.Pax;

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
	
	@Test(expected=RuleServiceException.class)
	public void testNullRequest() {
      testTarget.invokeRuleset("gtas.drl", null);
    }

	@Test
	public void testBasicApisRequest() {
	  Pax p1 = createPassenger("Medulla", "Oblongata", "Timbuktu");
	  ApisMessage msg = createBasicApisMessage(p1);
      RuleServiceResult res = testTarget.invokeRuleset(testTarget.createRuleServiceRequest(msg));
      assertNotNull(res);
      assertNotNull(res.getResultList());
      assertEquals("Result list is empty", 1, res.getResultList().size());
      assertEquals("Expected Passenger", p1, res.getResultList().get(0));
      RuleExecutionStatistics stats = res.getExecutionStatistics();
      assertNotNull(stats);
      assertEquals("Expected 2 rules to be fired", 2, stats.getRuleFiringSequence().size());
      //Expecting 1 flight object and one passenger object to be inserted
      assertEquals("Expected 2 object to be inserted", 2, stats.getInsertedObjectClassNameList().size());      
    }

	/**
	 * creates a simple passenger object.
	 * @param fn
	 * @param ln
	 * @param embarkation
	 * @return
	 */
	private Pax createPassenger(final String fn, final String ln, final String embarkation){
		Pax p = new Pax();
		p.setFirstName(fn);
		p.setLastName(ln);
		p.setEmbarkation(embarkation);
		return p;
	}
	/**
	 * Creates a simple ApisMessage with a single passenger
	 */
	private ApisMessage createBasicApisMessage(final Pax passenger){
		  ApisMessage msg = new ApisMessage();
		  Flight flight = new Flight();
		  HashSet<Pax> set = new HashSet<Pax>();
		  set.add(passenger);
		  flight.setPassengers(set);
		  flight.setDestination("Narnia");
		  HashSet<Flight> flightSet = new HashSet<Flight>();
		  flightSet.add(flight);
		  msg. setFlights(flightSet);
		  return msg;
	}
}