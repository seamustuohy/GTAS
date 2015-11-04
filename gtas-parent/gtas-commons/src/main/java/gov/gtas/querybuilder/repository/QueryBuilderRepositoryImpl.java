package gov.gtas.querybuilder.repository;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.OperatorEnum;
import gov.gtas.enumtype.TypeEnum;
import gov.gtas.model.Flight;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.JPQLGenerator;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;

@Repository
public class QueryBuilderRepositoryImpl implements QueryBuilderRepository {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilderRepository.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
	private static final String CREATED_BY = "createdBy";
	private static final String TITLE = "title";
	private static final String PERCENT_SIGN = "%";
	
	@PersistenceContext 
 	private EntityManager entityManager;

	@Override
	@Transactional
	public UserQuery saveQuery(UserQuery query) throws QueryAlreadyExistsRepositoryException, 
		InvalidQueryRepositoryException {
		
		if(query != null) {
			try {
				Errors errors = QueryValidationUtils.validateQueryRequest(query);
				
				if(errors != null && errors.hasErrors()) {
					throw new InvalidQueryRepositoryException(QueryValidationUtils.getErrorString(errors), query);
				}
			} catch (IOException e) {
				throw new InvalidQueryRepositoryException(e.getMessage(), query);
			} 
				
			// check whether the title is unique before saving
			// you can't have queries with duplicate titles for the same user
			if(isUniqueTitle(query)) {
				query.setId(null);
				query.setTitle(query.getTitle() != null ? query.getTitle().trim() : query.getTitle());
				query.setDescription(query.getDescription() != null ? query.getDescription().trim() : query.getDescription());
				query.setCreatedDt(new Date());
				
				// save query to database
				entityManager.persist(query);
			}
			else {
				throw new QueryAlreadyExistsRepositoryException(Constants.QUERY_EXISTS_ERROR_MSG, query);
			}
		}
		
		return query;
	}

	@Override
	@Transactional
	public UserQuery editQuery(UserQuery query) throws QueryAlreadyExistsRepositoryException, 
		QueryDoesNotExistRepositoryException, InvalidQueryRepositoryException {
		UserQuery queryToSave = new UserQuery();
		boolean isUniqueTitle = true;
		
		if(query != null && query.getId() != null) {
			
			try {
				Errors errors = QueryValidationUtils.validateQueryRequest(query);
				
				if(errors != null && errors.hasErrors()) {
					throw new InvalidQueryRepositoryException(QueryValidationUtils.getErrorString(errors), query);
				}
			} catch (IOException e) {
				throw new InvalidQueryRepositoryException(e.getMessage(), query);
			} 
			
			queryToSave = entityManager.find(UserQuery.class, query.getId());
			
			// check whether the query exists or has not been deleted
			// before updating
			if(queryToSave == null || queryToSave.getDeletedDt() != null) {
				throw new QueryDoesNotExistRepositoryException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, query);
			}
			
			// check if the query's title is unique
			if(!query.getTitle().trim().equalsIgnoreCase(queryToSave.getTitle().trim())) {
				isUniqueTitle = isUniqueTitle(query);
			}
						
			// update the query
			if(isUniqueTitle) {
				queryToSave.setTitle(query.getTitle() != null ? query.getTitle().trim() : query.getTitle());
				queryToSave.setDescription(query.getDescription() != null ? query.getDescription().trim() : query.getDescription());
				queryToSave.setQueryText(query.getQueryText());
				
				entityManager.flush();
			}
			else {
				throw new QueryAlreadyExistsRepositoryException(Constants.QUERY_EXISTS_ERROR_MSG, query);
			}
		}
	
