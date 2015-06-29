package gov.gtas.model.udr.json;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UdrSpecificationMappingTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try{
		ObjectMapper mapper = new ObjectMapper();
		UdrSpecification testObj = createSpec();
		
		//serialize
		String json=mapper.writeValueAsString(testObj);
	    //de-serialize
		mapper.readValue(json, UdrSpecification.class);
		
		} catch(Exception ex){
			ex.printStackTrace();
			fail("Got exception");
		}
	}
    private UdrSpecification createSpec(){
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate","Date","EQUAL", new String[]{new Date().toString()});
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "String", "EQUAL", new String[]{"Jones"}));

		QueryObject queryObjectEmbedded = new QueryObject();
		queryObjectEmbedded.setCondition("AND");
		List<QueryEntity> rules2 = new LinkedList<QueryEntity>();
		
		QueryTerm trm2 = new QueryTerm("Pax", "embarkation.name","String", "IN", new String[]{"DBY","PKY","FLT"});
		rules2.add(trm2);
		rules2.add(new QueryTerm("Pax", "debarkation.name", "String", "EQUAL", new String[]{"IAD"}));
		queryObjectEmbedded.setRules(rules2);

		queryObject.setRules(rules);
		
		UdrSpecification resp = new UdrSpecification(null, queryObject, new MetaData("Hello Rule 1", "This is a test", new Date(), "jpjones"));
    	return resp;
    }
}
