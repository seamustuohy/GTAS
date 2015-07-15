package gov.gtas.rule.builder;

import static org.junit.Assert.*;
import static gov.gtas.rule.builder.RuleBuilderTestUtils.*;
import java.text.ParseException;

import gov.gtas.model.udr.UdrRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DrlRuleFileBuilderTest {
    private DrlRuleFileBuilder testTarget;
	@Before
	public void setUp() throws Exception {
		testTarget = new DrlRuleFileBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ParseException{
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule();
		testTarget.addRule(udrRule);
		String result = testTarget.build();
		System.out.println(result);
		verifyDrl(result);
	}
	public static void verifyDrl(String drl){
		String target = "rule \""+UDR_RULE_TITLE+":"+ENGINE_RULE_INDX1+"\"";
		assertTrue(drl.indexOf(target) > 0);
	}

}
