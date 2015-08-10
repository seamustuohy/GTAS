package gov.gtas.controller.udr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gov.gtas.controller.AbstractRestServiceControllerIT;
import gov.gtas.controller.util.UdrBuilderDataUtils;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.repository.udr.UdrRuleRepository;
import gov.gtas.services.udr.RulePersistenceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UdrBuilderControllerIT extends AbstractRestServiceControllerIT {

	private MockMvc mockMvc;

	@Mock
	private RulePersistenceService rulePersistenceServiceMock;

    @Mock
    private UdrRuleRepository udrRuleRepositoryMock;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void getUDR_ShouldReturnFoundUdrSpecification() throws Exception {
		UdrSpecification udrSpec = new UdrBuilderDataUtils().createSimpleSpec();
		Long id = 1L;
	//	when(rulePersistenceServiceMock.findById(id)).thenReturn(udrSpec);

//		mockMvc.perform(get("/{id}", id).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(
//						content().contentType("application/json"))
//				.andExpect(jsonPath("id").value(id));

	}

}
