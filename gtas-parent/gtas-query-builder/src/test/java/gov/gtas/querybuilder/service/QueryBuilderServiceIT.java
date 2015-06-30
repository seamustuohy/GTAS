package gov.gtas.querybuilder.service;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.config.QueryBuilderAppConfig;
import gov.gtas.querybuilder.util.EntityEnum;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonServicesConfig.class, QueryBuilderAppConfig.class})
public class QueryBuilderServiceIT {

	@Autowired
	QueryBuilderService queryService;
	QueryObject query;
	
	@Before
	public void setUp() throws Exception {
		query = buildSimpleQuery();
	}

	@After
	public void tearDown() throws Exception {
		query = null;
	}

	@Test
	public void testRunQueryAgainstFlights() {
		List<Flight> flights = (List<Flight>) queryService.runQuery(query, EntityEnum.FLIGHT);
		
		if(flights != null && flights.size() > 0) {
			System.out.println("Number of flights: " + flights.size());
			System.out.println("Flight Information:");
			for(Flight flight : flights) {
				System.out.println("\tfight number: " + flight.getFlightNumber());
			}
		}
		else {
			System.out.println("No flights");
		}
	}

	@Test
	public void testRunQueryAgainstPassengers() {
		List<Traveler> passengers = (List<Traveler>) queryService.runQuery(query, EntityEnum.PASSENGER);
		
		if(passengers != null && passengers.size() > 0) {
			System.out.println("Number of Passengers: " + passengers.size());
			System.out.println("Passenger Information:");
			for(Traveler passenger : passengers) {
				System.out.println("\tFirst name: " + passenger.getFirstName() + " Last name: " + passenger.getLastName() + " DOB: " + passenger.getDob());
			}
		}
		else {
			System.out.println("No passengers");
		}
	}
	
	private QueryObject buildSimpleQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("equal");
		rule.setType("string");
		rule.setValue("DAVID");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
}
