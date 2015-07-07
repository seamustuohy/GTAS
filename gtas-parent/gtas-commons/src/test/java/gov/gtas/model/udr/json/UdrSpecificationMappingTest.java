package gov.gtas.model.udr.json;

import static org.junit.Assert.fail;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;

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
		UdrSpecification testObj = UdrSpecificationBuilder.createSampleSpec();
		
		//serialize
		String json=mapper.writeValueAsString(testObj);
	    //de-serialize
		mapper.readValue(json, UdrSpecification.class);
		
		} catch(Exception ex){
			ex.printStackTrace();
			fail("Got exception");
		}
	}
}
