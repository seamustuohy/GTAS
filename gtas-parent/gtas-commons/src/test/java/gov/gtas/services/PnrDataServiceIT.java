package gov.gtas.services;

import static org.junit.Assert.assertNotNull;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Address;
import gov.gtas.model.Agency;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
//import gov.gtas.model.Pax;
import gov.gtas.model.Phone;
import gov.gtas.model.PnrData;
import gov.gtas.model.Passenger;
import gov.gtas.model.lookup.PassengerTypeCode;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.LookUpRepository;

import java.util.Date;
import java.util.HashSet;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PnrDataServiceIT {

	
	@Autowired
	private PnrDataService pnrService;

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

	@Autowired
	private ApisMessageRepository apisMessageRepository;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testPnrDataSave() {
		System.out.println("##################################################");
		Flight f = new Flight();
		prepareFlightData( f);
		Passenger passengerToUpdate = new Passenger();
		preparePassengerData(passengerToUpdate);
		PnrData pnr = new PnrData();
		preparePnrData(pnr);
		passengerToUpdate.getPnrs().add(pnr);
		passengerToUpdate.getFlights().add(f);
		pnr.getPassengers().add(passengerToUpdate);
		
		
		f.getPassengers().add(passengerToUpdate);
		//testTarget.create(f);
		pnr.getFlights().add(f);
		pnrService.create(pnr);
		System.out.println("#####################pnr.getId()#############################"+pnr.getId());
		assertNotNull(pnr.getId());

	}

	private void preparePnrData(PnrData pnr){
		pnr.setBagCount(2);
		pnr.setDateBooked(new Date("7/7/2015"));
		pnr.setDateReceived(new Date("7/7/2015"));
		String cr = "AA";
		pnr.setCarrier(cr);
		pnr.setCreatedAt(new Date());
		pnr.setCreationDate();
		pnr.setCreatedBy("JUNIT TEST");
		pnr.setDaysBookedBeforeTravel(30);
		pnr.setDepartureDate(new Date());
		pnr.setFormOfPayment("CC");
		String a = "IAD";
		String c = "US";
		pnr.setOrigin(a);
		pnr.setOriginCountry(c);
		pnr.setPassengerCount(1);
		pnr.setTotalDwellTime(120);
		CreditCard cc = new CreditCard();
		cc.setCardExpiration("0417");
		cc.setCardHolderName("Srinivasarao Vempati");
		cc.setCardNumber("2222-3333-4444-5555");
		cc.setCardType("VISA");
		//cc.setPnrData(pnr);
		pnr.setCreditCard(cc);
		Address add = new Address();
		add.setAddressType("H");
		add.setCity("ALDIE");
		add.setCountry("USA");
		add.setLine1("41000 Zirocn dr");
		add.setPostalCode("20105");
		add.setState("VA");
		add.setCreationDate();
		add.setCreatedBy("JUNIT");
		add.setPnrData(pnr);
		Set adds = new HashSet<Address>();
		adds.add(add);
		pnr.setAddresses(adds);
		Phone p = new Phone();
		p.setPhoneNumber("24243534455");
		p.setPhoneType("H");
		p.setPnrData(pnr);
		p.setCreationDate();
		p.setCreatedBy("JUNIT");
		Set phones = new HashSet();
		phones.add(p);
		pnr.setPhones(phones);
		Agency ag = new Agency();
		ag.setAgencyCity("Aldie");
		ag.setAgencyCountry("USA");
		ag.setAgencyIdentifier("123456C");
		ag.setAgencyName("Some Test Agency");
		ag.setCreatedAt(new Date());
		ag.setAgencyState("VA");
		pnr.setAgency(ag);
		FrequentFlyer ff = new FrequentFlyer();
		ff.setFrequentFlyerNumber("1234");
		ff.setAirlineCode("DL");
		ff.setCreatedAt(new Date());
		ff.setCreatedBy("JUNIT");
		pnr.setFrequentFlyer(ff);
	}
	
	private void prepareFlightData(Flight f){
	    f.setDirection("I");
		f.setCreatedAt(new Date());
		f.setCreatedBy("JUNIT");
		// Airport a = new Airport(3616l,"Washington","IAD","KAID");
		String a = "IAD";
		System.out.println(a);
		f.setOrigin(a);
		String b = "JFK";
		// Airport b = new Airport (3584l,"Atlanta","ATL","KATL");

		f.setDestination(b);
		f.setEta(new Date());
		f.setEtd(new Date("7/31/2015"));
		f.setFlightDate(new Date());
		f.setFlightNumber("528");

		String c = "US";
		f.setDestinationCountry(c);
		f.setOriginCountry(c);

		String cr = "AA";
		f.setCarrier(cr);
		f.setUpdatedAt(new Date());
		f.setUpdatedBy("TEST");
	}
	
	private void preparePassengerData(Passenger passengerToUpdate){
	    passengerToUpdate.setPassengerType(PassengerTypeCode.P.name());
		passengerToUpdate.setAge(30);
		String c = "US";
		String b = "JFK";
		passengerToUpdate.setCitizenshipCountry(c);
		passengerToUpdate.setDebarkation(b);
		passengerToUpdate.setDebarkCountry(c);
		passengerToUpdate.setDob(new Date("04/06/1966"));
		passengerToUpdate.setEmbarkation(b);
		passengerToUpdate.setEmbarkCountry(c);
		passengerToUpdate.setFirstName("Srinivas");
		passengerToUpdate.setLastName("Test");
		passengerToUpdate.setCreatedBy("JUNIT");
		passengerToUpdate.setCreationDate();
		passengerToUpdate.setGender("M");
		passengerToUpdate.setSuffix("Jr");
		passengerToUpdate.setTitle("Mr");
		passengerToUpdate.setResidencyCountry(c);
	}
}
