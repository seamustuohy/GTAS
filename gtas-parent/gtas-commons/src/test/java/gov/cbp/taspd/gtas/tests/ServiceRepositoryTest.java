package gov.cbp.taspd.gtas.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.cbp.taspd.gtas.services.FlightService;
import gov.cbp.taspd.gtas.model.Airport;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Message;
import gov.cbp.taspd.gtas.config.CommonServicesConfig;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CommonServicesConfig.class)
public class ServiceRepositoryTest {
	
	@Autowired
	private FlightService testTarget;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
//	@Test()
//	public void testFindFlightRequest() {
//		List<Flight> flights = testTarget.findAll();
//		assertNotNull(flights);
//	    assertNotNull(flights.size());
//	    assertEquals(2, flights.size());
//	    System.out.println("********************************************************");
//	    assertEquals("Result list expected ", 1, flights.size());
//	    
//	    //for(Flight f : flights){
//	    	//System.out.println("Flight detail"+f.getFlightNumber());
//	    //}
//	    System.out.println("********************************************************");
//		
//    }
	@Test()
	public void testAddFlight() {
		Flight f = new Flight();
		f.setCreatedAt(new Date());
		f.setCreatedBy("JUNIT");
		//Airport a = this.createAirport("ATL");
		//f.setOrigin(a);
		//Airport b = this.createAirport("IAD");
		//f.setDestination(b);
		f.setEta(new Date());
		f.setEtd(new Date("6/17/2015"));
		f.setFlightDate(new Date());
		f.setFlightNumber("709");

		testTarget.create(f);
	    System.out.println("********************************************************");
	    System.out.println("******************Saved Flight***********************"+f.toString());
	    System.out.println("********************************************************");
		
    }

	private Airport createAirport(final String airportName){
		Airport airport = new Airport();
		ReflectionTestUtils.setField(airport, "name", airportName);
		return airport;
	}

}
