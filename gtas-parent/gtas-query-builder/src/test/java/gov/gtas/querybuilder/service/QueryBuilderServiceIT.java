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
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//----------------------------------------
	// Flight Queries
	//----------------------------------------
	@Test
	public void testRunQueryAgainstFlights() {
		QueryObject query = buildSimpleQuery();
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
	public void testSimpleDateQueryAgainstFlights() {
		QueryObject query = buildSimpleDateQuery();
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
	public void testSimpleIsNullQueryAgainstFlights() {
		QueryObject query = buildSimpleIsNullQuery();
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
	public void testSimpleContainsQueryAgainstFlights() {
		QueryObject query = buildSimpleContainsQuery();
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
	
	//-------------------------------
	// Passenger Queries
	//-------------------------------
	@Test
	public void testRunQueryAgainstPassengers() {
		QueryObject query = buildSimpleQuery();
		List<Traveler> passengers = (List<Traveler>) queryService.runQuery(query, EntityEnum.PAX);
		
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
	
	@Test
	public void testSimpleIsNullQueryAgainstPassengers() {
		QueryObject query = buildSimpleIsNullQuery();
		List<Traveler> passengers = (List<Traveler>) queryService.runQuery(query, EntityEnum.PAX);
		
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
	
	@Test
	public void testSimpleContainsQueryAgainstPassengers() {
		QueryObject query = buildSimpleContainsQuery();
		List<Traveler> passengers = (List<Traveler>) queryService.runQuery(query, EntityEnum.PAX);
		
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
	
	@Test
	public void testSimpleBetweenQueryAgainstPassengers() {
		QueryObject query = buildSimpleBetweenQuery();
		List<Traveler> passengers = (List<Traveler>) queryService.runQuery(query, EntityEnum.PAX);

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
	
	//---------------------------------------
	// Build Query Objects
	//---------------------------------------
	
	private QueryObject buildSimpleQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("equal");
		rule.setType("string");
		rule.setValue("David");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleDateQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Flight");
		rule.setField("eta");
		rule.setOperator("equal");
		rule.setType("date");
		rule.setValue("2014-05-11");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleIsNullQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("middleName");
		rule.setOperator("is_null");
		rule.setType("boolean");
		rule.setValue("");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
		
	private QueryObject buildSimpleContainsQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("contains");
		rule.setType("string");
		rule.setValue("avi");
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleBetweenQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		List<String> values = new ArrayList<>();
		
		values.add("20");
		values.add("40");
		
		rule.setEntity("Pax");
		rule.setField("age");
		rule.setOperator("between");
		rule.setType("integer");
		rule.setValue("");
		rule.setValues(values.toArray(new String[values.size()]));
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
}
