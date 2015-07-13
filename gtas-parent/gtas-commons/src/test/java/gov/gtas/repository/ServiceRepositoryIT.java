package gov.gtas.repository;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Gender;
import gov.gtas.model.Passport;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;
import gov.gtas.services.AirportService;
import gov.gtas.services.CarrierService;
import gov.gtas.services.CountryService;
import gov.gtas.services.FlightService;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceRepositoryIT {

	@Autowired
	private FlightService testTarget;

	@Autowired
	private AirportService aService;

	@Autowired
	private CountryService cService;

	@Autowired
	private CarrierService crService;

	@Autowired
	private LookUpRepository lookupDao;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test()
	// public void testFindFlightRequest() {
	// List<Flight> flights = testTarget.findAll();
	// assertNotNull(flights);
	// assertNotNull(flights.size());
	// assertEquals(2, flights.size());
	// System.out.println("********************************************************");
	// assertEquals("Result list expected ", 1, flights.size());
	//
	// //for(Flight f : flights){
	// //System.out.println("Flight detail"+f.getFlightNumber());
	// //}
	// System.out.println("********************************************************");
	//
	// }
	// @Test()
	public void testAddFlight() {
		Flight f = new Flight();
		f.setCreatedAt(new Date());
		f.setCreatedBy("JUNIT");
		// Airport a = new Airport(3616l,"Washington","IAD","KAID");
		Airport a = aService.getAirportByThreeLetterCode("IAD");
		System.out.println(a);
		f.setOrigin(a);
		Airport b = aService.getAirportByThreeLetterCode("JFK");
		// Airport b = new Airport (3584l,"Atlanta","ATL","KATL");

		f.setDestination(b);
		f.setEta(new Date());
		f.setEtd(new Date("6/20/2015"));
		f.setFlightDate(new Date());
		f.setFlightNumber("8002");

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
		passengerToUpdate.setDob(new Date("04/06/1980"));
		passengerToUpdate.setEmbarkation(b);
		passengerToUpdate.setEmbarkCountry(c);
		passengerToUpdate.setFirstName("Mike");
		Set hs = new HashSet<Flight>();
		hs.add(f);
		passengerToUpdate.setFlights(hs);
		passengerToUpdate.setGender(Gender.M);
		passengerToUpdate.setLastName("Copenhafer");
		passengerToUpdate.setResidencyCountry(c);
		// passengerToUpdate.setDocuments(passenger.getDocuments());
		passengerToUpdate.setSuffix("Mr.");
		// passengerToUpdate.setTitle(passenger.getTitle());
		// passengerToUpdate.setType(PaxType.PAX);
		passengerToUpdate.setCreatedAt(new Date());
		passengerToUpdate.setCreatedBy("JUNIT TEST");

		Passport d = new Passport();
		d.setDocumentNumber("T00123456");
		d.setExpirationDate(new Date("6/6/2020"));
		d.setIssuanceDate(new Date("6/6/1999"));
		d.setIssuanceCountry(c);
		d.setTraveler(passengerToUpdate);
		Set<Document> docs = new HashSet<>();
		docs.add(d);
		passengerToUpdate.setDocuments(docs);

		Set<Traveler> passengers = new HashSet<Traveler>();
		passengers.add(passengerToUpdate);
		f.setPassengers(passengers);

		testTarget.create(f);
		System.out
				.println("********************************************************");
		System.out
				.println("******************Saved Flight***********************"
						+ f.toString());
		System.out
				.println("********************************************************");

	}

	@Test()
	public void testSecondaryCahe() {
		System.out.println("\n>>>>> testSecondaryCahe <<<<<\n");
		
		List<Airport> airports = lookupDao.getAllAirports();
		System.out
				.println("***********************AIRPORTS*******************************"
						+ airports.size());
		List<Carrier> carriers = lookupDao.getAllCarriers();
		System.out
				.println("***********************CARRIERS*******************************"
						+ carriers.size());
		List<Country> countries = lookupDao.getAllCountries();
		System.out
				.println("*************COUNTRIES*******************************"
						+ countries.size());
		
		// next, get data from cache without going to db
		// verification: no sql execution on console output
		
		airports = lookupDao.getAllAirports();
		System.out
				.println("***********************AIRPORTS*******************************"
						+ airports.size());
		carriers = lookupDao.getAllCarriers();
		System.out
				.println("***********************CARRIERS*******************************"
						+ carriers.size());
		countries = lookupDao.getAllCountries();
		System.out
				.println("*************COUNTRIES*******************************"
						+ countries.size());
	}

	@Test()
	public void testEmptyCache() {
		System.out.println("\n>>>>> testEmptyCache <<<<<\n");
		
		List<Carrier> carriers = lookupDao.getAllCarriers();
		System.out
				.println("***********************CARRIERS- get from db*******************************"
						+ carriers.size());

		carriers = lookupDao.getAllCarriers();
		System.out
				.println("***********************CARRIERS- get from cache *******************************"
						+ carriers.size());

		lookupDao.clearAllEntitiesCache();
		System.out
		.println("***********************CARRIERS- empty cache *******************************");

		carriers = lookupDao.getAllCarriers();
		System.out
				.println("*************CARRIERS- retrieve from db again*******************************"
						+ carriers.size());
		
		carriers = lookupDao.getAllCarriers();
		System.out
				.println("***********************CARRIERS- get from cache again *******************************"
						+ carriers.size());
		
		lookupDao.clearAllEntitiesCache();
		System.out
		.println("***********************CARRIERS- empty cache -final *******************************\n");

	}

	private Airport createAirport(final String airportName) {
		Airport airport = new Airport();
		ReflectionTestUtils.setField(airport, "name", airportName);
		return airport;
	}

	private Country createCountry() {
		Country country = new Country();
		ReflectionTestUtils.setField(country, "name", "Test123");
		ReflectionTestUtils.setField(country, "iso2", "AJ");
		ReflectionTestUtils.setField(country, "iso3", "AJT");

		return country;
	}

	@Test
	public void testSingleEntityCache() {
		System.out.println("\n>>>>> testSingleEntityCache <<<<<\n");
		
		Country country = createCountry();
		lookupDao.saveCountry(country);
		
		System.out
				.println("*************save country*****************************"
						+ country.getName());
		
		lookupDao.getCountry(country.getName());
		System.out
				.println("*************get country from db*****************************"
						+ country.getName());
		lookupDao.getCountry(country.getName());
		System.out
				.println("*************get country from cache*****************************"
						+ country.getName());
		lookupDao.removeCountryCache(country.getName());
		System.out
				.println("*************delete country and remove it from cache*****************************"
						+ country.getName());
		lookupDao.getCountry(country.getName());
		System.out
				.println("*************get country again from db*****************************"
						+ country.getName());
		lookupDao.deleteCountryDb(country);
		System.out.println("************* Done***********************\n");
	}
}
