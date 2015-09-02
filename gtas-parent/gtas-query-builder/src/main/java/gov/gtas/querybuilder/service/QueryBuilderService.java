package gov.gtas.querybuilder.service;

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.exceptions.InvalidQueryException;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.IQueryResult;
import gov.gtas.querybuilder.model.IUserQueryResult;
import gov.gtas.querybuilder.model.QueryFlightResult;
import gov.gtas.querybuilder.model.QueryPassengerResult;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.model.UserQueryResult;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author GTAS5
 *
 */
@Service
public class QueryBuilderService {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilderService.class);
	private SimpleDateFormat dobFormat = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	
	@Autowired
	QueryBuilderRepository queryRepository;
	
	public IUserQueryResult saveQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, InvalidQueryException {
		IUserQueryResult result = new UserQueryResult();
		
		try {
			result = mapToQueryResult(queryRepository.saveQuery(createUserQuery(queryRequest)));
		} catch(QueryAlreadyExistsRepositoryException e) {
			throw new QueryAlreadyExistsException(e.getMessage(), queryRequest);
		} catch (InvalidQueryException | InvalidQueryRepositoryException | IOException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryRequest);
		}
		
		return result;
	}

	public IUserQueryResult editQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, QueryDoesNotExistException, InvalidQueryException {
		IUserQueryResult result = new UserQueryResult();
		
		try {
			result = mapToQueryResult(queryRepository.editQuery(createUserQuery(queryRequest)));
		} catch(QueryAlreadyExistsRepositoryException e) {
			throw new QueryAlreadyExistsException(e.getMessage(), queryRequest);
		} catch (QueryDoesNotExistRepositoryException e) {
			throw new QueryDoesNotExistException(e.getMessage(), queryRequest);
		} catch (InvalidQueryException | InvalidQueryRepositoryException | IOException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryRequest);
		} 
		
		return result;
	}
	
	public List<IUserQueryResult> listQueryByUser(String userId) throws InvalidQueryException {
		List<IUserQueryResult> result = new ArrayList<>();
		
		try {
			result = mapToResultList(queryRepository.listQueryByUser(userId));
		} catch (InvalidQueryException e) {
			throw new InvalidQueryException(e.getMessage(), null);
		}
		
		return result;
	}
	
	public void deleteQuery(String userId, int id) throws QueryDoesNotExistException {
		
		try {
			queryRepository.deleteQuery(userId, id);
		} catch (QueryDoesNotExistRepositoryException e) {
			throw new QueryDoesNotExistException(e.getMessage(), null);
		}
	}
		
	public List<IQueryResult> runFlightQuery(QueryObject queryObject) throws InvalidQueryException {
		List<IQueryResult> result = new ArrayList<>();
		
		try {
			result = mapToQueryFlightResult(queryRepository.getFlightsByDynamicQuery(queryObject));
		} catch (InvalidQueryRepositoryException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryObject);
		} 
		
		return result;
	}
	
	public List<IQueryResult> runPassengerQuery(QueryObject queryObject) throws InvalidQueryException {
		List<IQueryResult> result = new ArrayList<>();
		
		try {
			result = mapToQueryPassengerResult(queryRepository.getPassengersByDynamicQuery(queryObject));
		} catch (InvalidQueryRepositoryException | IllegalArgumentException e) {
			throw new InvalidQueryException(e.getMessage(), queryObject);
		}
		
		return result;
	}
	
	private UserQuery createUserQuery(QueryRequest req) throws JsonProcessingException {
		UserQuery query = new UserQuery();
		ObjectMapper mapper = new ObjectMapper();
		
		if(req != null) {
			User user = new User();
			user.setUserId(req.getUserId());
			
			query.setId(req.getId());
			query.setCreatedBy(user);
			query.setTitle(req.getTitle());
			query.setDescription(req.getDescription());
			query.setQueryText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req.getQuery()));
		}
		
		return query;
	}
	
	private List<IQueryResult> mapToQueryFlightResult(List<Flight> flights) {
		List<IQueryResult> qbFlights = new ArrayList<>();
		
		if(flights == null || flights.size() == 0) {
			return qbFlights;
		}
		
		for(Flight flight : flights) {
			if(flight != null && flight.getId() > 0) {
				QueryFlightResult qbFlight = new QueryFlightResult();
				
				qbFlight.setId(flight.getId());
				qbFlight.setFlightNumber(flight.getFlightNumber() != null ? flight.getFlightNumber() : "");
				qbFlight.setCarrierCode(flight.getCarrier() != null ? flight.getCarrier() : "");
				qbFlight.setOrigin(flight.getOrigin() != null ? flight.getOrigin() : "");
				qbFlight.setOriginCountry(flight.getOriginCountry() != null ? flight.getOriginCountry() : "");
				qbFlight.setDepartureDt(flight.getEtd() != null ? dtFormat.format(flight.getEtd()) : "");
				qbFlight.setDestination(flight.getDestination() != null ? flight.getDestination() : "");
				qbFlight.setDestinationCountry(flight.getDestinationCountry() != null ? flight.getDestinationCountry() : "");
				qbFlight.setArrivalDt(flight.getEta() != null ? dtFormat.format(flight.getEta()) : "");
				
				qbFlights.add(qbFlight);
			}
		}
		
		return qbFlights;
	}
	
	private List<IQueryResult> mapToQueryPassengerResult(List<Object[]> resultList) {
		List<IQueryResult> qbPassengers = new ArrayList<>();
		
		if(resultList == null || resultList.size() == 0) {
			return qbPassengers;
		}
		
		for (Object[] result : resultList) {
			Passenger passenger = (Passenger) result[0];
			Flight flight = (Flight) result[1];
			Document document = (Document) result[2];
							
			// passenger information
			if(passenger != null && passenger.getId() > 0) {
				QueryPassengerResult qbPassenger = new QueryPassengerResult();
				String carrier = "";
				String flightNumber = "";
				String origin = "";
				String destination = "";
				String departureDt = "";
				String arrivalDt = "";
				String documentNumber = "";
				String documentType = "";
				String documentIssuanceCountry = "";
				
				qbPassenger.setId(passenger.getId());
				qbPassenger.setFirstName(passenger.getFirstName() != null ? passenger.getFirstName() : "");
				qbPassenger.setLastName(passenger.getLastName() != null ? passenger.getLastName() : "");
	            qbPassenger.setPassengerType(passenger.getPassengerType() != null ? passenger.getPassengerType() : "");				
				qbPassenger.setGender(passenger.getGender() != null ? passenger.getGender() : "");
				qbPassenger.setDob(passenger.getDob() != null ? dobFormat.format(passenger.getDob()) : "");
				qbPassenger.setCitizenship(passenger.getCitizenshipCountry() != null ? passenger.getCitizenshipCountry() : "");
				qbPassenger.setSeat(passenger.getSeat() != null ? passenger.getSeat() : "");

				// document information
				if(document != null) {
					documentNumber = document.getDocumentNumber() != null ? document.getDocumentNumber() : "";
					documentType  = document.getDocumentType() != null ? document.getDocumentType() : "";
					documentIssuanceCountry = document.getIssuanceCountry() != null ? document.getIssuanceCountry() : "";
				}
				
				qbPassenger.setDocumentNumber(documentNumber);
				qbPassenger.setDocumentType(documentType);
				qbPassenger.setDocumentIssuanceCountry(documentIssuanceCountry);
				
				
				// flight information
				if(flight != null) {
					carrier = flight.getCarrier() != null ? flight.getCarrier() : "";
					flightNumber = flight.getFlightNumber() != null ? flight.getFlightNumber() : "";
					origin = flight.getOrigin() != null ? flight.getOrigin() : "";
					destination = flight.getDestination() != null ? flight.getDestination() : "";
					departureDt = flight.getEtd() != null ? dtFormat.format(flight.getEtd()) : "";
					arrivalDt = flight.getEta() != null ? dtFormat.format(flight.getEta()) : "";
				}
				
				qbPassenger.setCarrierCode(carrier);
				qbPassenger.setFlightNumber(flightNumber);
				qbPassenger.setOrigin(origin);
				qbPassenger.setDestination(destination);
				qbPassenger.setDepartureDt(departureDt);
				qbPassenger.setArrivalDt(arrivalDt);
				
				qbPassenger.setRuleHit(false);
				qbPassenger.setOnWatchList(false);
				
				qbPassengers.add(qbPassenger);
			}
		}
		
		return qbPassengers;
	}
	
	private IUserQueryResult mapToQueryResult(UserQuery query) throws InvalidQueryException {
		IUserQueryResult result = new UserQueryResult();
		ObjectMapper mapper = new ObjectMapper();
		
		result.setId(query.getId());
		result.setTitle(query.getTitle());
		result.setDescription(query.getDescription());
		try {
			if(query.getQueryText() != null && !query.getQueryText().isEmpty()) {
				result.setQuery(mapper.readValue(query.getQueryText(), QueryObject.class));
			}
		} catch (IOException e) {
			throw new InvalidQueryException(e.getMessage(), query);
		}
		
		return result;
	}
	
	private List<IUserQueryResult> mapToResultList(List<UserQuery> queryList) throws InvalidQueryException {
		List<IUserQueryResult> resultList = new ArrayList<>();
		
		if(queryList != null && queryList.size() > 0) {
			for(UserQuery query : queryList) {
				try {
					resultList.add(mapToQueryResult(query));
				} catch (InvalidQueryException e) {
					throw new InvalidQueryException(e.getMessage(), queryList);
				}
			}
		}
		
		return resultList;
	}
}

