package gov.gtas.svc;

import static gov.gtas.rule.builder.RuleBuilderTestUtils.DOC_FLIGHT_CRITERIA_RULE_INDX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.lookup.PassengerTypeCode;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.rule.builder.DrlRuleFileBuilder;
import gov.gtas.rule.builder.RuleBuilderTestUtils;
import gov.gtas.svc.util.TargetingServiceUtils;
import gov.gtas.testdatagen.ApisDataGenerator;

import java.text.ParseException;

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
	public static final String UDR_RULE_AUTHOR="adelorie";

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
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule(UDR_RULE_AUTHOR, DOC_FLIGHT_CRITERIA_RULE_INDX);
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
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule(UDR_RULE_AUTHOR, 2);
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
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule(UDR_RULE_AUTHOR, 3);
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
}
