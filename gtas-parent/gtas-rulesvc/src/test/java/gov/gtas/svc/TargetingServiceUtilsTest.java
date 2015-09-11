package gov.gtas.svc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.svc.util.TargetingServiceUtils;
import gov.gtas.testdatagen.ApisDataGenerator;
import gov.gtas.testdatagen.PnrDataGenerator;

public class TargetingServiceUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

	/**
	 * 2pnr+2flt+6pass+6doc+3addr+2email+2phone+2ff+2cc+2agency + (6+3+ 2*5)links = 48   
	 */
    @Test
    public void testPnrRuleRequestCreation() {
        Pnr msg = PnrDataGenerator.createTestPnr(1L);
        RuleServiceRequest request = TargetingServiceUtils.createPnrRequest(msg);
        Collection<?> reqObjects = request.getRequestObjects();
        assertNotNull(reqObjects);
        assertEquals(28, reqObjects.size());

        msg = PnrDataGenerator.createTestPnr2(1L);
        request = TargetingServiceUtils.createPnrRequest(msg);
        reqObjects = request.getRequestObjects();
        assertNotNull(reqObjects);
        assertEquals(20, reqObjects.size());
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
		Pnr pnr = PnrDataGenerator.createTestPnr(1L);
		RuleServiceRequest request = TargetingServiceUtils
						.createPnrApisRequest(Arrays.asList(apis), Arrays.asList(pnr));
		Collection<?> reqObjects = request.getRequestObjects();
		assertNotNull(reqObjects);		
		assertEquals(42, reqObjects.size());//28 PNR + 14 APIS		
	}
	
	@Test
	public void testApisPnrRuleRequestCreation2() {
		/*
		 * PNR and APIS has 2 common flights and 3 passengers
		 */
		ApisMessage apisMsg = ApisDataGenerator.createSimpleTestApisMesssage();
		Pnr pnr = PnrDataGenerator.createTestPnr(1L);
		
		//BEGIN:create common flights aand passengers
		Collection<Flight> apisFlights = apisMsg.getFlights();
		Collection<Passenger> apisFlt1Passengers = apisFlights.iterator().next().getPassengers();
		for(Flight fl:apisFlights){
			pnr.getFlights().add(fl);
		}
		//add 3 passengers
		for(Passenger p:apisFlt1Passengers){
			pnr.getPassengers().add(p);
		}
		//END:create common flights aand passengers
		
		RuleServiceRequest request = TargetingServiceUtils
						.createPnrApisRequest(Arrays.asList(apisMsg), Arrays.asList(pnr));
		Collection<?> reqObjects = request.getRequestObjects();
		assertNotNull(reqObjects);		
		assertEquals(45, reqObjects.size());//28 PNR + 14 APIS + 3 common passenger links	
	}
}