		return queryToSave;
	}

	@Override
	public List<UserQuery> listQueryByUser(String userId) {
		List<UserQuery> queryList = new ArrayList<>();
		
		if(userId != null) {
			// get all the user's queries that have not been deleted
			queryList = entityManager.createNamedQuery(Constants.LIST_QUERY, UserQuery.class)
					.setParameter(CREATED_BY, userId).getResultList();
		}
		
		return queryList;
	}
	
	@Override
	@Transactional
	public void deleteQuery(String userId, int id) throws QueryDoesNotExistRepositoryException {
		
		if(userId != null && !userId.isEmpty() && id > 0) {
			UserQuery query = entityManager.find(UserQuery.class, id);
			
			// check whether the query exists or has not been deleted
			// before deleting
			if(query == null || query.getDeletedDt() != null) {
				throw new QueryDoesNotExistRepositoryException(Constants.QUERY_DOES_NOT_EXIST_ERROR_MSG, null);
			}
			
			// delete the query
			User user = new User();
			user.setUserId(userId);
			
			query.setDeletedDt(new Date());
			query.setDeletedBy(user);
			
			entityManager.flush();
		}
	}
	
	@Override
	public List<Flight> getFlightsByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException {
		List<Flight> flights = new ArrayList<>();
		
		if(queryRequest != null && queryRequest.getQuery() != null) {
			Errors errors = QueryValidationUtils.validateQueryObject(queryRequest.getQuery());
			
			if(errors != null && errors.hasErrors()) {
				String errorMsg = QueryValidationUtils.getErrorString(errors);
				logger.info(errorMsg, new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery()));
				throw new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery());
			}

			try {
				String jpqlQuery = JPQLGenerator.generateQuery(queryRequest.getQuery(), EntityEnum.FLIGHT);
				TypedQuery<Flight> query = entityManager.createQuery(jpqlQuery, Flight.class);
				MutableInt positionalParameter = new MutableInt();
				setJPQLParameters(query, queryRequest.getQuery(), positionalParameter);
				
				// if page size is less than zero, return all flight result
				if(queryRequest.getPageSize() < 0) {
					logger.info("Getting all flights with this query: " + jpqlQuery);
					flights = query.getResultList();
					
					logger.info("Number of Flights returned: " + (flights != null ? flights.size() : "Flight result is null"));
				} else {
					// paginate results
			        int pageNumber = queryRequest.getPageNumber();
			        int pageSize = queryRequest.getPageSize();
			        int firstResultIndex = (pageNumber - 1) * pageSize;
			        
					logger.info("Getting " + pageSize + " flights with this query: " + jpqlQuery);
					query.setFirstResult(firstResultIndex);
					query.setMaxResults(pageSize);
					flights = query.getResultList();
					
					logger.info("Number of Flights returned: " + (flights != null ? flights.size() : "Flight result is null"));
				}
			} catch (InvalidQueryRepositoryException | ParseException e) {
				throw new InvalidQueryRepositoryException(e.getMessage(), queryRequest.getQuery());
			}
		}
		
		return flights;
	}

	public long totalFlightsByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException {
		long totalFlights = 0;
		
		if(queryRequest != null && queryRequest.getQuery() != null) {
			Errors errors = QueryValidationUtils.validateQueryObject(queryRequest.getQuery());
			
			if(errors != null && errors.hasErrors()) {
				String errorMsg = QueryValidationUtils.getErrorString(errors);
				logger.info(errorMsg, new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery()));
				throw new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery());
			}
			
			try {
				String jpqlQuery = JPQLGenerator.generateQuery(queryRequest.getQuery(), EntityEnum.FLIGHT);
				jpqlQuery = jpqlQuery.replace(Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias(), 
						Constants.SELECT_COUNT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + Constants.ID + ")");
				logger.info("Getting total flight count with this query: " + jpqlQuery);
				Query query = entityManager.createQuery(jpqlQuery);
				MutableInt positionalParameter = new MutableInt();
				setJPQLParameters(query, queryRequest.getQuery(), positionalParameter);
				totalFlights = (long) query.getSingleResult();
				
				logger.info("Total number of flights: " + totalFlights);
			} catch (InvalidQueryRepositoryException | ParseException e) {
				throw new InvalidQueryRepositoryException(e.getMessage(), queryRequest.getQuery());
			}
		}
		
		return totalFlights;
	}
	
	@Override
	public List<Object[]> getPassengersByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException {
		List<Object[]> result = new ArrayList<>();
		
		if(queryRequest != null && queryRequest.getQuery() != null) {
			Errors errors = QueryValidationUtils.validateQueryObject(queryRequest.getQuery());
			
			if(errors != null && errors.hasErrors()) {
				String errorMsg = QueryValidationUtils.getErrorString(errors);
				logger.info(errorMsg, new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery()));
				throw new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery());
			}
			
			try {
				String jpqlQuery = JPQLGenerator.generateQuery(queryRequest.getQuery(), EntityEnum.PASSENGER);
				TypedQuery<Object[]> query = entityManager.createQuery(jpqlQuery, Object[].class);
				MutableInt positionalParameter = new MutableInt();
				setJPQLParameters(query, queryRequest.getQuery(), positionalParameter);
				
				if(queryRequest.getPageSize() < 0) {
					logger.info("Getting all passengers with this query: " + jpqlQuery);
					result = query.getResultList();
					
					logger.info("Number of Passengers returned: " + (result != null ? result.size() : "Passenger result is null"));
				}
				else {
					// pagination
			        int pageNumber = queryRequest.getPageNumber();
			        int pageSize = queryRequest.getPageSize();
			        int firstResultIndex = (pageNumber - 1) * pageSize;
			        
					logger.info("Getting " + pageSize + " passengers with this query: " + jpqlQuery);
					query.setFirstResult(firstResultIndex);
					query.setMaxResults(pageSize);
					result = query.getResultList();
					
					logger.info("Number of Passengers returned: " + (result != null ? result.size() : "Passenger result is null"));
				}
			} catch (InvalidQueryRepositoryException | ParseException e) {
				throw new InvalidQueryRepositoryException(e.getMessage(), queryRequest.getQuery());
			}
		}
		
		return result;
	}
	
	@Override
	public long totalPassengersByDynamicQuery(QueryRequest queryRequest) throws InvalidQueryRepositoryException {
		long totalPassengers = 0;
		
		if(queryRequest != null && queryRequest.getQuery() != null) {
			Errors errors = QueryValidationUtils.validateQueryObject(queryRequest.getQuery());
			
			if(errors != null && errors.hasErrors()) {
				String errorMsg = QueryValidationUtils.getErrorString(errors);
				logger.info(errorMsg, new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery()));
				throw new InvalidQueryRepositoryException(errorMsg, queryRequest.getQuery());
			}
			
			try {
				/**
				 * JPA doesn't allow count over multiple columns.
				 * Also doesn't allow a subquery in the from clause
				 * hence using resultList.size() to get the total number of passengers
				 */ 
				String jpqlQuery = JPQLGenerator.generateQuery(queryRequest.getQuery(), EntityEnum.PASSENGER);
				logger.info("Getting total passenger count with this query: " + jpqlQuery); 
				Query query = entityManager.createQuery(jpqlQuery);
				MutableInt positionalParameter = new MutableInt();
				setJPQLParameters(query, queryRequest.getQuery(), positionalParameter);
				totalPassengers = query.getResultList().size();
				
				logger.info("Total number of Passengers: " + totalPassengers);
			} catch (InvalidQueryRepositoryException | ParseException e) {
				throw new InvalidQueryRepositoryException(e.getMessage(), queryRequest.getQuery());
			}
		}
		
		return totalPassengers;
	}
	
	private boolean isUniqueTitle(UserQuery query) {
		boolean unique = false;
		
		// check uniqueness of query title for this user
		List<Integer> ids = entityManager.createNamedQuery(Constants.UNIQUE_TITLE_QUERY, Integer.class)
				.setParameter(CREATED_BY, query.getCreatedBy())
				.setParameter(TITLE, query.getTitle() != null ? query.getTitle().trim() : query.getTitle()).getResultList();
		
		if(ids == null || ids.size() == 0) {
			unique = true;
		}
		
		return unique;
	}
	
	private void setJPQLParameters(Query query, QueryEntity queryEntity, MutableInt positionalParameter) throws ParseException {
		QueryObject queryObject = null;
		QueryTerm queryTerm = null;
		
		if(queryEntity instanceof QueryObject) {
			queryObject = (QueryObject) queryEntity;
			
			List<QueryEntity> rules = queryObject.getRules();
			
			for(QueryEntity rule : rules) {
				setJPQLParameters(query, rule, positionalParameter);
			}
		}
		else if(queryEntity instanceof QueryTerm) {
			queryTerm = (QueryTerm) queryEntity;
			
			String field = queryTerm.getField();
			String type = queryTerm.getType();
			String operator = queryTerm.getOperator();
			String value = (queryTerm.getValue() != null && queryTerm.getValue().length == 1) ? queryTerm.getValue()[0]:null;
			EntityEnum entityEnum = EntityEnum.getEnum(queryTerm.getEntity());
			
			// These four operators don't have any value ex. where firstname IS NULL
			// field isRuleHit doesn't have any value either
			if(!OperatorEnum.IS_EMPTY.toString().equalsIgnoreCase(operator) &&
				!OperatorEnum.IS_NOT_EMPTY.toString().equalsIgnoreCase(operator) &&
				!OperatorEnum.IS_NULL.toString().equalsIgnoreCase(operator) &&
				!OperatorEnum.IS_NOT_NULL.toString().equalsIgnoreCase(operator) &&
				!(entityEnum == EntityEnum.HITS && (field.equalsIgnoreCase(Constants.IS_RULE_HIT) || 
						field.equalsIgnoreCase(Constants.IS_WATCHLIST_HIT)))) {
				
				positionalParameter.increment();
				
				if(OperatorEnum.BETWEEN.toString().equalsIgnoreCase(operator) ) {
					List<String> values = null;
					
					if(queryTerm.getValue() != null && queryTerm.getValue().length > 0) {
						values = Arrays.asList(queryTerm.getValue());
					}
					
					if(values != null && values.size() == 2) {
						
						if(TypeEnum.INTEGER.toString().equalsIgnoreCase(type)) {
							if(entityEnum == EntityEnum.HITS && field.equalsIgnoreCase(Constants.HITS_ID)) {
								query.setParameter(positionalParameter.intValue(), Long.parseLong(values.get(0)));
								positionalParameter.increment();
								query.setParameter(positionalParameter.intValue(), Long.parseLong(values.get(1)));
							} else {
								query.setParameter(positionalParameter.intValue(), Integer.parseInt(values.get(0)));
								positionalParameter.increment();
								query.setParameter(positionalParameter.intValue(), Integer.parseInt(values.get(1)));
							}
						}
						else if(TypeEnum.DOUBLE.toString().equalsIgnoreCase(type)) {
							query.setParameter(positionalParameter.intValue(), Double.parseDouble(values.get(0)));
							positionalParameter.increment();
							query.setParameter(positionalParameter.intValue(), Double.parseDouble(values.get(1)));
						}
						else if(TypeEnum.DATE.toString().equalsIgnoreCase(type)) {
							query.setParameter(positionalParameter.intValue(), sdf.parse(values.get(0)), TemporalType.DATE);
							positionalParameter.increment();
							query.setParameter(positionalParameter.intValue(), sdf.parse(values.get(1)), TemporalType.DATE);
						}
						else if(TypeEnum.DATETIME.toString().equalsIgnoreCase(type)) {
							query.setParameter(positionalParameter.intValue(), dtFormat.parse(values.get(0)), TemporalType.DATE);
							positionalParameter.increment();
							query.setParameter(positionalParameter.intValue(), dtFormat.parse(values.get(1)), TemporalType.DATE);
						}
						else {
							query.setParameter(positionalParameter.intValue(), values.get(0));
							positionalParameter.increment();
							query.setParameter(positionalParameter.intValue(), values.get(1));
						}
					}
				}
				else if(OperatorEnum.IN.toString().equalsIgnoreCase(operator) ||
						OperatorEnum.NOT_IN.toString().equalsIgnoreCase(operator)) {
					List<String> values = null;
					
					if(queryTerm.getValue() != null && queryTerm.getValue().length > 0) {
						values = Arrays.asList(queryTerm.getValue());
					}
					
					if(TypeEnum.INTEGER.toString().equalsIgnoreCase(type)) {
						if(entityEnum == EntityEnum.HITS && field.equalsIgnoreCase(Constants.HITS_ID)) {
							
							List<Long> vals = new ArrayList<>();
							if(values != null) {
								for(String val : values) {
									vals.add(Long.parseLong(val));
								}
							}
							query.setParameter(positionalParameter.intValue(), vals);
						} else {
							List<Integer> vals = new ArrayList<>();
							if(values != null) {
								for(String val : values) {
									vals.add(Integer.parseInt(val));
								}
							}
							query.setParameter(positionalParameter.intValue(), vals);
						}
					}
					else if(TypeEnum.DOUBLE.toString().equalsIgnoreCase(type)) {
						List<Double> vals = new ArrayList<>();
						if(values != null) {
							for(String val : values) {
								vals.add(Double.parseDouble(val));
							}
						}
						query.setParameter(positionalParameter.intValue(), vals);
					}
					else if(TypeEnum.DATE.toString().equalsIgnoreCase(type)) {
						List<Date> vals = new ArrayList<>();
						if(values != null) {
							for(String val : values) {
								vals.add(sdf.parse(val));
							}
						}
						query.setParameter(positionalParameter.intValue(), vals);
					}
					else if(TypeEnum.DATETIME.toString().equalsIgnoreCase(type)) {
						List<Date> vals = new ArrayList<>();
						if(values != null) {
							for(String val : values) {
								vals.add(dtFormat.parse(val));
							}
						}
						query.setParameter(positionalParameter.intValue(), vals);
					}
					else {
						query.setParameter(positionalParameter.intValue(), values);
					}
				}
				else if(OperatorEnum.BEGINS_WITH.toString().equalsIgnoreCase(operator) || 
						OperatorEnum.NOT_BEGINS_WITH.toString().equalsIgnoreCase(operator) ) {
					query.setParameter(positionalParameter.intValue(), value + PERCENT_SIGN);
				}
				else if(OperatorEnum.CONTAINS.toString().equalsIgnoreCase(operator) ||
						OperatorEnum.NOT_CONTAINS.toString().equalsIgnoreCase(operator) ) {
					query.setParameter(positionalParameter.intValue(), PERCENT_SIGN + value + PERCENT_SIGN);
				}
				else if(OperatorEnum.ENDS_WITH.toString().equalsIgnoreCase(operator) ||
						OperatorEnum.NOT_ENDS_WITH.toString().equalsIgnoreCase(operator) ) {
					query.setParameter(positionalParameter.intValue(), PERCENT_SIGN + value);
				}
				else {
					if(TypeEnum.INTEGER.toString().equalsIgnoreCase(type)) {
						if(entityEnum == EntityEnum.HITS && field.equalsIgnoreCase(Constants.HITS_ID)) {
							query.setParameter(positionalParameter.intValue(), Long.parseLong(value));
						} else {
							query.setParameter(positionalParameter.intValue(), Integer.parseInt(value));
						}
					}
					else if(TypeEnum.DOUBLE.toString().equalsIgnoreCase(type)) {
						query.setParameter(positionalParameter.intValue(), Double.parseDouble(value));
					}
					else if(TypeEnum.DATE.toString().equalsIgnoreCase(type)) {
						query.setParameter(positionalParameter.intValue(), sdf.parse(value), TemporalType.DATE);
					}
					else if(TypeEnum.DATETIME.toString().equalsIgnoreCase(type)) {
						query.setParameter(positionalParameter.intValue(), dtFormat.parse(value), TemporalType.DATE);
					}
					else {
					    query.setParameter(positionalParameter.intValue(), value);
					}
				}
			}
		}
	}

}
