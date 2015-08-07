package gov.gtas.querybuilder.service;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.User;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.json.QueryConditionEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;
import gov.gtas.querybuilder.config.QueryBuilderAppConfig;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.enums.OperatorEnum;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.mappings.TravelerMapping;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.util.DateCalendarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *	Query Builder Service Integration Test 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonServicesConfig.class, QueryBuilderAppConfig.class})
public class QueryBuilderServiceIT {

	@PersistenceContext 
 	private EntityManager entityManager;
	@Autowired
	private QueryBuilderService queryService;
	private static final String TITLE = "Sample Query";
	private static final String DESCRIPTION = "A simple query";
	private static final String USER_ID = "ladebiyi";
	private static final int QUERY_ID = 1;
	
	QueryObject query;
	QueryTerm rule;
	List<QueryEntity> rules;
	
	@Before
	public void setUp() throws Exception {
		query = new QueryObject();
		rule = new QueryTerm();
		rules = new ArrayList<>();
	}

	@After
	public void tearDown() throws Exception {
		query = null;
		rule = null;
		rules = null;
		Query deleteQuery = entityManager.createQuery("delete from UserQuery");
		deleteQuery.executeUpdate();
	}

	@Test
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
		QueryRequest request = new QueryRequest();
		
		request.setTitle(TITLE);
		request.setDescription(DESCRIPTION);
		request.setQuery(buildSimpleBetweenQuery());
		request.setUserId(USER_ID);
		
		UserQuery result = queryService.saveQuery(request);
		
//		queryService.deleteQuery(USER_ID, result.getId());
	}
	
	@Test
	public void testSaveDuplicateQuery() {
//		QueryRequest request = new QueryRequest();
//		
//		request.setTitle(TITLE);
//		request.setDescription(DESCRIPTION);
//		request.setQuery(buildSimpleBetweenQuery());
//		request.setUserId(USER_ID);
//		
//		UserQuery result = queryService.saveQuery(request);
//		queryService.saveQuery(request);
	}
	
	public void testSaveInvalidQuery() {
		
	}
	
	//----------------------------------------
	// Flight Queries
	//----------------------------------------
//	@Test
	public void testRunQueryAgainstFlights() throws InvalidQueryException  {
//		QueryObject query = buildSimpleQuery();
//		List<Flight> flights = (List<Flight>) queryService.runFlightQuery(query);
//		
//		if(flights != null && flights.size() > 0) {
//			System.out.println("Number of flights: " + flights.size());
//			System.out.println("Flight Information:");
//			
//			for(Flight flight : flights) {
//				System.out.println("\tfight number: " + flight.getFlightNumber());
//			}
//		}
//		else {
//			System.out.println("No flights");
//		}
	}

//	@Test
	public void testSimpleDateQueryAgainstFlights() throws InvalidQueryException {
//		QueryObject query = buildSimpleDateQuery();
//		List<Flight> flights = (List<Flight>) queryService.runFlightQuery(query);
//		
//		if(flights != null && flights.size() > 0) {
//			System.out.println("Number of flights: " + flights.size());
//			System.out.println("Flight Information:");
//			for(Flight flight : flights) {
//				System.out.println("\tfight number: " + flight.getFlightNumber());
//			}
//		}
//		else {
//			System.out.println("No flights");
//		}
	}
	
//	@Test
	public void testSimpleIsNullQueryAgainstFlights() throws InvalidQueryException {
//		QueryObject query = buildSimpleIsNullQuery();
//		List<Flight> flights = (List<Flight>) queryService.runFlightQuery(query);
//		
//		if(flights != null && flights.size() > 0) {
//			System.out.println("Number of flights: " + flights.size());
//			System.out.println("Flight Information:");
//			for(Flight flight : flights) {
//				System.out.println("\tfight number: " + flight.getFlightNumber());
//			}
//		}
//		else {
//			System.out.println("No flights");
//		}
	}
	
//	@Test
	public void testSimpleContainsQueryAgainstFlights() throws InvalidQueryException {
//		QueryObject query = buildSimpleContainsQuery();
//		List<Flight> flights = (List<Flight>) queryService.runFlightQuery(query);
//		
//		if(flights != null && flights.size() > 0) {
//			System.out.println("Number of flights: " + flights.size());
//			System.out.println("Flight Information:");
//			for(Flight flight : flights) {
//				System.out.println("\tfight number: " + flight.getFlightNumber());
//			}
//		}
//		else {
//			System.out.println("No flights");
//		}
	}
	
