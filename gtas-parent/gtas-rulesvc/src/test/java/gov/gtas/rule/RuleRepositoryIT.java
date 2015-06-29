package gov.gtas.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.error.CommonServiceException;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.lookup.Airport;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
public class RuleRepositoryIT {
	@Autowired
	private RuleService testTarget;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test(expected=CommonServiceException.class)
	public void testNullRequest() {
      testTarget.invokeRuleset("gtas.drl", null);
    }

//	@Test
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
      assertEquals("Expected 2 rules to be fired", 2, stats.getTotalRulesFired());
      assertEquals("Expected 2 rule names in list", 2, stats.getRuleFiringSequence().size());
      //Expecting 1 flight object and one passenger object to be inserted
      assertEquals("Expected 2 object to be affected", 2, stats.getTotalObjectsModified());
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
		p.setEmbarkation(createAirport("Timbuktu"));
		return p;
	}
	private Airport createAirport(final String airportName){
		Airport airport = new Airport();
		ReflectionTestUtils.setField(airport, "name", airportName);
		return airport;
	}
	/**
	 * Creates a simple ApisMessage with a single passenger
	 */
	private ApisMessage createBasicApisMessage(final Traveler passenger){
		  ApisMessage msg = new ApisMessage();
		  Flight flight = new Flight();
		  HashSet<Traveler> set = new HashSet<Traveler>();
		  set.add(passenger);
		  flight.setPassengers(set);
		  flight.setDestination(createAirport("foo"));
		  HashSet<Flight> flightSet = new HashSet<Flight>();
		  flightSet.add(flight);
		  msg.setFlights(flightSet);
		  return msg;
	}
}
