package gov.gtas.querybuilder.service;

import gov.gtas.model.Flight;
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
import gov.gtas.querybuilder.exceptions.InvalidQueryRequestException;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.exceptions.QueryRepositoryException;
import gov.gtas.querybuilder.model.Query;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

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
	
	@Autowired
	QueryBuilderRepository queryRepository;
	
	/**
	 * 
	 * @param queryObject
	 * @param queryType
	 * @return
	 * @throws InvalidQueryObjectException 
	 */
	public List<Flight> runFlightQuery(QueryObject queryObject, EntityEnum queryType) throws InvalidQueryObjectException {
			
		Errors errors = QueryValidationUtils.validateQueryObject(queryObject, Constants.QUERYOBJECT_OBJECTNAME);
		
		if(errors != null && errors.hasErrors()) {
			throw new InvalidQueryObjectException(QueryValidationUtils.getErrorString(errors), queryObject);
		}

		return queryRepository.getFlightsByDynamicQuery(getQuery(queryObject, queryType));
	}
	
	public List<Traveler> runPassengerQuery(QueryObject queryObject, EntityEnum queryType) throws InvalidQueryObjectException {
		
		Errors errors = QueryValidationUtils.validateQueryObject(queryObject, Constants.QUERYOBJECT_OBJECTNAME);
		
		if(errors != null && errors.hasErrors()) {
			throw new InvalidQueryObjectException(QueryValidationUtils.getErrorString(errors), queryObject);
		}
		
		return queryRepository.getPassengersByDynamicQuery(getQuery(queryObject, queryType));
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws JsonProcessingException 
	 * @throws InvalidQueryObjectException 
	 */
	public Query saveQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, JsonProcessingException, InvalidQueryRequestException {
		Query result = null;
		Errors errors = QueryValidationUtils.validateQueryRequest(queryRequest, Constants.QUERYREQUEST_OBJECTNAME);
		
		if(errors != null && errors.hasErrors()) {
			throw new InvalidQueryRequestException(QueryValidationUtils.getErrorString(errors), queryRequest);
		}
		
		try {
			result = queryRepository.saveQuery(createQuery(queryRequest));
			
		} catch(QueryRepositoryException ex) {
			throw new QueryAlreadyExistsException(Constants.QUERY_EXISTS_ERROR_MSG, queryRequest);
		}
		return result;
	}

	/**
	 * 
	 * @param query
	 * @return
	 * @throws QueryAlreadyExistsException
	 * @throws JsonProcessingException 
	 * @throws InvalidQueryObjectException 
	 */
	public Query editQuery(QueryRequest queryRequest) throws QueryAlreadyExistsException, JsonProcessingException, InvalidQueryRequestException {
		Query result = null;
		Errors errors = QueryValidationUtils.validateQueryRequest(queryRequest, Constants.QUERYREQUEST_OBJECTNAME);
		
		if(errors != null && errors.hasErrors()) {
			throw new InvalidQueryRequestException(QueryValidationUtils.getErrorString(errors), queryRequest);
		}
		
		try {
			result = queryRepository.editQuery(createQuery(queryRequest));
		} catch(QueryRepositoryException ex) {
			throw new QueryAlreadyExistsException(Constants.QUERY_EXISTS_ERROR_MSG, queryRequest);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<Query> listQueryByUser(String userId) {
		
		return queryRepository.listQueryByUser(userId);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Query getQuery(int id) {
		
		return queryRepository.getQuery(id);
	}
	
	/**
	 * 
	 * @param userId
	 * @param id
	 */
	public void deleteQuery(String userId, int id) {
		
		queryRepository.deleteQuery(userId, id);
	}
		

	private Query createQuery(QueryRequest req) throws JsonProcessingException {
		Query query = new Query();
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
	
	/**
	 * 
	 * @param queryObject
	 * @param queryType
	 * @return
	 */
	private String getQuery(QueryObject queryObject, EntityEnum queryType) {
		String query = "";
		
		if(queryObject != null && queryType != null) {
			String queryPrefix = "";
			StringBuilder join = new StringBuilder();
			StringBuilder where = new StringBuilder();
			MutableInt level = new MutableInt();
			
			logger.debug("Parsing QueryObject...");
			parseQueryObject(queryObject, queryType, join, where, level);			
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
	
	/**
	 * 
	 * @param queryEntity
	 * @param queryType
	 * @param join
	 * @param where
	 * @param level
	 */
	private void parseQueryObject(QueryEntity queryEntity, EntityEnum queryType, StringBuilder join, StringBuilder where, MutableInt level) {
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
				parseQueryObject(rule, queryType, join, where, level);
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
			String type = queryTerm.getType();
			
			StringBuilder valueStr = new StringBuilder();
			
			if(operator != null && (operator.equalsIgnoreCase(OperatorEnum.IN.toString()) || 
					operator.equalsIgnoreCase(OperatorEnum.NOT_IN.toString()) || operator.equalsIgnoreCase(OperatorEnum.BETWEEN.toString()))) {
				List<String> values = Arrays.asList(queryTerm.getValues());
				
				if(values != null && values.size() > 0) {
					
					if(operator.equalsIgnoreCase(OperatorEnum.BETWEEN.toString())) {
						// for BETWEEN operator, there should be two values for the range
						if(values.size() == 2) {
							valueStr.append(values.get(0) + " " + Constants.AND + " " + values.get(1));
						}
					}
					else {
						int index = 0;
						valueStr.append("(");
						for(String val : values) {
							if(index > 0) {
								valueStr.append(", ");
							}
							valueStr.append(type.equalsIgnoreCase("string") ? "'" + val + "'" : val);
							index++;
						}
						valueStr.append(")");
					}
				}
			}
			else {
				String value = queryTerm.getValue();
				
				if(value != null) {
					value = value.replaceAll("'", "''");
				}
				
				// These four operators don't have any value ex. where firstname IS NULL
				if(operator != null && !(operator.equalsIgnoreCase(OperatorEnum.IS_EMPTY.toString()) ||
						operator.equalsIgnoreCase(OperatorEnum.IS_NOT_EMPTY.toString()) ||
						operator.equalsIgnoreCase(OperatorEnum.IS_NULL.toString()) ||
						operator.equalsIgnoreCase(OperatorEnum.IS_NOT_NULL.toString()))) {
					
					if(operator != null && (operator.equalsIgnoreCase(OperatorEnum.BEGINS_WITH.toString()) || 
							operator.equalsIgnoreCase(OperatorEnum.NOT_BEGINS_WITH.toString())) ) {
						valueStr.append("'" + value + "%'");
					}
					else if(operator != null && (operator.equalsIgnoreCase(OperatorEnum.CONTAINS.toString()) ||
							operator.equalsIgnoreCase(OperatorEnum.NOT_CONTAINS.toString())) ) {
						valueStr.append("'%" + value + "%'");
					}
					else if(operator != null && (operator.equalsIgnoreCase(OperatorEnum.ENDS_WITH.toString()) ||
							operator.equalsIgnoreCase(OperatorEnum.NOT_ENDS_WITH.toString()))) {
						valueStr.append("'%" + value + "'");
					}
					else {
						valueStr.append(type.equalsIgnoreCase(TypeEnum.STRING.toString()) || 
								type.equalsIgnoreCase(TypeEnum.DATE.toString()) || 
								type.equalsIgnoreCase(TypeEnum.DATETIME.toString())? "'" + value + "'" : value);
					}
				}
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
			
			where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " " + valueStr);
		}
	}
	
	private String getJoinCondition(EntityEnum entity) {
		String joinCondition = "";
		
		switch (entity.getEntityName()) {
			case "Flight":
				joinCondition = " join " + EntityEnum.PAX.getAlias() + ".flights " + EntityEnum.FLIGHT.getAlias();
				break;
	        case "Pax":
	        	joinCondition = " join " + EntityEnum.FLIGHT.getAlias() + ".passengers " + EntityEnum.PAX.getAlias();
	        	break;
	        case "Document":
	        	joinCondition = " join " + EntityEnum.PAX.getAlias() + ".documents " + EntityEnum.DOCUMENT.getAlias();
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid entity");
		}
		
		return joinCondition;
	}
}

