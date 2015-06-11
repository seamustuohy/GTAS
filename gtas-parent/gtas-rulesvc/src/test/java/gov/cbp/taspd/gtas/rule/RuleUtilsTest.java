package gov.cbp.taspd.gtas.rule;

import static org.junit.Assert.*;

import java.io.IOException;

import gov.cbp.taspd.gtas.error.RuleServiceErrorHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

public class RuleUtilsTest {
    private static final String testDrl = 
    		"package gov.cbp.taspd.gtas.rule;"   
           +"import gov.cbp.taspd.gtas.model.Message;"
           +"global java.util.List resultList;"    
            +"rule \"RH1 - Hello Messsage\""
              +"dialect \"java\""
              + "when"
              +   "m:Message( transmissionSource.equaals(\"Hello\"), date:transmissionDate )"
              +  "then"
                      +"System.out.println( \"Transmission date =\"+date );"
                      +"modify ( m ) { transmissionSource = \"Goodbye\"};"
              +"end";
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateKieBaseFromString() throws IOException{
		KieBase kbase = RuleUtils.createKieBaseFromDrlString(testDrl, new RuleServiceErrorHandler());
		assertNotNull("Expected non null KieBase", kbase);
		KieSession ksession = RuleUtils.createSession(kbase);
		assertNotNull("Expected non null KieSession", ksession);
	}

}
