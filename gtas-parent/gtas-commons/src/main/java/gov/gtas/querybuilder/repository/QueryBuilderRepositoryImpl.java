package gov.gtas.querybuilder.repository;

import gov.gtas.model.Flight;
import gov.gtas.model.Gender;
import gov.gtas.model.Traveler;
import gov.gtas.model.User;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.enums.OperatorEnum;
import gov.gtas.querybuilder.enums.TypeEnum;
import gov.gtas.querybuilder.exceptions.InvalidQueryObjectException;
import gov.gtas.querybuilder.exceptions.QueryDoesNotExistRepositoryException;
import gov.gtas.querybuilder.exceptions.QueryNotUniqueRepositoryException;
import gov.gtas.querybuilder.model.UserQuery;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

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
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	
	@PersistenceContext 
 	private EntityManager entityManager;

	@Override
	@Transactional
	public UserQuery saveQuery(UserQuery query) throws QueryNotUniqueRepositoryException {
		
		if(query != null) {
			
			// check whether the title is unique before saving
			// you can't have queries with duplicate titles for the same user
			if(isUniqueTitle(query)) {
				query.setId(null);
				query.setTitle(query.getTitle().trim());
				query.setCreatedDt(new Date());
				
				// save query to database
				entityManager.persist(query);
			}
			else {
				throw new QueryNotUniqueRepositoryException();
			}
		}
		
		return query;
	}

	@Override
	@Transactional
	public UserQuery editQuery(UserQuery query) throws QueryNotUniqueRepositoryException, QueryDoesNotExistRepositoryException {
		UserQuery queryToSave = new UserQuery();
		boolean isUniqueTitle = true;
		
		if(query != null && query.getId() != null) {
			
			queryToSave = entityManager.find(UserQuery.class, query.getId());
			
			// check whether the query exists or has not been deleted
			// before updating
			if(queryToSave == null || queryToSave.getDeletedDt() != null) {
				throw new QueryDoesNotExistRepositoryException();
			}
			
			// check if the query's title is unique
			if(!query.getTitle().trim().equalsIgnoreCase(queryToSave.getTitle().trim())) {
				isUniqueTitle = isUniqueTitle(query);
			}
			
			// update the query
			if(isUniqueTitle) {
				queryToSave.setTitle(query.getTitle().trim());
				queryToSave.setDescription(query.getDescription().trim());
				queryToSave.setQueryText(query.getQueryText());
				
				entityManager.flush();
			}
			else {
				throw new QueryNotUniqueRepositoryException();
			}
		}
	
		return queryToSave;
	}

	@Override
	public List<UserQuery> listQueryByUser(String userId) {
		List<UserQuery> queryList = new ArrayList<>();
		
		if(userId != null) {
			// get all the user's queries that have not been deleted
			queryList = entityManager.createNamedQuery("UserQuery.listQueryByUser", UserQuery.class)
					.setParameter("createdBy", userId).getResultList();
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
				throw new QueryDoesNotExistRepositoryException();
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
	public List<Flight> getFlightsByDynamicQuery(QueryObject queryObject) throws InvalidQueryObjectException, ParseException {
		List<Flight> flights = new ArrayList<>();
		
		if(queryObject != null) {
			Errors errors = QueryValidationUtils.validateQueryObject(queryObject);
			
			if(errors != null && errors.hasErrors()) {
				throw new InvalidQueryObjectException(QueryValidationUtils.getErrorString(errors), queryObject);
			}
			
			String jpqlQuery = generateQuery(queryObject, EntityEnum.FLIGHT);
			logger.info("Getting Flights by this query: " + jpqlQuery);
			TypedQuery<Flight> query = entityManager.createQuery(jpqlQuery, Flight.class);
			MutableInt positionalParameter = new MutableInt();
			setJPQLParameters(query, queryObject, positionalParameter);
			
			flights = query.getResultList();
			
			logger.info("Number of Flights returned: " + (flights != null ? flights.size() : "Flight result is null"));
		}
		
		return flights;
	}

	@Override
	public List<Traveler> getPassengersByDynamicQuery(QueryObject queryObject) throws InvalidQueryObjectException, ParseException {
		List<Traveler> passengers = new ArrayList<>();
		
		if(queryObject != null) {
			Errors errors = QueryValidationUtils.validateQueryObject(queryObject);
			
			if(errors != null && errors.hasErrors()) {
				throw new InvalidQueryObjectException(QueryValidationUtils.getErrorString(errors), queryObject);
			}
			
			String jpqlQuery = generateQuery(queryObject, EntityEnum.PAX);
			logger.info("Getting Passengers by this query: " + jpqlQuery);
			TypedQuery<Traveler> query = entityManager.createQuery(jpqlQuery, Traveler.class);
			MutableInt positionalParameter = new MutableInt();
			setJPQLParameters(query, queryObject, positionalParameter);
			
			passengers = query.getResultList();
			
			logger.info("Number of Passengers returned: " + (passengers != null ? passengers.size() : "Passenger result is null"));
		}
		
		return passengers;
	}
	
	private boolean isUniqueTitle(UserQuery query) {
		boolean unique = false;
		
		// check uniqueness of query title for this user
		List<Integer> ids = entityManager.createNamedQuery("UserQuery.checkUniqueTitle", Integer.class)
				.setParameter("createdBy", query.getCreatedBy())
				.setParameter("title", query.getTitle().trim()).getResultList();
		
		if(ids == null || ids.size() == 0) {
			unique = true;
		}
		
		return unique;
	}
	
	/**
	 * 
	 * @param queryObject
	 * @param queryType
	 * @return
	 */
	private String generateQuery(QueryObject queryObject, EntityEnum queryType) {
		String query = "";
		
		if(queryObject != null && queryType != null) {
			String queryPrefix = "";
			StringBuilder join = new StringBuilder();
			StringBuilder where = new StringBuilder();
			MutableInt positionalParameter = new MutableInt();
			MutableInt level = new MutableInt();
			
			logger.debug("Parsing QueryObject...");
			parseQueryObject(queryObject, queryType, join, where, positionalParameter, level);
			logger.debug("Finished Parsing QueryObject");
			
			if(queryType == EntityEnum.FLIGHT) {
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias();
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			else if(queryType == EntityEnum.PAX) {
				String flightsJoinStmt = " join fetch " + EntityEnum.PAX.getAlias() + ".flights " + EntityEnum.FLIGHT.getAlias();
				String documentJoinStmt = " join fetch " + EntityEnum.PAX.getAlias() + ".documents " + EntityEnum.DOCUMENT.getAlias();
				
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.PAX.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.PAX.getEntityName() + " " + EntityEnum.PAX.getAlias();
				
				if(join.length() == 0) {
					join.append(flightsJoinStmt);
					join.append(documentJoinStmt);
				}
				else {
					if(!join.toString().contains("flights")) {
						join.append(flightsJoinStmt);
					}
					else {
						String condition = " join " + EntityEnum.PAX.getAlias() + ".flights ";
						String joinFetchCondition = " join fetch " + EntityEnum.PAX.getAlias() + ".flights ";
						int startIndex = join.indexOf(condition);
						join.replace(startIndex, (startIndex + condition.length()), joinFetchCondition);
					}
						
					
					if(!join.toString().contains("documents")) {
						join.append(documentJoinStmt);
					}
					else {
						String condition = " join " + EntityEnum.PAX.getAlias() + ".documents ";
						String joinFetchCondition = " join fetch " + EntityEnum.PAX.getAlias() + ".documents ";
						int startIndex = join.indexOf(condition);
						join.replace(startIndex, (startIndex + condition.length()), joinFetchCondition);
						
						// if this is a passenger query, you don't need the passenger join again because that is already part of the queryPrefix
						// so remove it
						condition = " join " + EntityEnum.FLIGHT.getAlias() + ".passengers " + EntityEnum.PAX.getAlias();
						startIndex = join.indexOf(condition);
						join.replace(startIndex, (startIndex + condition.length()), "");
					}
				}
				
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			
			logger.info("Parsed Query: " + query);
		}
		
		return query;
	}
	
	private void parseQueryObject(QueryEntity queryEntity, EntityEnum queryType, StringBuilder join, StringBuilder where, MutableInt positionalParameter, MutableInt level) {
		QueryObject queryObject = null;
		QueryTerm queryTerm = null;
		String condition = null;
		
		if(queryEntity instanceof QueryObject) {
			queryObject = (QueryObject) queryEntity;
			condition = queryObject.getCondition();
			level.increment();
			
			List<QueryEntity> rules = queryObject.getRules();
			
			if(level.intValue() > 1) {
				where.append("(");
			}
			
			int index = 0;
			for(QueryEntity rule : rules) {
				
				if(index > 0) {
					where.append(" " + condition + " ");
				}
				parseQueryObject(rule, queryType, join, where, positionalParameter, level);
				index++;
			}
						
			if(level.intValue() > 1) {
				where.append(")");
				level.setValue(1);
			}
		}
		else if(queryEntity instanceof QueryTerm) {
			queryTerm = (QueryTerm) queryEntity;
			
			String entity = queryTerm.getEntity();
			String field = queryTerm.getField();
			String operator = queryTerm.getOperator();
			
			positionalParameter.increment();
			
			// These four operators don't have any value ex. where firstname IS NULL
			if(OperatorEnum.IS_EMPTY.toString().equalsIgnoreCase(operator) ||
					OperatorEnum.IS_NOT_EMPTY.toString().equalsIgnoreCase(operator) ||
					OperatorEnum.IS_NULL.toString().equalsIgnoreCase(operator) ||
					OperatorEnum.IS_NOT_NULL.toString().equalsIgnoreCase(operator)) {
				where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator());
			}
			else if(OperatorEnum.BETWEEN.toString().equalsIgnoreCase(operator) ) {
				List<String> values = Arrays.asList(queryTerm.getValues());
				
				if(values != null && values.size() == 2) {
					
					where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " ?" + positionalParameter);
					positionalParameter.increment();
					where.append(" " + Constants.AND + " ?" + positionalParameter);
				}
			}
			else {
				where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " ?" + positionalParameter);
			}
			
			if(entity != null && !queryType.getEntityName().equalsIgnoreCase(entity)) { 
				String joinCondition = "";
				
				if(entity.equalsIgnoreCase(EntityEnum.DOCUMENT.getEntityName())) {
					joinCondition = getJoinCondition(EntityEnum.PAX);
					
					if(join.indexOf(joinCondition) == -1) {
						join.append(joinCondition);
					}
				}
				
				joinCondition = getJoinCondition(EntityEnum.valueOf(entity.toUpperCase()));
				if(join.indexOf(joinCondition) == -1) {
					join.append(getJoinCondition(EntityEnum.valueOf(entity.toUpperCase())));
				}
			}
		}
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
			String value = queryTerm.getValue();
			
			positionalParameter.increment();
			
			// These four operators don't have any value ex. where firstname IS NULL
			if(!OperatorEnum.IS_EMPTY.toString().equalsIgnoreCase(operator) &&
				!OperatorEnum.IS_NOT_EMPTY.toString().equalsIgnoreCase(operator) &&
				!OperatorEnum.IS_NULL.toString().equalsIgnoreCase(operator) &&
				!OperatorEnum.IS_NOT_NULL.toString().equalsIgnoreCase(operator)) {
				
				if(OperatorEnum.BETWEEN.toString().equalsIgnoreCase(operator) ) {
					List<String> values = Arrays.asList(queryTerm.getValues());
					
					if(values != null && values.size() == 2) {
						
						if(TypeEnum.INTEGER.toString().equalsIgnoreCase(type)) {
							query.setParameter(positionalParameter.intValue(), Integer.parseInt(values.get(0)));
							positionalParameter.increment();
							query.setParameter(positionalParameter.intValue(), Integer.parseInt(values.get(1)));
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
				else if(OperatorEnum.IN.toString().equalsIgnoreCase(operator)) {
					List<String> values = Arrays.asList(queryTerm.getValues());
					StringBuilder valueStr = new StringBuilder();
					int index = 0;
					
					for(String val : values) {
						if(index > 0) {
							valueStr.append(", ");
						}
						valueStr.append(val);
						index++;
					}
					query.setParameter(positionalParameter.intValue(), valueStr);
				}
				else if(OperatorEnum.BEGINS_WITH.toString().equalsIgnoreCase(operator) || 
						OperatorEnum.NOT_BEGINS_WITH.toString().equalsIgnoreCase(operator) ) {
					query.setParameter(positionalParameter.intValue(), value + "%");
				}
				else if(OperatorEnum.CONTAINS.toString().equalsIgnoreCase(operator) ||
						OperatorEnum.NOT_CONTAINS.toString().equalsIgnoreCase(operator) ) {
					query.setParameter(positionalParameter.intValue(), "%" + value + "%");
				}
				else if(OperatorEnum.ENDS_WITH.toString().equalsIgnoreCase(operator) ||
						OperatorEnum.NOT_ENDS_WITH.toString().equalsIgnoreCase(operator) ) {
					query.setParameter(positionalParameter.intValue(), "%" + value);
				}
				else {
					if(field != null && field.equalsIgnoreCase("gender")) {
						if(value != null && value.equalsIgnoreCase("Female")) {
							query.setParameter(positionalParameter.intValue(), Gender.F);
						} else {
							query.setParameter(positionalParameter.intValue(), Gender.M);
						}
					}
					else {
						if(TypeEnum.INTEGER.toString().equalsIgnoreCase(type)) {
							query.setParameter(positionalParameter.intValue(), Integer.parseInt(value));
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
	
	private String getJoinCondition(EntityEnum entity) {
		String joinCondition = "";
		
		switch (entity.getEntityName().toUpperCase()) {
			case Constants.FLIGHT:
				joinCondition = " join " + EntityEnum.PAX.getAlias() + ".flights " + EntityEnum.FLIGHT.getAlias();
				break;
	        case Constants.PAX:
	        	joinCondition = " join " + EntityEnum.FLIGHT.getAlias() + ".passengers " + EntityEnum.PAX.getAlias();
	        	break;
	        case Constants.DOCUMENT:
	        	joinCondition = " join " + EntityEnum.PAX.getAlias() + ".documents " + EntityEnum.DOCUMENT.getAlias();
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid Entity");
		}
		
		return joinCondition;
	}
}
