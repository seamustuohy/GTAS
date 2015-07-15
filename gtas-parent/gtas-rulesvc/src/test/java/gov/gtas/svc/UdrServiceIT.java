package gov.gtas.svc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.model.Role;
import gov.gtas.model.User;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrConstants;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.udr.json.JsonUdrListElement;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;

import java.util.LinkedList;
import java.util.List;

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
 * Integration tests for the UDR maanageement service.
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
@TransactionConfiguration(defaultRollback = true)
public class UdrServiceIT {
	private static final String RULE_TITLE1 = "Hello Rule 1";
	private static final String RULE_DESCRIPTION1 = "This is a test";
	private static final String RULE_TITLE2 = "Hello Rule 2";
	private static final String RULE_DESCRIPTION2 = "This is a test2";

	@Autowired
    UdrService udrService;
    
	@Autowired
    RulePersistenceService ruleService;

	@Autowired
    UserService userService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Transactional
	public void testCreateUdr() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
	}
	@Test
	@Transactional
	public void testCreateUdrWithSingleConditionAND() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		QueryObject details = spec.getDetails();
		details.setCondition("AND");
		List<QueryEntity> terms =  details.getRules();
		List<QueryEntity> newterms = new LinkedList<QueryEntity>();
		newterms.add(terms.get(0));
		details.setRules(newterms);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		Long id = Long.valueOf(resp.findResponseDetailValue(UdrConstants.UDR_ID_ATTRIBUTE_NAME));
		assertNotNull("The saved ID is null",id);
		UdrRule rule = ruleService.findById(id);
		assertNotNull(rule);
		List<Rule> engineRules = rule.getEngineRules();
		assertNotNull(engineRules);
		assertEquals(1, engineRules.size());
		List<RuleCond> conds =  engineRules.get(0).getRuleConds();
		assertNotNull(conds);
		assertEquals(1, conds.size());
	}
	@Test
	@Transactional
	public void testCreateUdrWithSingleConditionOR() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		QueryObject details = spec.getDetails();
		details.setCondition("OR");
		List<QueryEntity> terms =  details.getRules();
		List<QueryEntity> newterms = new LinkedList<QueryEntity>();
		newterms.add(terms.get(0));
		details.setRules(newterms);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		Long id = Long.valueOf(resp.findResponseDetailValue(UdrConstants.UDR_ID_ATTRIBUTE_NAME));
		assertNotNull("The saved ID is null",id);
		UdrRule rule = ruleService.findById(id);
		assertNotNull(rule);
		List<Rule> engineRules = rule.getEngineRules();
		assertNotNull(engineRules);
		assertEquals(1, engineRules.size());
		List<RuleCond> conds =  engineRules.get(0).getRuleConds();
		assertNotNull(conds);
		assertEquals(1, conds.size());
	}
	@Test
	@Transactional
	public void testFetchSummaryList() {
		User user = createUser();
		UdrSpecification spec1 = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		UdrSpecification spec2 = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE2, RULE_DESCRIPTION1);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec1);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		resp = udrService.createUdr(user.getUserId(), spec2);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		List<JsonUdrListElement> listResp = udrService.fetchUdrSummaryList(user.getUserId());
		assertNotNull(listResp);
		assertEquals(2, listResp.size());
	}
	@Test
	@Transactional
	public void testFetchUdrById() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		assertNotNull(resp.getResponseDetails());
		Long id = Long.valueOf(resp.getResponseDetails().get(0).getAttributeValue());       
		assertNotNull(id);
		UdrSpecification specFetched = udrService.fetchUdr(id);
		assertNotNull(specFetched);
		assertEquals(spec.getSummary().getTitle(), specFetched.getSummary().getTitle());
	}
	@Test
	@Transactional
	public void testFetchUdrByAuthorTitle() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		assertNotNull(resp.getResponseDetails());
		String title = resp.getResponseDetails().get(1).getAttributeValue();       
		assertEquals(RULE_TITLE1, title);
		UdrSpecification specFetched = udrService.fetchUdr(user.getUserId(), title);
		assertNotNull(specFetched);
		assertEquals(spec.getSummary().getTitle(), specFetched.getSummary().getTitle());
	}
	@Test
	@Transactional
	public void testUpdate() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		assertNotNull(resp.getResponseDetails());
		String title = resp.getResponseDetails().get(1).getAttributeValue();       
		assertEquals(RULE_TITLE1, title);
		UdrSpecification specFetched = udrService.fetchUdr(user.getUserId(), title);
		//specFetched.getSummary().setDescription(RULE_DESCRIPTION2);
		UdrSpecification updatedSpec = UdrSpecificationBuilder.createSampleSpec2(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION2);
		updatedSpec.setId(specFetched.getId());
		udrService.updateUdr(user.getUserId(), updatedSpec);
		specFetched = udrService.fetchUdr(user.getUserId(), title);		
		assertNotNull(specFetched);
		assertEquals(RULE_DESCRIPTION2, specFetched.getSummary().getDescription());
	}
	@Test
	@Transactional
	public void testDelete() {
		User user = createUser();
		UdrSpecification spec1 = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE1, RULE_DESCRIPTION1);
		UdrSpecification spec2 = UdrSpecificationBuilder.createSampleSpec(user.getUserId(), RULE_TITLE2, RULE_DESCRIPTION1);
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec1);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		Long id = Long.valueOf(resp.getResponseDetails().get(0).getAttributeValue());       

		resp = udrService.createUdr(user.getUserId(), spec2);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
		List<JsonUdrListElement> listResp = udrService.fetchUdrSummaryList(user.getUserId());
		assertNotNull(listResp);
		assertEquals(2, listResp.size());
		
		udrService.deleteUdr(user.getUserId(),id);
		
		listResp = udrService.fetchUdrSummaryList(user.getUserId());
		assertNotNull(listResp);
		assertEquals(1, listResp.size());
	}
   private User createUser(){
		String ROLE_NAME = "user";
		String USER_FNAME = "Patrick";
		String USER_LASTNAME = "Henry";
		String USER_ID = "phenry";

	   User usr = new User();
	   Role role = new Role();
	   role.setRoleDescription(ROLE_NAME);
	   //role.setRoleId(ROLE_ID);
	   usr.setUserRole(role);
	   usr.setFirstName(USER_FNAME);
	   usr.setLastName(USER_LASTNAME);
	   usr.setPassword("password");
	   usr.setUserId(USER_ID);
	   
		usr = userService.create(usr);

	   return usr;
   }
}