//	@Test
	public void testSimpleBetweenQuery() throws InvalidQueryException {
//		QueryObject query = buildSimpleBetweenQuery();
//		List<Flight> flights = (List<Flight>) queryService.runFlightQuery(query);
//		
//		if(flights != null && flights.size() > 0) {
//			System.out.println("Number of flights: " + flights.size());
//			System.out.println("Flight Information:");
//			for(Flight flight : flights) {
//				System.out.println("\tfight number: " + flight.getFlightNumber());
//			}
//		}
//		else {
//			System.out.println("No flights");
//		}
	}
	
//	@Test
	public void testComplexQueryAgainstFlights() throws JsonProcessingException {
	}
	//-------------------------------
	// Passenger Queries
	//-------------------------------
//	@Test
	public void testRunQueryAgainstPassengers() throws InvalidQueryException {
//		QueryObject query = buildSimpleQuery();
//		
//		List<Traveler> passengers = (List<Traveler>) queryService.runPassengerQuery(query);
//		SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
//		
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Traveler passenger : passengers) {
//				String docNumber = "";
//				String docType = "";
//				String docIssuanceCountry = "";
//				String carrierCode = "";
//				String flightNumber = "";
//				String origin = "";
//				String destination = "";
//				String departureDt = "";
//				String arrivalDt = "";
//				
//				System.out.println("\tFirst name: " + passenger.getFirstName() + " Last name: " + passenger.getLastName() + " DOB: " + passenger.getDob());
//				
//				Set<Document> docs = passenger.getDocuments();
//				System.out.println("number of docs: " + docs.size());
//				if(docs != null) {
//					if(docs.iterator().hasNext()) {
//						Document doc = docs.iterator().next();
//						docNumber = doc.getDocumentNumber();
//						docType = doc.getDocumentType();
//						docIssuanceCountry = doc.getIssuanceCountry();
//					}
//				}
//
//				System.out.println("doc number: " + docNumber);
//				System.out.println("doc type: " + docType);
//				System.out.println("issuance country: " + docIssuanceCountry);
//				
//				Set<Flight> flights = passenger.getFlights();
//				if(flights != null) {
//					if(flights.iterator().hasNext()) {
//						Flight flight = flights.iterator().next();
//						
//						carrierCode = flight.getCarrier() != null ? flight.getCarrier() : "";
//						flightNumber = flight.getFlightNumber();
//						origin = flight.getOrigin() != null ? flight.getOrigin() : "";
//						destination  = flight.getDestination() != null ? flight.getDestination() : "";
//						departureDt = dtFormat.format(flight.getEtd());
//						arrivalDt = dtFormat.format(flight.getEta());
//					}
//				}
//				
//				System.out.println("carrierCode: " + carrierCode);
//				System.out.println("flightNumber: " + flightNumber);
//				System.out.println("origin: " + origin);
//				System.out.println("destination: " + destination);
//				System.out.println("departureDt: " + departureDt);
//				System.out.println("arrivalDt: " + arrivalDt);
//			}
//		}
//		else {
//			System.out.println("No passengers");
//		}
	}
	
//	@Test
	public void testSimpleIsNullQueryAgainstPassengers() throws InvalidQueryException {
//		QueryObject query = buildSimpleIsNullQuery();
//		List<Traveler> passengers = (List<Traveler>) queryService.runPassengerQuery(query);
//		
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Traveler passenger : passengers) {
//				System.out.println("\tFirst name: " + passenger.getFirstName() + " Last name: " + passenger.getLastName() + " DOB: " + passenger.getDob());
//			}
//		}
//		else {
//			System.out.println("No passengers");
//		}
	}
	
//	@Test
	public void testSimpleContainsQueryAgainstPassengers() throws InvalidQueryException {
//		QueryObject query = buildSimpleContainsQuery();
//		List<Traveler> passengers = (List<Traveler>) queryService.runPassengerQuery(query);
//		
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Traveler passenger : passengers) {
//				System.out.println("\tFirst name: " + passenger.getFirstName() + " Last name: " + passenger.getLastName() + " DOB: " + passenger.getDob());
//			}
//		}
//		else {
//			System.out.println("No passengers");
//		}
	}
	
//	@Test
	public void testSimpleBetweenQueryAgainstPassengers() throws InvalidQueryException {
//		QueryObject query = buildSimpleBetweenQuery();
//		List<Traveler> passengers = (List<Traveler>) queryService.runPassengerQuery(query);
//
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Traveler passenger : passengers) {
//				System.out.println("\tFirst name: " + passenger.getFirstName() + " Last name: " + passenger.getLastName() + " DOB: " + passenger.getDob());
//			}
//		}
//		else {
//			System.out.println("No passengers");
//		}
	}
	
	
//	@Test
	public void display() throws JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		UserQuery queryToSave = new UserQuery();
