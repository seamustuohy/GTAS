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
	public void testAddRule() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		Rule r = testGenUtils.createRuleNoCondition(RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);

		// read the rule back
		Rule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
	}

	@Test()
	public void testAddRuleWithConditions() {
		final String RULE_DESCRIPTION = "This is a Rule with conditions";
		Rule r = testGenUtils.createRuleWithOneCondition(RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);
		List<RuleCond> conditions = rsav.getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected one condition", 1, conditions.size());

		// read the rule back
		Rule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
		conditions = rsav.getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected one condition", 1, conditions.size());
	}

	@Test()
	public void testUpdateMetaData() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		Rule r = testGenUtils.createRuleNoCondition(RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);

		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);

		// modify meta and update
		meta.setDescription("new");
		testTarget.update(rsav, RuleServiceDataGenUtils.TEST_USER1_ID);

		// read the rule back
		Rule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);

		// check that the version has been updated by 1
		assertEquals(new Long(rsav.getVersion() + 1), readRule.getVersion());

		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());
	}

	@Test()
	public void testUpdateRuleConditions() {
		final String RULE_DESCRIPTION = "This is a Rule with conditions";
		Rule r = testGenUtils.createRuleWithOneCondition(RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);
		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		RuleMeta meta = rsav.getMetaData();
		assertNotNull(meta);
		List<RuleCond> conditions = rsav.getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected one condition", 1, conditions.size());

		// read the rule back
		Rule readRule = testTarget.findById(rsav.getId());
		assertNotNull(readRule);
		assertNotNull(readRule.getMetaData());
		assertEquals(meta, readRule.getMetaData());

		// add a condition
		rsav.addConditionToRule(testGenUtils.createCondition(2,
				EntityLookupEnum.Flight,
				EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME,
				OperatorCodeEnum.EQUAL, "DBY"));
		rsav = testTarget.update(rsav, RuleServiceDataGenUtils.TEST_USER1_ID);
		conditions = rsav.getRuleConds();
		assertNotNull(conditions);
		assertEquals("Expected two conditions", 2, conditions.size());
	}

	@Test()
	public void testDeleteRule() {
		final String RULE_DESCRIPTION = "This is a Simple Rule";
		Rule r = testGenUtils.createRuleNoCondition(RULE_DESCRIPTION,
				YesNoEnum.Y);
		Rule rsav = testTarget.create(r, RuleServiceDataGenUtils.TEST_USER1_ID);

		assertNotNull(rsav);
		long id = rsav.getId();
		assertTrue(id > 0);
		Rule deletedRule = testTarget.delete(id,
				RuleServiceDataGenUtils.TEST_USER1_ID);
		assertEquals(YesNoEnum.Y, deletedRule.getDeleted());

		// read back all the rules and make sure that this rule is not present
		List<Rule> allRules = testTarget.findAll();
		for (Rule rl : allRules) {
			if (rl.getId() == id) {
				fail("deleted rule is fetched by RulePersistenceService.findAll()");
			}
		}
	}
}
