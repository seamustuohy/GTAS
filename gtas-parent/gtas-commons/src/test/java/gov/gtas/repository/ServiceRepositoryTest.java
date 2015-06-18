package gov.gtas.repository;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;
import gov.gtas.model.lookup.Gender;
import gov.gtas.model.lookup.PaxType;
import gov.gtas.services.AirportService;
import gov.gtas.services.CarrierService;
import gov.gtas.services.CountryService;
import gov.gtas.services.FlightService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
	
	@Autowired
	private AirportService aService;
	
	@Autowired
	private CountryService cService;
	
	@Autowired 
	private CarrierService crService;

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
		//Airport a = new Airport(3616l,"Washington","IAD","KAID");
		Airport a = aService.getAirportByThreeLetterCode("IAD");
		f.setOrigin(a);
		Airport b = aService.getAirportByThreeLetterCode("ATL");
		//Airport b = new Airport (3584l,"Atlanta","ATL","KATL");
		
		
		f.setDestination(b);
		f.setEta(new Date());
		f.setEtd(new Date("6/18/2015"));
		f.setFlightDate(new Date());
		f.setFlightNumber("0778");

		Country c = cService.getCountryByTwoLetterCode("US");
		f.setDestinationCountry(c);
		f.setOriginCountry(c);
		
		Carrier cr = crService.getCarrierByTwoLetterCode("AA");
		f.setCarrier(cr);
		f.setUpdatedAt(new Date());
		f.setUpdatedBy("TEST");
		
		Pax passengerToUpdate = new Pax();
		passengerToUpdate.setAge(30);
		passengerToUpdate.setCitizenshipCountry(c);
		passengerToUpdate.setDebarkation(b);
		passengerToUpdate.setDebarkCountry(c);
		passengerToUpdate.setDob(new Date("06/06/1986"));
		passengerToUpdate.setEmbarkation(b);
		passengerToUpdate.setEmbarkCountry(c);
		passengerToUpdate.setFirstName("Srinivas");
		Set hs = new HashSet<Flight>();
		hs.add(f);
		passengerToUpdate.setFlights(hs);
		passengerToUpdate.setGender(Gender.M);
		passengerToUpdate.setLastName("Vempati");
		passengerToUpdate.setResidencyCountry(c);
		//passengerToUpdate.setDocuments(passenger.getDocuments());
		passengerToUpdate.setSuffix("Mr.");
		//passengerToUpdate.setTitle(passenger.getTitle());
		passengerToUpdate.setType(PaxType.PAX);
		Set<Pax> passengers = new HashSet<Pax>();
		passengers.add(passengerToUpdate);
		f.setPassengers(passengers);
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
