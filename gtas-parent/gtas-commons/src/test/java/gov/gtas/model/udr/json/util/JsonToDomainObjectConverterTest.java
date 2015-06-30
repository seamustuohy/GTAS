package gov.gtas.model.udr.json.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import gov.gtas.model.User;
import gov.gtas.model.udr.ConditionValueTypeEnum;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.EntityLookupEnum;
import gov.gtas.model.udr.OperatorCodeEnum;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.YesNoEnum;
import gov.gtas.model.udr.json.QueryConditionEnum;
import gov.gtas.model.udr.json.UdrSpecification;

import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonToDomainObjectConverterTest {
	private static final String UDR_TITLE = "test";
	private static final String UDR_DESCRIPTION = "test_descr";
	private static final String UDR_AUTHOR = "jpjones";
	private static final Long UDR_ID = new Long(251);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicWithId() {
		Date startDate = new Date();
		Date endDate = new Date(System.currentTimeMillis() + 86400000L);//one day in the future
		
		UdrSpecification spec = buildUdrSpec(UDR_ID, startDate, endDate, false,
				UDR_AUTHOR);
		UdrRule rule = null;
		try {
			User author = new User();
			author.setUserId(spec.getSummary().getAuthor());
			
			rule = JsonToDomainObjectConverter.createUdrRuleFromJson(spec,
					author);
			
			assertNotNull("Rule blob is null", rule.getUdrConditionObject());
			assertEquals(UDR_AUTHOR, rule.getAuthor().getUserId());
			assertEquals("rule id is null", UDR_ID, rule.getId());
			assertEquals("meta data id is null",UDR_ID, rule.getMetaData().getId());
			assertNotNull(rule);
			RuleMeta meta = rule.getMetaData();
			verifyMeta(meta, UDR_TITLE, UDR_DESCRIPTION, startDate, endDate, false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Not expecting exception");
		}

	}

	@Test
	public void testBasicWithNullId() {
		Date startDate = new Date();
		Date endDate = new Date(System.currentTimeMillis() + 86400000L);//one day in the future
		
		UdrSpecification spec = buildUdrSpec(null, startDate, endDate, false,
				UDR_AUTHOR);
		UdrRule rule = null;
		try {
			User author = new User();
			author.setUserId(spec.getSummary().getAuthor());
			
			rule = JsonToDomainObjectConverter.createUdrRuleFromJson(spec,
					author);
			assertNotNull("Rule blob is null", rule.getUdrConditionObject());
			assertEquals(UDR_AUTHOR, rule.getAuthor().getUserId());
			assertNull("rule id is  not null", rule.getId());
			assertNull("meta data id is not null", rule.getMetaData().getId());
			assertNotNull(rule);
			RuleMeta meta = rule.getMetaData();
			verifyMeta(meta, UDR_TITLE, UDR_DESCRIPTION, startDate, endDate, false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Not expecting exception");
		}

	}
//	@Test
//	public void testBasicWithNullMeta() {
//		Date startDate = new Date();
//		Date endDate = new Date(System.currentTimeMillis() + 86400000L);//one day in the future
//		
//		UdrSpecification spec = buildUdrSpec(UDR_ID);
//		UdrRule rule = null;
//		try {
//			
//			rule = JsonToDomainObjectConverter.createUdrRuleFromJson(spec,
//					null);
//			assertNotNull("Rule blob is null", rule.getUdrConditionObject());
//			assertEquals(UDR_AUTHOR, rule.getAuthor().getUserId());
//			assertNull("rule id is  not null", rule.getId());
//			assertNull("meta data id is not null", rule.getMetaData().getId());
//			assertNotNull(rule);
//			RuleMeta meta = rule.getMetaData();
//			verifyMeta(meta, UDR_TITLE, UDR_DESCRIPTION, startDate, endDate, false);
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//			fail("Not expecting exception");
//		}
//
//	}
	
	private void verifyMeta(RuleMeta meta, String title, String descr,
			Date startDate, Date endDate, boolean enabled) {
		assertNotNull(meta);
		assertEquals(title, meta.getTitle());
		assertEquals(descr, meta.getDescription());
		assertEquals(startDate, meta.getStartDt());
		assertEquals(endDate, meta.getEndDt());
		assertEquals(enabled, meta.getEnabled() == YesNoEnum.Y ? true : false);
	}

	private UdrSpecification buildUdrSpec(Long id) {
		return buildUdrSpec(id,null,null,false,null,false,true);
	}

	private UdrSpecification buildUdrSpec(Long id, Date st, Date nd, boolean enabled,
			String author) {
		return buildUdrSpec(id,st,nd,enabled,author,false,false);
	}
	private UdrSpecification buildUdrSpec(Long id, Date st, Date nd, boolean enabled,
			String author, boolean noQuery, boolean noSummary) {
		
		UdrSpecificationBuilder builder = null;
		if(noSummary){
			builder = new UdrSpecificationBuilder(id);			
		}else{
			builder = new UdrSpecificationBuilder(id,
				QueryConditionEnum.OR);
			// add terms and then another query object
			builder.addTerm(EntityLookupEnum.Pax,
					EntityAttributeConstants.PAX_ATTTR_DEBARKATION_AIRPORT_NAME,
					ConditionValueTypeEnum.STRING, OperatorCodeEnum.EQUAL,
					new String[] { "IAD" });
			builder.addNestedQueryObject(QueryConditionEnum.AND);
			builder.addTerm(EntityLookupEnum.Pax,
					EntityAttributeConstants.PAX_ATTTR_LAST_NAME,
					ConditionValueTypeEnum.STRING, OperatorCodeEnum.EQUAL,
					new String[] { "Jones" });
			builder.addTerm(EntityLookupEnum.Pax,
					EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME,
					ConditionValueTypeEnum.STRING, OperatorCodeEnum.EQUAL,
					new String[] { "DBY" });
		}
		if(!noSummary){
		   builder.addMeta(UDR_TITLE, UDR_DESCRIPTION, st, nd, enabled, author);
		}
		return builder.build();

	}
}
