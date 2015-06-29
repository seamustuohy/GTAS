package gov.gtas.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.EntityLookupEnum;
import gov.gtas.model.udr.OperatorCodeEnum;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.YesNoEnum;
import gov.gtas.services.udr.RulePersistenceService;
import gov.gtas.test.util.RuleServiceDataGenUtils;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
public class RulePersistenceServiceTest {

	@Autowired
	private RulePersistenceService testTarget;
	@Autowired
	private UserService userService;

	private RuleServiceDataGenUtils testGenUtils;
	
	@Before
	public void setUp() throws Exception {
		testGenUtils = new RuleServiceDataGenUtils(userService, testTarget);
		testGenUtils.initUserData();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test()
	public void testAddUdrRuleNoChild() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		String testRuleTitle = testGenUtils.generateTestRuleTitle(1);
		UdrRule r = testGenUtils.createUdrRule(testRuleTitle,RULE_DESCRIPTION,
				YesNoEnum.Y);
		UdrRule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);

		// read the rule back
		UdrRule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
	}
	@Test()
	public void testFetchUdrRuleByTitleAndAuthor() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		String testRuleTitle = testGenUtils.generateTestRuleTitle(2);
		UdrRule r = testGenUtils.createUdrRule(testRuleTitle, RULE_DESCRIPTION,
				YesNoEnum.Y);
		UdrRule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);

		// read the rule back
		UdrRule readRule = testTarget.findByTitleAndAuthor(testRuleTitle, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(String.format("Could not get Rule with title='%s', author ='%s'", testRuleTitle, RuleServiceDataGenUtils.TEST_USER1_ID), readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
	}
	@Test()
	public void testUpdateUdrRuleMetaData() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		String testRuleTitle = testGenUtils.generateTestRuleTitle(3);
		UdrRule r = testGenUtils.createUdrRule(testRuleTitle, RULE_DESCRIPTION,
				YesNoEnum.Y);
		r = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		UdrRule rsav = testTarget.findById(r.getId());
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);

		//save the version
		long savedVersion = rsav.getVersion();
		// modify meta and update
		meta.setDescription("This is a Simple Rule - Updated");
		testTarget.update(rsav, RuleServiceDataGenUtils.TEST_USER1_ID);

		// read the rule back
		UdrRule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);

		// check that the version has been updated by 1
		assertEquals(new Long(savedVersion+1), readRule.getVersion());

		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
	}


	@Test()
	public void testAddUdrRuleWithChildRule() {
		final String RULE_DESCRIPTION = "This is a UDR Rule with children";
		String testRuleTitle = testGenUtils.generateTestRuleTitle(4);
		UdrRule r = testGenUtils.createUdrRule(testRuleTitle, RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule engineRule = testGenUtils.createRuleWithOneCondition(r, 1);
		r.addEngineRule(engineRule);
		UdrRule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);
		List<Rule> engineRules = rsav.getEngineRules();
		assertNotNull(engineRules);
		assertEquals(1, engineRules.size());
		Rule er = engineRules.get(0);
		List<RuleCond> conditions = er.getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected one condition", 1, conditions.size());

		// read the rule back
		UdrRule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
		engineRules = rsav.getEngineRules();
		assertNotNull(engineRules);
		assertEquals(1, engineRules.size());
		er = engineRules.get(0);
		conditions = er.getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected one condition", 1, conditions.size());
	}

	@Test()
	public void testRuleWithMultipleConditions() {
		final String RULE_DESCRIPTION = "This is a Rule with conditions";
		String testRuleTitle = testGenUtils.generateTestRuleTitle(5);
		UdrRule r = testGenUtils.createUdrRule(testRuleTitle, RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule engineRule = testGenUtils.createRuleWithOneCondition(r, 1);
		engineRule.addConditionToRule(testGenUtils.createCondition(2,
				EntityLookupEnum.Flight,
				EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME,
				OperatorCodeEnum.EQUAL, "DBY"));

		r.addEngineRule(engineRule);
		UdrRule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);
		List<RuleCond> conditions = rsav.getEngineRules().get(0).getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected two condition", 2, conditions.size());

		// read the rule back
		UdrRule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
		conditions = readRule.getEngineRules().get(0).getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected two conditions", 2, conditions.size());
	}

	@Test()
	public void testDeleteRule() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		String testRuleTitle = testGenUtils.generateTestRuleTitle(6);
		UdrRule r = testGenUtils.createUdrRule(testRuleTitle, RULE_DESCRIPTION,
				YesNoEnum.Y);
		UdrRule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);

		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		UdrRule deletedRule = testTarget.delete(id,
				RuleServiceDataGenUtils.TEST_USER1_ID);
		assertEquals(YesNoEnum.Y, deletedRule.getDeleted());

		//read the rule back and make sure it is disabled
		UdrRule readRule = testTarget.findById(rsav.getId());
		assertEquals(YesNoEnum.Y, readRule.getDeleted());
		assertEquals(YesNoEnum.N, readRule.getMetaData().getEnabled());
		
		// read back all the rules and make sure that this rule is not present
		List<UdrRule> allRules = testTarget.findAll();
		for (UdrRule rl : allRules) {
			if (rl.getId() == id) {
				fail("deleted rule is fetched by RulePersistenceService.findAll()");
			}
		}
	}
}
