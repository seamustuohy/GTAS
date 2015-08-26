package gov.gtas.svc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;
import gov.gtas.testdatagen.ApisDataGenerator;
import gov.gtas.testdatagen.PnrDataGenerator;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TargetingServiceUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

    @Test
    public void testPnrRuleRequestCreation(){
		PnrMessage msg = PnrDataGenerator.createTestPnrmessage(1L);
		RuleServiceRequest request = TargetingServiceUtils
				.createPnrRequest(msg);
		Collection<?> reqObjects = request.getRequestObjects();
		assertNotNull(reqObjects);		
		assertEquals(48, reqObjects.size());//2pnr+2flt+6pass+6doc+3addr+2email+2phone+2ff+2cc+2agency + (6+3+ 2*5)links = 48		
    }

	@Test
	public void testApisRuleRequestCreation() {
		ApisMessage message = ApisDataGenerator.createSimpleTestApisMesssage();
		assertNotNull(message);
		RuleServiceRequest request = TargetingServiceUtils
						.createApisRequest(message);
		Collection<?> reqObjects = request.getRequestObjects();
		assertNotNull(reqObjects);		
		assertEquals(14, reqObjects.size());//2 flights with 3 passengers each. Each passenger has 1 doc.		
	}

	@Test
	public void testApisPnrRuleRequestCreation() {
		ApisMessage apis = ApisDataGenerator.createSimpleTestApisMesssage();
		PnrMessage pnr = PnrDataGenerator.createTestPnrmessage(1L);
		RuleServiceRequest request = TargetingServiceUtils
						.createPnrApisRequest(Arrays.asList(apis), Arrays.asList(pnr));
		Collection<?> reqObjects = request.getRequestObjects();
		assertNotNull(reqObjects);		
		assertEquals(62, reqObjects.size());//48 PNR + 14 APIS		
	}
	@Test
	public void testApisPnrRuleRequestCreation2() {
		/*
		 * PNR and APIS has 2 common flights and 3 passengers
		 */
		ApisMessage apisMsg = ApisDataGenerator.createSimpleTestApisMesssage();
		PnrMessage pnrMsg = PnrDataGenerator.createTestPnrmessage(1L);
		
		//BEGIN:create common flights aand passengers
		Collection<Flight> apisFlights = apisMsg.getFlights();
		Collection<Passenger> apisFlt1Passengers = apisFlights.iterator().next().getPassengers();
		Pnr pnr = pnrMsg.getPnrs().iterator().next();
		for(Flight fl:apisFlights){
			pnr.getFlights().add(fl);
		}
		//add 3 passengers
		for(Passenger p:apisFlt1Passengers){
			pnr.getPassengers().add(p);
		}
		//END:create common flights aand passengers
		
		RuleServiceRequest request = TargetingServiceUtils
						.createPnrApisRequest(Arrays.asList(apisMsg), Arrays.asList(pnrMsg));
		Collection<?> reqObjects = request.getRequestObjects();
		assertNotNull(reqObjects);		
		assertEquals(65, reqObjects.size());//48 PNR + 14 APIS + 3 common passenger links	
	}

}
