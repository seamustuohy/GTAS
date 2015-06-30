package gov.gtas.model.udr.json.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimple() {
		Date startDate = new Date();
		Date endDate = new Date(System.currentTimeMillis() + 86400L);
		Long id = new Long(251);
		UdrSpecification spec = buildUdrSpec(id, startDate, endDate, false,
				UDR_AUTHOR);
		UdrRule rule = null;
		try {
			User author = new User();
			author.setUserId(spec.getSummary().getAuthor());
			
			rule = JsonToDomainObjectConverter.createUdrRuleFromJson(spec,
					author);
			
			assertEquals(UDR_AUTHOR, rule.getAuthor().getUserId());
			assertEquals("rule id is null", id, rule.getId());
			assertEquals("meta data id is null",id, new Long(rule.getMetaData().getId()));
			assertNotNull(rule);
			RuleMeta meta = rule.getMetaData();
			verifyMeta(meta, UDR_TITLE, UDR_DESCRIPTION, startDate, endDate, false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Not expecting exception");
		}

	}

	private void verifyMeta(RuleMeta meta, String title, String descr,
			Date startDate, Date endDate, boolean enabled) {
		assertNotNull(meta);
		assertEquals(title, meta.getTitle());
		assertEquals(descr, meta.getDescription());
		assertEquals(startDate, meta.getStartDt());
		assertEquals(endDate, meta.getEndDt());
		assertEquals(enabled, meta.getEnabled() == YesNoEnum.Y ? true : false);
	}

	private UdrSpecification buildUdrSpec(Long id, Date st, Date nd, boolean enabled,
			String author) {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(id,
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
		builder.addMeta(UDR_TITLE, UDR_DESCRIPTION, st, nd, enabled, author);
		return builder.build();

	}
}
