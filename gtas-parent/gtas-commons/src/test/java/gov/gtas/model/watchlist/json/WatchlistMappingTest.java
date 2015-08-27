package gov.gtas.model.watchlist.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import gov.gtas.model.watchlist.util.WatchlistBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WatchlistMappingTest {
    private static final String TEST_JSON =
    	"{\"@class\": \"gov.gtas.model.watchlist.json.Watchlist\","
       +" \"entity\": \"PASSENGER\","
       +"\"watchlistItems\": ["
        +"    {"
        //+ "\"@class\": \"gov.gtas.model.watchlist.json.WatchlistItem\","
        +"        \"action\": \"Create\","
        +"        \"terms\": ["
        +"            {  \"type\": \"string\","
         +"               \"entity\": \"PASSENGER\","
          +"              \"field\": \"firstName\","
         +"               \"value\": \"John\" },"
         +"           { \"type\": \"string\","
    	+"               \"entity\": \"PASSENGER\","
         +"               \"field\": \"lastName\","
          +"              \"value\": \"Jones\" },"
         +"          {  \"type\": \"date\","
         +"              \"entity\": \"PASSENGER\","
          +"              \"field\": \"dob\","
          +"              \"value\": \"1747-07-06\"  } ]},"
 
        +"  { "
        //+ "\"@class\": \"gov.gtas.model.watchlist.json.WatchlistItem\","
        +"        \"action\": \"Create\","
        +"        \"terms\": [ {  \"type\": \"string\","
         +"               \"entity\": \"PASSENGER\","
                        +"               \"field\": \"firstName\","
         +"               \"value\": \"The\" },"
         +"           {  \"type\": \"string\","
         +"               \"entity\": \"PASSENGER\","
         +"               \"field\": \"lastName\","
         +"               \"value\": \"Donald\" },"
         +"           {  \"type\": \"date\","
         +"               \"entity\": \"PASSENGER\","
          +"              \"field\": \"dob\","
         +"               \"value\": \"1957-04-01\" } ] } ] } ";       


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUdrSpecToJson() {
		try{
		ObjectMapper mapper = new ObjectMapper();
		Watchlist testObj = WatchlistBuilder.createSampleWatchlist();
		
		//serialize
		String json=mapper.writeValueAsString(testObj);
	    //de-serialize
		mapper.readValue(json, Watchlist.class);
		
		} catch(Exception ex){
			ex.printStackTrace();
			fail("Got exception");
		}
	}
	@Test
	public void testJsonToUdrSpec() {
		try{
			ObjectMapper mapper = new ObjectMapper();
		    //de-serialize
			Watchlist testObj = mapper.readValue(TEST_JSON, Watchlist.class);	
			assertNotNull(testObj);
			assertEquals("PASSENGER", testObj.getEntity());
		} catch(Exception ex){
			ex.printStackTrace();
			fail("Got exception");
		}
	}
}
