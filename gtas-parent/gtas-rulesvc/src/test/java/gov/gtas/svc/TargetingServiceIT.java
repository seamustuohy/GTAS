package gov.gtas.svc;

import static gov.gtas.rule.builder.RuleBuilderTestUtils.DOC_FLIGHT_CRITERIA_RULE_INDX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.lookup.PassengerTypeCode;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.rule.RuleServiceResult;
import gov.gtas.rule.builder.DrlRuleFileBuilder;
import gov.gtas.rule.builder.RuleBuilderTestUtils;
import gov.gtas.testdatagen.ApisDataGenerator;

import java.text.ParseException;
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
 * Unit tests for the TargetingService using spring support and Mockito.
 * 
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RuleServiceConfig.class)
@TransactionConfiguration(defaultRollback = true)
public class TargetingServiceIT {

	@Autowired
	TargetingService targetingService;

//	@Autowired
//	ApisDataGenerator apisDataGenerator;

	@Resource
	private ApisMessageRepository apisMessageRepository;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Transactional
	public void testDataGeneration() {
		ApisMessage msg = ApisDataGenerator.createSimpleTestApisMesssage();
		assertNotNull(msg);
		assertNotNull(msg.getId());
		assertEquals(2, msg.getFlights().size());
		Flight flight = msg.getFlights().iterator().next();
		assertEquals(3, flight.getPassengers().size());
		Passenger pax = flight.getPassengers().iterator().next();
		assertTrue(pax.getPassengerType().equals(PassengerTypeCode.P.name()));
		assertNotNull("Pax ID is null", pax.getId());
		assertEquals(1, pax.getDocuments().size());
		Document doc = pax.getDocuments().iterator().next();
		assertNotNull(doc.getId());
		assertNotNull("Passenger is null", doc.getPassenger());
		assertNotNull("Passenger ID is null", doc.getPassenger().getId());
		assertEquals(ApisDataGenerator.DOCUMENT_NUMBER, doc.getDocumentNumber());
	}

	@Test
	@Transactional
	public void testApisRuleExecution1() throws ParseException {
		ApisMessage msg = ApisDataGenerator.createSimpleTestApisMesssage();
		DrlRuleFileBuilder drlBuilder = new DrlRuleFileBuilder();
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule(DOC_FLIGHT_CRITERIA_RULE_INDX);
		String drlRules = drlBuilder.addRule(udrRule).build();
		System.out.println(drlRules);
		RuleServiceRequest request = TargetingServiceUtils
				.createApisRequest(msg);
		RuleServiceResult result = targetingService.applyRules(request,
				drlRules);
		assertNotNull(result);
		assertEquals("Expected 1 hit", 1, result.getResultList().size());
		RuleHitDetail res = (RuleHitDetail) (result.getResultList().get(0));
		assertNotNull("passenger ID in result is null", res.getPassengerId());
		assertEquals("Expected passenger with id = 11", 11L,
				res.getPassengerId());
	}

	@Test
	@Transactional
	public void testApisRuleExecution2() throws ParseException {
		ApisMessage msg = ApisDataGenerator.createSimpleTestApisMesssage();
		DrlRuleFileBuilder drlBuilder = new DrlRuleFileBuilder();
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule(2);
		String drlRules = drlBuilder.addRule(udrRule).build();
		System.out.println(drlRules);
		RuleServiceRequest request = TargetingServiceUtils
				.createApisRequest(msg);
		RuleServiceResult result = targetingService.applyRules(request,
				drlRules);
		assertNotNull(result);
		assertEquals("Expected 2 hits", 2, result.getResultList().size());
		RuleHitDetail res = (RuleHitDetail) (result.getResultList().get(0));
		assertNotNull("passenger ID in result is null", res.getPassengerId());
		assertTrue(
				"Hit Passenger id mismatch",
				new Long(44L).equals(res.getPassengerId())
						|| new Long(66L).equals(res.getPassengerId()));
	}

	@Test
	@Transactional
	public void testApisRuleExecution3() throws ParseException {
		// select all passengers in a flight
		ApisMessage msg = ApisDataGenerator.createSimpleTestApisMesssage();
		DrlRuleFileBuilder drlBuilder = new DrlRuleFileBuilder();
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule(3);
		String drlRules = drlBuilder.addRule(udrRule).build();
		System.out.println(drlRules);
		RuleServiceRequest request = TargetingServiceUtils
				.createApisRequest(msg);
		RuleServiceResult result = targetingService.applyRules(request,
				drlRules);
		assertNotNull(result);
		assertEquals("Expected 3 hits", 3, result.getResultList().size());
		RuleHitDetail res = (RuleHitDetail) (result.getResultList().get(0));
		assertNotNull("passenger ID in result is null", res.getPassengerId());
		assertTrue(
				"Hit Passenger id mismatch",
				new Long(44L).equals(res.getPassengerId())
						|| new Long(55L).equals(res.getPassengerId())
						|| new Long(66L).equals(res.getPassengerId()));
	}

	// need to add test data
//	@Test
//	@Transactional
	public void testApisMessage() {
		Iterable<ApisMessage> messages = apisMessageRepository.findAll();
		assertNotNull(messages);
		int count = 0;
		for (ApisMessage msg : messages) {
			int reqObjCount = 0;
			++count;
			System.out.println("Processsing APIs message:" + msg.getId());
			Set<Flight> flights = msg.getFlights();
			if(flights.size() > 0){
				for (Flight flt : flights) {
					reqObjCount += verifyPassengers(flt, flt.getPassengers());
				}
				RuleServiceRequest req = TargetingServiceUtils
						.createApisRequest(msg);
				assertEquals(reqObjCount, req.getRequestObjects().size());
			} else {
				System.out.println("********???Message has no flights:"+ msg.getId());
			}
		}
		assertTrue("There are no API messsages", count > 0);

		System.out.println("APIs message count = " + count);
	}

	private int verifyPassengers(Flight flt, Set<Passenger> passengers) {
		int travCount = 0;
		int paxCount = 0;
		int crewCount = 0;
		int docCount = 0;
		for (Passenger pax : passengers) {
			++travCount;
			String type = pax.getPassengerType();
			if (type.equals(PassengerTypeCode.P.name())) {
				++paxCount;
			} else if (type.equals(PassengerTypeCode.C.name())) {
				++crewCount;
			}
			Set<Document> docs = pax.getDocuments();
			assertNotNull(docs);
			assertTrue(docs.size() > 0);
			docCount += docs.size();
			for (Document doc : docs) {
				assertNotNull(doc.getId());
				assertNotNull("Passenger reference is null in Document",
						doc.getPassenger());
				assertNotNull("Passenger ID is null in Document Passenger",
						doc.getPassenger().getId());
			}
		}
		assertTrue("Flight has no passengers:" + flt.getFlightNumber(),
				travCount > 0);
		assertTrue("Flight passenger+crew <> passenger:" + flt.getFlightNumber(),
				paxCount+crewCount == travCount);
		System.out.println("Flight:" + flt.getFlightNumber()
				+ " - passenger count = " + travCount + ", Passenger count ="
				+ paxCount);
		return travCount + docCount + 1;// passengers+documents_flight
	}
}
