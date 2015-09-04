package gov.gtas.controller.udr;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import gov.gtas.common.WebAppConfig;
import gov.gtas.controller.UdrManagementController;
import gov.gtas.controller.config.TestMvcRestServiceWebConfig;
import gov.gtas.controller.util.UdrBuilderDataUtils;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.svc.RuleManagementService;
import gov.gtas.svc.UdrService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
/**
 * End to end Integration tests for UDR.
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestMvcRestServiceWebConfig.class,
		WebAppConfig.class })
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
public class UdrBuilderControllerIT {
    private static final String TEST_USER = "adelorie";
    private static final String TEST_UDR_TITLE = "TEST_TITLE5634";
    private static final String TEST_DESCRIPTION = "TREST_DESCRIPTION";
    
	private MockMvc mockMvc;

	@Autowired
	private UdrService udrService;
	
	@Autowired
	private RuleManagementService ruleManagementService;

	private UdrManagementController udrController;
	
	@Before
	public void setUp() {
		udrController = new UdrManagementController();
		ReflectionTestUtils.setField(udrController,
				"udrService", udrService);
		ReflectionTestUtils.setField(udrController, "ruleManagementService",
				ruleManagementService);

		mockMvc = MockMvcBuilders
				.standaloneSetup(udrController)
				.defaultRequest(
						get("/").contextPath("/gtas").accept(
								MediaType.APPLICATION_JSON)).build();
	}

	@Test
	@Transactional
	public void getUDR_ShouldReturnFoundUdrSpecification() throws Exception {
		UdrSpecification udrSpec = new UdrBuilderDataUtils().createSimpleSpec(TEST_UDR_TITLE, TEST_DESCRIPTION, TEST_USER);
		udrService.createUdr(TEST_USER, udrSpec);

		mockMvc.perform(get("/gtas/udr/" + TEST_USER+"/"+TEST_UDR_TITLE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON+";charset=UTF-8"))
				.andExpect(jsonPath("$.summary.title", is(TEST_UDR_TITLE)))
				.andExpect(jsonPath("$.summary.description", is(TEST_DESCRIPTION)));

	}

}
