package gov.gtas.svc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.repository.ApisMessageRepository;

import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 	Unit tests for the TargetingService using spring support and Mockito.
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
@TransactionConfiguration(defaultRollback = true)
public class TargetingServiceIT {
    @Autowired
    TargetingService targetingService;

	@Resource
    private ApisMessageRepository apisMessageRepository;

    @Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
//	@Test
//	@Transactional
//	public void testApisRuleExecution() {
//		Iterable<ApisMessage> messages = apisMessageRepository.findAll();
//		assertNotNull(messages);
//		int count = 0;
//		for(ApisMessage msg:messages){
//			++count;
//			System.out.println("Processsing APIs message:"+msg.getId());
//			Set<Flight> flights = msg.getFlights();
//			assertTrue("ApisMessage has no flights:"+ msg.getId(), flights.size() > 0);
//            for(Flight flt:flights){
//            	verifyPassengers(flt, flt.getPassengers());
//            }
//		}
//		assertTrue("There are no API messsages", count > 0);
//		System.out.println("APIs message count = "+ count);
//	}

	@Test
	@Transactional
	public void testApisMessage() {
		Iterable<ApisMessage> messages = apisMessageRepository.findAll();
		assertNotNull(messages);
		int count = 0;
		for(ApisMessage msg:messages){
			int reqObjCount = 0;
			++count;
			System.out.println("Processsing APIs message:"+msg.getId());
			Set<Flight> flights = msg.getFlights();
			assertTrue("ApisMessage has no flights:"+ msg.getId(), flights.size() > 0);
            for(Flight flt:flights){
            	reqObjCount += verifyPassengers(flt, flt.getPassengers());
            }
            RuleServiceRequest req = TargetingServiceUtils.createApisRequest(msg);
    		assertEquals(reqObjCount, req.getRequestObjects().size());         
		}
		assertTrue("There are no API messsages", count > 0);
		
		System.out.println("APIs message count = "+ count);
	}
    private int verifyPassengers(Flight flt, Set<Traveler> passengers){
    	int travCount = 0;
    	int paxCount = 0;
    	int docCount = 0;
    	for(Traveler traveler: passengers){
    		++travCount;
    		if(traveler instanceof Pax){
    			++paxCount;
    			Set<Document> docs = traveler.getDocuments();
    			assertNotNull(docs);
    			assertTrue(docs.size() > 0);
    			docCount += docs.size();
    		}
    	}
    	assertTrue("Flight has no travelers:"+flt.getFlightNumber(), travCount > 0);
    	assertTrue("Flight has no passengers:"+flt.getFlightNumber(), paxCount > 0);
		System.out.println("Flight:"+ flt.getFlightNumber() + " - Traveler count = "+ travCount + ", Passenger count ="+paxCount);
		return travCount+docCount+1;//travelers+documents_flight
    }
}
