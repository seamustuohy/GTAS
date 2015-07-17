package gov.gtas.rule.builder;

import static gov.gtas.rule.builder.RuleBuilderTestUtils.ENGINE_RULE_INDX1;
import static gov.gtas.rule.builder.RuleBuilderTestUtils.UDR_RULE_TITLE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.rule.RuleUtils;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

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
	public void testSimpleRuleGenerationAndCompilation() {
		try{
		UdrRule udrRule = RuleBuilderTestUtils.createSimpleUdrRule();
		testTarget.addRule(udrRule);
		String result = testTarget.build();
		System.out.println(result);
		verifyDrl(result);
		testKnowledgeBaseTest(result);
		}catch (Exception ex){
			ex.printStackTrace();
			fail("Not expecting exception.");
		}
	}
	public static void verifyDrl(String drl){
		String target = "rule \""+UDR_RULE_TITLE+":"+ENGINE_RULE_INDX1+"\"";
		assertTrue(drl.indexOf(target) > 0);
	}
    private void testKnowledgeBaseTest(String testDrl) throws IOException, ClassNotFoundException{
		KieBase kbase = RuleUtils.createKieBaseFromDrlString(testDrl);
    	byte[] blob = RuleUtils.convertKieBaseToBytes(kbase);
    	assertNotNull("ERROR - KieBase blob is null", blob);
    	byte[] blobCopy = Arrays.copyOf(blob, blob.length);
    	kbase = RuleUtils.convertKieBasefromBytes(blobCopy);
    	assertNotNull("ERROR - could not get KieBase from blob", kbase);
    	KieSession s = RuleUtils.createSession(kbase);
    	assertNotNull("Could not Create KieSession from copied KieBase", s);
    	s.dispose();
    	
    }
}
