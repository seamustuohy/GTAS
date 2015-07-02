package gov.gtas.svc;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import gov.gtas.config.RuleServiceConfig;
import gov.gtas.model.Role;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;
import gov.gtas.services.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RuleServiceConfig.class)
@TransactionConfiguration(defaultRollback = true)
public class UdrServiceIT {
	
    @Autowired
    UdrService udrService;
    
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
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId());
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
	}
	@Test
	@Transactional
	public void testFetchSummaryList() {
		User user = createUser();
		UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec(user.getUserId());
		JsonServiceResponse resp = udrService.createUdr(user.getUserId(), spec);
		assertEquals(JsonServiceResponse.SUCCESS_RESPONSE, resp.getStatus());
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
