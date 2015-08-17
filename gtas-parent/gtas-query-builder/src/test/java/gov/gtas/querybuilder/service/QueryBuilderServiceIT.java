package gov.gtas.querybuilder.service;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.config.QueryBuilderAppConfig;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

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
	
	private QueryObject query;
	
	@Before
	public void setUp() throws Exception {
		query = buildSimpleBetweenQuery();
	}

	@After
	public void tearDown() throws Exception {
		query = null;
		
		// delete all records from the user_query table
		// thereby creating isolated integration test
//		deleteAllRecords();
	}
	

	@Transactional
	private void deleteAllRecords() {
		// delete all records from the user_query table
		Query deleteQuery = entityManager.createQuery("delete from UserQuery");
		deleteQuery.executeUpdate();
	}

//	@Test
//	@Transactional
	public void testSaveQuery() throws QueryAlreadyExistsException, InvalidQueryException, QueryDoesNotExistException {
//		QueryRequest request = new QueryRequest();
//		
//		request.setTitle(TITLE);
//		request.setDescription(DESCRIPTION);
//		request.setQuery(query);
//		request.setUserId(USER_ID);
//		
//		UserQuery result = queryService.saveQuery(request);
//		assertNotNull(result.getId());
	}
	
//	@Test(expected = QueryAlreadyExistsException.class)
//	@Transactional
	public void testSaveDuplicateQuery() throws QueryAlreadyExistsException, InvalidQueryException {
//		QueryRequest request = new QueryRequest();
//		
//		request.setTitle(TITLE + "1");
//		request.setDescription(DESCRIPTION);
//		request.setQuery(buildSimpleBetweenQuery());
//		request.setUserId(USER_ID);
//		
//		UserQuery result = queryService.saveQuery(request);
//		queryService.saveQuery(request);
//		assertNotNull(result.getId());
//		
//		queryService.saveQuery(request);
	}
	
//	@Test
	public void testSaveInvalidQuery() {
		
	}
	
//	@Test
	public void testPNRSimpleFlightQuery() {
		// select distinct f from Flight f join f.pnrs pnr where pnr.bagCount = ?1
		// select distinct f from Flight f join f.pnrs pnr join pnr.passengers pnr_p where pnr_p.firstName = ?1
		TypedQuery<Flight> query = entityManager.createQuery("select distinct f from Flight f join f.passengers p left join p.documents d where d.documentType = ?1", Flight.class);
		query.setParameter(1, "P");
		List<Flight> resultList = query.getResultList();
		
		if(resultList != null) {
			for(Flight f : resultList) {
				System.out.println("carrier: " + f.getCarrier() + " flight number: " + f.getFlightNumber());
			}
		}
	}
	
	@Test
	@Transactional
	public void testPNRSimplePassengerQuery() {
		//select distinct pnr from PnrData pnr join fetch pnr.passengers p left outer join fetch p.documents d where pnr.origin = ?1
		// select distinct p from Passenger p join p.pnrs pnr join fetch pnr.flights f join fetch p.documents d where pnr.bagCount = ?1
		// select distinct p from Passenger p join p.pnrs pnr where pnr.bagCount = ?1
		// select distinct p from Passenger p join p.pnrs pnr join fetch p.flights f left join fetch p.documents d where pnr.bagCount = ?1
		// select distinct p from Passenger p join p.pnrs pnr where pnr.creditCard.number = ?1
		TypedQuery<Passenger> query = entityManager.createQuery("select p from Passenger p where p.documents is empty or p.documents is not empty", Passenger.class);
//		query.setParameter(1, "2222-3333-4444-5555");
		List<Passenger> resultList = query.getResultList();
		
//		resultList.get(0);
//		if(resultList != null) {
//			for(Passenger p : resultList) {
//				System.out.println(" id: " + p.getId() + " first name: " + p.getFirstName() + " last name: " + p.getLastName());
//			}
//		}
	}
	
//	@Test
//	@Transactional
	public void testPNRQuery() {
		TypedQuery<Pnr> query = entityManager.createQuery("select distinct pnr from PnrData pnr join fetch pnr.passengers p left join fetch p.documents d where pnr.origin = ?1", Pnr.class);
		query.setParameter(1, "iad");
		List<Pnr> resultList = query.getResultList();
		
		if(resultList != null) {
			for(Pnr pnr : resultList) {
				int bagCount = pnr.getBagCount();
				Set<Passenger> passengers = pnr.getPassengers();
				if(!passengers.isEmpty()) {
					Iterator<Passenger> it = passengers.iterator();
					while(it.hasNext()) {
						Passenger passenger = it.next();
						System.out.println("first name: " + passenger.getFirstName());
						Set<Document> docs = passenger.getDocuments();
						if(!docs.isEmpty()) {
							Iterator<Document> docIt = docs.iterator();
							while(docIt.hasNext()) {
								Document doc = docIt.next();
								System.out.println("document number: " + doc.getDocumentNumber());
							}
						
						}
					}
				}
				System.out.println("bagCount: " + bagCount);
			}
		}
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
//		List<Passenger> passengers = (List<Passenger>) queryService.runPassengerQuery(query);
//		SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
//		
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Passenger passenger : passengers) {
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
//		List<Passenger> passengers = (List<Passenger>) queryService.runPassengerQuery(query);
//		
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Passenger passenger : passengers) {
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
//		List<Passenger> passengers = (List<Passenger>) queryService.runPassengerQuery(query);
//		
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Passenger passenger : passengers) {
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
//		List<Passenger> passengers = (List<Passenger>) queryService.runPassengerQuery(query);
//
//		if(passengers != null && passengers.size() > 0) {
//			System.out.println("Number of Passengers: " + passengers.size());
//			System.out.println("Passenger Information:");
//			for(Passenger passenger : passengers) {
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
//		ObjectMapper mapper = new ObjectMapper();
		
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
	private QueryObject buildSimpleBetweenQuery() {
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		QueryObject query = new QueryObject();
		List<String> values = new ArrayList<>();
		
		values.add("20");
		values.add("40");
		
		rule.setEntity("Passenger");
		rule.setField("age");
		rule.setOperator("between");
		rule.setType("integer");
		rule.setValue(values.toArray(new String[values.size()]));
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
	
}
