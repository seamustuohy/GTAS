package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.RuleServiceRequest;
import gov.cbp.taspd.gtas.error.RuleServiceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleRepositoryTest {
	private RuleService testTarget;

	@Before
	public void setUp() throws Exception {
		testTarget = new RuleServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDefaultRequest() {
      testTarget.invokeRuleset(0, new RuleServiceRequest() {
	     });
    }

	@Test(expected=RuleServiceException.class)
	public void testNullRequest() {
      testTarget.invokeRuleset(0, null);
    }
}
