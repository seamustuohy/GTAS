package gov.gtas.model.udr.json;

import static org.junit.Assert.*;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;

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
		QueryTerm trm = new QueryTerm("Pax", "firstName","EQUAL", "John");
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "EQUAL", "Jones"));
		queryObject.setRules(rules);
		
		UdrSpecification resp = new UdrSpecification(queryObject, new MetaData("Hello Rule 1", "This is a test", new Date(), "jpjones"));
    	return resp;
    }
}