//		User user = new User();
//		
//		String queryText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(buildSimpleBetweenQuery());
//		
//		user.setUserId("ladebiyi");
//		queryToSave = new UserQuery();
//		queryToSave.setCreatedBy(user);
//		queryToSave.setCreatedDt(new Date());
//		queryToSave.setTitle("Test Query 1");
//		queryToSave.setDescription("Test description ");
//		queryToSave.setQueryText(queryText);
//		
//		System.out.println("query: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryToSave));
	}
	/**
	 * 
	 * @throws JsonProcessingException
	 * @throws QueryAlreadyExistsException 
	 * @throws InterruptedException 
	 */
//	@Test
	public void testSaveQuery2() throws JsonProcessingException, QueryAlreadyExistsException, InterruptedException {
//		ObjectMapper mapper = new ObjectMapper();
//		UserQuery queryToSave = new UserQuery();
//		User user = new User();
//		
//		String queryText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(buildSimpleBetweenQuery());
//		
//		user.setUserId("ladebiyi");
//		for(int i = 1; i <= 3; i++) {
//			queryToSave = new UserQuery();
//			queryToSave.setCreatedBy(user);
//			queryToSave.setCreatedDt(new Date());
//			queryToSave.setTitle("Test Query " + i);
//			queryToSave.setDescription("Test description " + i);
//			queryToSave.setQueryText(queryText);
//			
////			queryService.saveQuery(queryToSave);
//			Thread.sleep(5000); // wait for 5 seconds
//		}
//		
//		user.setUserId("bstygar");
//		for(int i = 1; i <= 4; i++) {
//			queryToSave = new UserQuery();
//			queryToSave.setCreatedBy(user);
//			queryToSave.setCreatedDt(new Date());
//			queryToSave.setTitle("Test Query " + i);
//			queryToSave.setDescription("Test description " + i);
//			queryToSave.setQueryText(queryText);
//			
////			queryService.saveQuery(queryToSave);
//			Thread.sleep(5000); // wait for 5 seconds
//		}
		
	}
	
//	@Test
	public void addDuplicateQuery() throws JsonProcessingException, QueryAlreadyExistsException, InterruptedException {
//		ObjectMapper mapper = new ObjectMapper();
//		UserQuery queryToSave = new UserQuery();
//		User user = new User();
//		
//		String queryText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(buildSimpleBetweenQuery());
//		
//		user.setUserId("ladebiyi");
//		queryToSave = new UserQuery();
//		queryToSave.setCreatedBy(user);
//		queryToSave.setCreatedDt(new Date());
//		queryToSave.setTitle("Test Query 1");
//		queryToSave.setDescription("Test description ");
//		queryToSave.setQueryText(queryText);
			
//		queryService.saveQuery(queryToSave);
	}
	
//	@Test 
	public void testEditQuery() throws JsonProcessingException, QueryAlreadyExistsException {
		ObjectMapper mapper = new ObjectMapper();
		
//		Query query = queryService.listQueryByUser("ladebiyi").get(0);
//		Query query = queryService.getQuery(1);
		
//		query.setTitle("SimpleQuery");
//		query.setDescription("Updated query from SimpleBetweenQuery to  SimpleQuery");
//		query.setQueryText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(buildSimpleQuery()));
//		
//		Query editedQuery = queryService.editQuery(query);
		
//		System.out.println("-----------------------------------");
//		System.out.println("Edited Query");
//		System.out.println("id: " + editedQuery.getId());
//		System.out.println("query title: " + editedQuery.getTitle());
//		System.out.println("query description: " + editedQuery.getDescription());
//		System.out.println("query text: " + editedQuery.getQueryText());
//		System.out.println("-----------------------------------\n");
	}
	
//	@Test
	public void testListQueryByUser() {
//		List<UserQuery> queryList = queryService.listQueryByUser("bstygar");
//		
//		if(queryList != null && queryList.size() > 0) {
//			System.out.println("\nnumber of queries: " + queryList.size());
//			
//			for(UserQuery query : queryList) {
//				System.out.println("-----------------------------------");
//				System.out.println("id: " + query.getId());
//				System.out.println("query title: " + query.getTitle());
//				System.out.println("query description: " + query.getDescription());
//				System.out.println("query text: " + query.getQueryText());
//				System.out.println("-----------------------------------\n");
//			}
//		}
//		else {
//			System.out.println("query list size: " + queryList.size());
//		}
	}

