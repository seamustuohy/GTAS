package gov.gtas.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import gov.gtas.error.BasicErrorHandler;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Message;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.rule.RuleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

public class RuleUtilsIT {
    private static final String testDrl = 
    		"package gov.gtas.rule; "   
           +"import gov.gtas.model.ApisMessage; "
           +"global java.util.List resultList; "    
            +"rule \"RH1 - Hello Messsage\" "
              +"dialect \"mvel\" "
              + "when "
              +   "m:ApisMessage( transmissionSource.equals(\"Hello\"), date:transmissionDate ) "
              +  "then "
                      +"System.out.println( \"Transmission date =\"+date ); "
                      +"modify ( m ) { transmissionSource = \"Goodbye\"}; "
              +"end "
              +"rule \"RH2 - GoodBye Messsage\" "
              +"dialect \"java\" "
              + "when "
              +   "m:ApisMessage( transmissionSource.equals(\"Goodbye\"), date:transmissionDate ) "
              +  "then "
                      +"System.out.println( \"Got Goodbye\"); "
                      +"resultList.add(m.getTransmissionDate()); "
              +"end";
    
    private static final String testJson =
    	"{ \"details\": {"
          +"  \"@class\": \"gov.gtas.model.udr.json.QueryObject\","
           +" \"condition\": \"OR\","
           +" \"rules\": ["
             +  " {"
               +"   \"@class\": \"QueryTerm\","
                    + " \"entity\": \"Pax\","
                    + " \"attribute\": \"firstName\","
                    + " \"operator\": \"EQUAL\","
                    + " \"value\": \"John\" "
               +" },"
               + " {"
                    + " \"@class\": \"QueryTerm\","
                    + " \"entity\": \"Pax\","
                    + " \"attribute\": \"lastName\","
                    + " \"operator\": \"EQUAL\","
                    + " \"value\": \"Jones\" "
                +"}"
            +"]"
        +"},"
        + " \"summary\": {"
            + " \"title\": \"Hello Rule 1\","
            + " \"description\": \"This is a test\","
            + " \"startDate\": \"2015-06-24\","
            + " \"endDate\": null,"
            +" \"author\": \"jpjones\","
            + " \"enabled\": false"
        + "}"
    +"}";
    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateKieBaseFromString() throws IOException{
		KieBase kbase = RuleUtils.createKieBaseFromDrlString(testDrl, new BasicErrorHandler());
		assertNotNull("Expected non null KieBase", kbase);
		KieSession ksession = RuleUtils.createSession(kbase);
		assertNotNull("Expected non null KieSession", ksession);

		//set up the global
        ksession.setGlobal( "resultList",
                new ArrayList<Object>() );

        //insert the fact
		ApisMessage msg = new ApisMessage();
		msg.setTransmissionSource("Hello");
		Date transmissionDate = new Date();
		msg.setTransmissionDate(transmissionDate);
        ksession.insert(msg);
        
        // and fire the rules
        ksession.fireAllRules();
        
        //extract the result
        final List<?> resList = (List<?>)ksession.getGlobal( "resultList");
		
        ksession.dispose();
        
	    assertNotNull(resList);
	    assertEquals("Result list is empty", 1, resList.size());
	    assertEquals("Expected Transmission Date", transmissionDate, resList.get(0));
	}
	@Test
	public void testMultipleSession() throws IOException{
		//verify that multiple sessions can be created with different IDs
		KieBase kbase = RuleUtils.createKieBaseFromDrlString(testDrl, new BasicErrorHandler());
		KieSession s1 = RuleUtils.createSession(kbase);
		KieSession s2 = RuleUtils.createSession(kbase);
		assertNotEquals("Unexpected - got identical ids for two sessions", s1.getId(), s2.getId());
		s1.dispose();
		s2.dispose();		
	}
    @Test
    public void testBinarySerializationOfKieBase()  throws IOException, ClassNotFoundException{
		KieBase kbase = RuleUtils.createKieBaseFromDrlString(testDrl, new BasicErrorHandler());
    	byte[] blob = RuleUtils.convertKieBaseToBytes(kbase);
    	assertNotNull("ERROR - KieBase blob is null", blob);
    	byte[] blobCopy = Arrays.copyOf(blob, blob.length);
    	kbase = RuleUtils.convertKieBasefromBytes(blobCopy);
    	assertNotNull("ERROR - could not get KieBase from blob", kbase);
    	KieSession s = RuleUtils.createSession(kbase);
    	assertNotNull("Could not Create KieSession from copied KieBase", s);
    	s.dispose();
    	
    }
//    @Test
//    public void testBinarySerialization()  throws IOException, ClassNotFoundException{
//		UdrSpecification jsonObject = RuleUtils.createKieBaseFromDrlString(testDrl, new BasicErrorHandler());
//    	byte[] blob = RuleUtils.convertKieBaseToBytes(kbase);
//    	assertNotNull("ERROR - KieBase blob is null", blob);
//    	byte[] blobCopy = Arrays.copyOf(blob, blob.length);
//    	kbase = RuleUtils.convertKieBasefromBytes(blobCopy);
//    	assertNotNull("ERROR - could not get KieBase from blob", kbase);
//    	KieSession s = RuleUtils.createSession(kbase);
//    	assertNotNull("Could not Create KieSession from copied KieBase", s);
//    	s.dispose();
//    	
//    }
}
