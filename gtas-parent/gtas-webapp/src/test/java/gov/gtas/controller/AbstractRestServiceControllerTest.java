package gov.gtas.controller;

import gov.gtas.config.WebAppInitializer;
import gov.gtas.controller.config.TestMvcRestServiceWebConfig;
import gov.gtas.controller.config.TestRestServiceConfig;
import gov.gtas.model.Role;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.ValueObject;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestMvcRestServiceWebConfig.class,
		TestRestServiceConfig.class })
@WebAppConfiguration
public class AbstractRestServiceControllerTest {

	protected MockMvc mockMvc;

	@Inject
	protected WebApplicationContext webApplicationContext;

	@Inject
	protected UserService userServiceMock;

	@Inject
	protected RulePersistenceService rulePersistenceServiceMock;

	@Before
	public void setup() {
		Mockito.reset(userServiceMock);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
	}

	public RulePersistenceService getRulePersistenceServiceMock() {
		return rulePersistenceServiceMock;
	}

	public void setRulePersistenceServiceMock(
			RulePersistenceService rulePersistenceServiceMock) {
		this.rulePersistenceServiceMock = rulePersistenceServiceMock;
	}

	protected static final String USER_ID = "user123";
	protected static final String USER_FIRSTNAME = "john";
	protected static final String USER_LASTNAME = "Doe";
	protected static final String USER_PASSWORD = "any";

	protected static final Integer ROLE_ID = 123;
	protected static final String ROLE_DESCRIPTION = "administrator";

	protected User getTestLoggedInUser() {

		Role role = new Role();
		role.setRoleId(ROLE_ID);
		role.setRoleDescription(ROLE_DESCRIPTION);

		User user = new User();
		user.setUserId(USER_ID);
		user.setUserRole(role);
		user.setFirstName(USER_FIRSTNAME);
		user.setFirstName(USER_LASTNAME);
		user.setPassword(USER_PASSWORD);

		return user;
	}

	protected UdrSpecification getUdrSpecification() {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");

		List<QueryEntity> rules = new LinkedList<QueryEntity>();

		QueryTerm trm = new QueryTerm("Pax", "embarkationDate", "EQUAL",
				new ValueObject(new Date()));
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "EQUAL", new ValueObject(
				"Jones")));

		// QueryObject queryObjectEmbedded = new QueryObject();
		// queryObjectEmbedded.setCondition("AND");
		//
		// List<QueryEntity> rules2 = new LinkedList<QueryEntity>();
		//
		// QueryTerm trm2 = new QueryTerm("Pax", "embarkation.name", "IN",
		// new ValueObject("String", new String[] { "DBY", "PKY", "FLT" }));
		// rules2.add(trm2);
		// rules2.add(new QueryTerm("Pax", "debarkation.name", "EQUAL",
		// new ValueObject("IAD")));
		// queryObjectEmbedded.setRules(rules2);
		//
		// rules.add(queryObjectEmbedded);

		queryObject.setRules(rules);

		UdrSpecification resp = new UdrSpecification(queryObject, new MetaData(
				"Hello Rule 1", "This is a test", new Date(), "jpjones"));
		return resp;
	}

}
