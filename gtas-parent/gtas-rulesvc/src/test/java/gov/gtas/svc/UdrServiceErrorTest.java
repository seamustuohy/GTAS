package gov.gtas.svc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonServiceException;
import gov.gtas.error.CommonValidationException;
import gov.gtas.error.udr.UdrErrorConstants;
import gov.gtas.model.User;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.util.JsonToDomainObjectConverter;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;
import gov.gtas.querybuilder.mappings.PassengerMapping;
import gov.gtas.services.UserService;
import gov.gtas.services.udr.RulePersistenceService;
import gov.gtas.util.DateCalendarUtils;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UdrServiceErrorTest {
    private static final String TEST_JSON =
        	"{ \"details\": {"
              +"  \"@class\": \"gov.gtas.model.udr.json.QueryObject\","
               +" \"condition\": \"OR\","
               +" \"rules\": ["
                 +  " {"
                   +"   \"@class\": \"QueryTerm\","
                        + " \"entity\": \"%s\","
                        + " \"field\": \"%s\","
                        + " \"operator\": \"%s\","
                        + " \"type\": \"string\","
                        + " \"value\": [\"John\"]"
                   +" },"
                   + " {"
                        + " \"@class\": \"QueryTerm\","
                        + " \"entity\": \"Passenger\","
                        + " \"field\": \"lastName\","
                        + " \"operator\": \"EQUAL\","
                        + " \"type\": \"string\","
                        + " \"value\": [\"Jones\"]"
                    +"}"
                +"]"
            +"},"
            + " \"summary\": {"
                + " \"title\": \"Hello Rule 1\","
                + " \"description\": \"This is a test\","
                + " \"startDate\": \"%s\","
                + " \"endDate\": null,"
                +" \"author\": \"jpjones\","
                + " \"enabled\": false"
            + "}"
        +"}";
    private UdrService udrService;
    
    @Mock
    private RulePersistenceService mockRulePersistenceSvc;
    
    @Mock
    private UserService mockUserService;
    
    @Mock
    private RuleManagementService mockRuleManagementService;
    
	@Before
	public void setUp() throws Exception {
		udrService = new UdrServiceImpl();
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(udrService, "rulePersistenceService", mockRulePersistenceSvc);
		ReflectionTestUtils.setField(udrService, "userService", mockUserService);
		ReflectionTestUtils.setField(udrService, "ruleManagementService", mockRuleManagementService);
	}

	@After
	public void tearDown() throws Exception {
	}
    
	@Test
	public void testCreateBadUser() {
		String testUser = "foo";
		when(mockUserService.findById(testUser)).thenReturn(null);
		try{
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 spec.getSummary().setAuthor(testUser);
		 udrService.createUdr(testUser, spec);
		 fail("Expecting Exception");
		} catch (CommonServiceException cse){
			assertEquals(CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, cse.getErrorCode());
		}
		verify(mockUserService).findById(testUser);
	}

	@Test
	public void testCreateNullDetails() {
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 spec.setDetails(null);
		 String authorId = spec.getSummary().getAuthor();
		 User author = new User();
		 author.setUserId(authorId);
		when(mockUserService.findById(authorId)).thenReturn(author);
		try{
		 udrService.createUdr(authorId, spec);
		 fail("Expecting Exception");
		} catch (CommonServiceException cse){
			assertEquals(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, cse.getErrorCode());
		}
		verify(mockUserService).findById(authorId);
	}
	@Test
	public void testCreateNullSummary() {
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 String authorId = spec.getSummary().getAuthor();
		 User author = new User();
		 author.setUserId(authorId);
		 spec.setSummary(null);
		try{
		 udrService.createUdr(authorId, spec);
		 fail("Expecting Exception");
		} catch (CommonServiceException cse){
			assertEquals(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE, cse.getErrorCode());
		}
		verify(mockUserService, times(0)).findById(authorId);
	}
	@Test
	public void testCreateBadJsonCondition() {
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 String authorId = spec.getSummary().getAuthor();
		 User author = new User();
		 author.setUserId(authorId);
		 spec.getDetails().setCondition("foo");
		 when(mockUserService.findById(authorId)).thenReturn(author);
		try{
		 udrService.createUdr(authorId, spec);
		 fail("Expecting Exception");
		} catch (CommonValidationException cse){
			assertEquals(CommonErrorConstants.JSON_INPUT_VALIDATION_ERROR_CODE, cse.getErrorCode());
		}
		verify(mockUserService, times(1)).findById(authorId);
	}
	@Test
	public void testCreateBadJsonRules() {
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 String authorId = spec.getSummary().getAuthor();
		 User author = new User();
		 author.setUserId(authorId);
		 spec.getDetails().setRules(null);
		 when(mockUserService.findById(authorId)).thenReturn(author);
		try{
		 udrService.createUdr(authorId, spec);
		 fail("Expecting Exception");
		} catch (CommonServiceException cse){
			assertEquals(CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE, cse.getErrorCode());
		}
		verify(mockUserService, times(1)).findById(authorId);
	}
	@Test
	public void testCreateTodayDateOk() {
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 String authorId = spec.getSummary().getAuthor();
		 User author = new User();
		 author.setUserId(authorId);
		 try{
			 String today = DateCalendarUtils.formatJsonDate(new Date());
			 System.out.println(today);
			 spec.getSummary().setStartDate(DateCalendarUtils.parseJsonDate(today));
			 spec.getSummary().setEndDate(DateCalendarUtils.parseJsonDate(today));
		     UdrRule rule = JsonToDomainObjectConverter.createUdrRuleFromJson(
					spec, author);
		     rule.setId(1L);
             when(mockRulePersistenceSvc.create(any(UdrRule.class), any())).thenReturn(rule);
		     when(mockUserService.findById(authorId)).thenReturn(author);
		     udrService.createUdr(authorId, spec);
		} catch (Exception ex){
			ex.printStackTrace();
			fail("Not Expecting Exception");
		}
		verify(mockUserService, times(1)).findById(authorId);
		verify(mockRulePersistenceSvc).create(any(), any());
		verify(mockRulePersistenceSvc).findAll();
		verify(mockRuleManagementService).createKnowledgeBaseFromUdrRules(any(), any(), any());
		
	}
	@Test
	public void testCreateYesterdayDateError() {
		 UdrSpecification spec = UdrSpecificationBuilder.createSampleSpec();
		 String authorId = spec.getSummary().getAuthor();
		 User author = new User();
		 author.setUserId(authorId);
		 try{
			 String yesterday = DateCalendarUtils.formatJsonDate(new Date(System.currentTimeMillis() - 86400000L));
			 spec.getSummary().setStartDate(DateCalendarUtils.parseJsonDate(yesterday));
		     UdrRule rule = JsonToDomainObjectConverter.createUdrRuleFromJson(
					spec, author);
		     rule.setId(1L);
             when(mockRulePersistenceSvc.create(any(UdrRule.class), any())).thenReturn(rule);
		     when(mockUserService.findById(authorId)).thenReturn(author);
		     udrService.createUdr(authorId, spec);
		     fail("Expecting exception");
		  } catch (CommonServiceException cse){
				assertEquals(UdrErrorConstants.PAST_START_DATE_ERROR_CODE, cse.getErrorCode());		     
		  } catch (Exception ex){
				ex.printStackTrace();
				fail("Not Expecting Exception");
		  }
		  verify(mockUserService, times(0)).findById(authorId);
	}
	@Test
	public void testInvalidEntity() {
		String authorId = null;
		 try{
				ObjectMapper mapper = new ObjectMapper();
			    //de-serialize
				//add arbitrary offset to counteract Jackson GMT interpretation
				String startDate = DateCalendarUtils.formatJsonDate(new Date(System.currentTimeMillis()+864000000L));

				UdrSpecification testObj = mapper.readValue(
						String.format(TEST_JSON, "foo", "bar", "equal", startDate), 
						UdrSpecification.class);	
				assertNotNull(testObj);
				 authorId = testObj.getSummary().getAuthor();
				 User author = new User();
				 author.setUserId(authorId);
			     UdrRule rule = JsonToDomainObjectConverter.createUdrRuleFromJson(
						testObj, author);
			     rule.setId(1L);
	             when(mockRulePersistenceSvc.create(any(UdrRule.class), any())).thenReturn(rule);
			     when(mockUserService.findById(authorId)).thenReturn(author);
			     udrService.createUdr(authorId, testObj);
			     fail("Expecting exception");
		  } catch (CommonValidationException cve){
				assertEquals(CommonErrorConstants.JSON_INPUT_VALIDATION_ERROR_CODE, cve.getErrorCode());		     
		  } catch (Exception ex){
				ex.printStackTrace();
				fail("Not Expecting Exception");
		  }
		  verify(mockUserService, times(0)).findById(authorId);
	}
	@Test
	public void testInvalidAttribute() {
		String authorId = null;
		 try{
				ObjectMapper mapper = new ObjectMapper();
			    //de-serialize
				
				//add arbitrary offset to counteract Jackson GMT interpretation
				String startDate = DateCalendarUtils.formatJsonDate(new Date(System.currentTimeMillis()+864000000L));

				UdrSpecification testObj = mapper.readValue(
						String.format(TEST_JSON, "Pax", "bar", "equal", startDate), 
						UdrSpecification.class);	
				assertNotNull(testObj);
				 authorId = testObj.getSummary().getAuthor();
				 User author = new User();
				 author.setUserId(authorId);
			     UdrRule rule = JsonToDomainObjectConverter.createUdrRuleFromJson(
						testObj, author);
			     rule.setId(1L);
	             when(mockRulePersistenceSvc.create(any(UdrRule.class), any())).thenReturn(rule);
			     when(mockUserService.findById(authorId)).thenReturn(author);
			     udrService.createUdr(authorId, testObj);
			     fail("Expecting exception");
		  } catch (CommonValidationException cve){
				assertEquals(CommonErrorConstants.JSON_INPUT_VALIDATION_ERROR_CODE, cve.getErrorCode());		     
		  } catch (Exception ex){
				ex.printStackTrace();
				fail("Not Expecting Exception");
		  }
		  verify(mockUserService, times(0)).findById(authorId);
	}
	@Test
	public void testValidEntityAttribute() {
		String authorId = null;
		 try{
				ObjectMapper mapper = new ObjectMapper();
			    //de-serialize
				//add arbitrary offset to counteract Jackson GMT interpretation
				String startDate = DateCalendarUtils.formatJsonDate(new Date(System.currentTimeMillis()+864000000L));

				UdrSpecification testObj = mapper.readValue(
						String.format(TEST_JSON, EntityEnum.PASSENGER.getEntityName(), PassengerMapping.DEBARKATION.getFieldName(), "equal", startDate), 
						UdrSpecification.class);	
				assertNotNull(testObj);
				 authorId = testObj.getSummary().getAuthor();
				 User author = new User();
				 author.setUserId(authorId);
			     UdrRule rule = JsonToDomainObjectConverter.createUdrRuleFromJson(
						testObj, author);
			     rule.setId(1L);
	             when(mockRulePersistenceSvc.create(any(UdrRule.class), any())).thenReturn(rule);
			     when(mockUserService.findById(authorId)).thenReturn(author);
			     udrService.createUdr(authorId, testObj);
		  } catch (Exception ex){
				ex.printStackTrace();
				fail("Not Expecting Exception");
		  }
		  verify(mockUserService, times(1)).findById(authorId);
		  verify(mockRulePersistenceSvc, times(1)).create(any(UdrRule.class), any());
		  verify(mockRulePersistenceSvc).findAll();
		  verify(mockRuleManagementService).createKnowledgeBaseFromUdrRules(any(), any(), any());
	}
}