//	@Test
	public void testDeleteQuery() {
//		queryService.deleteQuery("bstygar", 6);
	}
	
	//---------------------------------------
	// Build Query Objects
	//---------------------------------------
	
	private QueryObject buildSimpleQuery() {
		
		rule.setEntity("TRAVELER");
		rule.setField("gender");
		rule.setOperator("equal");
		rule.setType("string");
		rule.setValue(new String[]{"male"});
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleDateQuery() {
		
		rule.setEntity("Flight");
		rule.setField("eta");
		rule.setOperator("equal");
		rule.setType("date");
		rule.setValue(new String[]{"05/11/2014 5:00:00 PM"});
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleIsNullQuery() {
		
		rule.setEntity("Pax");
		rule.setField("middleName");
		rule.setOperator("is_null");
		rule.setType("boolean");
		rule.setValue(new String[]{""});
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
		
	private QueryObject buildSimpleContainsQuery() {
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("contains");
		rule.setType("string");
		rule.setValue(new String[]{"avi"});
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private QueryObject buildSimpleBetweenQuery() {
		List<String> values = new ArrayList<>();
		
		values.add("20");
		values.add("40");
		
		rule.setEntity("Traveler");
		rule.setField("age");
		rule.setOperator("between");
		rule.setType("integer");
		///rule.setValue(new String[]{""});
		rule.setValue(values.toArray(new String[values.size()]));
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
	private void testing() throws JsonProcessingException {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate","Date","EQUAL", new String[]{new Date().toString()});
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "String", "EQUAL", new String[]{"Jones"}));

		QueryObject queryObjectEmbedded = new QueryObject();
		queryObjectEmbedded.setCondition("AND");
		List<QueryEntity> rules2 = new LinkedList<QueryEntity>();
		
		QueryTerm trm2 = new QueryTerm("Pax", "embarkation","String", "IN", new String[]{"DBY","PKY","FLT"});
		rules2.add(trm2);
		rules2.add(new QueryTerm("Pax", "debarkation", "String", "EQUAL", new String[]{"IAD"}));
		queryObjectEmbedded.setRules(rules2);

		queryObject.setRules(rules);
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryObject));
	}
	
	private QueryObject buildComplexQueryObject() {
		final UdrSpecificationBuilder bldr = new UdrSpecificationBuilder(null,
				QueryConditionEnum.OR);
		bldr.addTerm(EntityEnum.TRAVELER,
				TravelerMapping.DOB.getFieldName(), ValueTypesEnum.DATE,
				OperatorCodeEnum.EQUAL,
				new String[] { DateCalendarUtils.formatJsonDate(new Date()) });
		bldr.addTerm(EntityEnum.TRAVELER,
				TravelerMapping.LAST_NAME.getFieldName(),
				ValueTypesEnum.STRING, OperatorCodeEnum.EQUAL,
				new String[] { "Jones" });
		bldr.addNestedQueryObject(QueryConditionEnum.AND);
		bldr.addTerm(EntityEnum.TRAVELER,
				TravelerMapping.EMBARKATION.getFieldName(),
				ValueTypesEnum.STRING, OperatorCodeEnum.IN, new String[] {
						"DBY", "PKY", "FLT" });
		bldr.addTerm(EntityEnum.TRAVELER,
				TravelerMapping.DEBARKATION.getFieldName(),
				ValueTypesEnum.STRING, OperatorCodeEnum.EQUAL,
				new String[] { "IAD" });

		return bldr.build().getDetails();
	}
	private QueryObject buildComplexQuery() throws JsonProcessingException {
		rule.setEntity("Passenger");
		rule.setField("firstName");
		rule.setOperator(OperatorEnum.BEGINS_WITH.toString());
		rule.setType("string");
		rule.setValue(new String[]{"da"});
		
		rules.add(rule);
		
		rule = new QueryTerm();
		rule.setEntity("Passenger");
		rule.setField("lastName");
		rule.setOperator(OperatorEnum.BEGINS_WITH.toString());
		rule.setType("string");
		rule.setValue(new String[]{"c"});
		
		rules.add(rule);
		
		query.setCondition("OR");
		query.setRules(rules);
		
		QueryObject query2 = new QueryObject();
		List<QueryTerm> rules2 = new ArrayList<>();
		
		query2.setCondition(Constants.AND);
		rule = new QueryTerm();
		rule.setEntity("Passenger");
		rule.setField("lastName");
		rule.setOperator(OperatorEnum.BEGINS_WITH.toString());
		rule.setType("string");
		rule.setValue(new String[]{"c"});
		rules2.add(rule);
		
		rule = new QueryTerm();
		rule.setEntity("Passenger");
		rule.setField("lastName");
		rule.setOperator(OperatorEnum.BEGINS_WITH.toString());
		rule.setType("string");
		rule.setValue(new String[]{"c"});
		rules2.add(rule);
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(query));
		  
		return query;
	}
}
