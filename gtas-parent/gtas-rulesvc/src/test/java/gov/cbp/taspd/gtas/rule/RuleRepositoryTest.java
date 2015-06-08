package gov.cbp.taspd.gtas.rule;

import static org.junit.Assert.*;
import gov.cbp.taspd.gtas.bo.RuleServiceRequest;
import gov.cbp.taspd.gtas.error.RuleServiceException;
import gov.cbp.taspd.gtas.rule.RuleRunner;
import gov.cbp.taspd.gtas.rule.RuleService;
import gov.cbp.taspd.gtas.rule.RuleServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { RuleRunner.class, RuleServiceImpl.class })
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
