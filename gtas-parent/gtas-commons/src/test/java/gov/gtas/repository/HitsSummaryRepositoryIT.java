package gov.gtas.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Flight;
import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.services.HitsSummaryService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

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
public class HitsSummaryRepositoryIT {

	@Autowired
	private HitsSummaryRepository testTarget;
	
	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private HitsSummaryService hitsSummaryService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	@Transactional
	public void testFindHitDetailByUdr() {
		long udrId1 = 999954L;
		long udrId2 = 888854L;
		
		Object[] ids = createPassengerFlight();
		assertEquals(2, ids.length);
		assertTrue(((Passenger)ids[0]).getId() >0);
		assertTrue(((Flight)ids[1]).getId() >0);
		
		createHitsSummary(udrId1,(Passenger)ids[0], (Flight)ids[1]);
		createHitsSummary(udrId1,(Passenger)ids[0], (Flight)ids[1]);
		
		createHitsSummary(udrId2,(Passenger)ids[0], (Flight)ids[1]);
		createHitsSummary(udrId2,(Passenger)ids[0], (Flight)ids[1]);
		
		List<Object[]> udrSummaryList = testTarget.findDetailsByUdr();
		assertNotNull(udrSummaryList);
		assertTrue(udrSummaryList.size() >= 2);
		int count1 = 0;
		int count2 = 0;
		for(Object[] data:udrSummaryList){
			Long udrId = (Long)data[0];
			Long rlcount =  (Long)data[1];
			assertNotNull(udrId);
			if(udrId.equals(udrId1)){
				count1++;
				assertEquals(2, rlcount.intValue());
			}
			if(udrId.equals(udrId2)){
				count2++;
				assertEquals(2, rlcount.intValue());
			}
		}
		assertEquals(1, count1);
		assertEquals(1, count2);
	}
	private Object[] createPassengerFlight(){
		Passenger p = new Passenger();
		p.setDeleted(false);
		p.setPassengerType("P");
		passengerRepository.save(p);
		
		Flight f = new Flight();
		f.setFlightDate(new Date());
		f.setFlightNumber("899");
		f.setOrigin("IAD");
		f.setListHitCount(21);
		f.setPassengerCount(234);
		f.setRuleHitCount(3);
		f.setCarrier("DL");
		f.setDestination("DXB");
		f.setDirection("I");
		flightRepository.save(f);

		return new Object[]{p, f};
	}
	private HitsSummary createHitsSummary(Long udrId, Passenger p, Flight f){
		HitsSummary ret = new HitsSummary();
		ret.setCreatedDate(new Date());
		ret.setFlight(f);
		ret.setPassenger(p);
		ret.setHitType("R");
		ret.setRuleHitCount(1);
		ret.setWatchListHitCount(0);
		
		List<HitDetail> detList = new LinkedList<HitDetail>();
		HitDetail det = new HitDetail();
		det.setCreatedDate(new Date());
		det.setDescription("jkkjhg");
		det.setHitType("R");
		det.setRuleId(udrId);
		det.setTitle("Hello");
		det.setParent(ret);
		detList.add(det);
		ret.setHitdetails(detList);
		
		ret = testTarget.save(ret);
		return ret;
	}
}
