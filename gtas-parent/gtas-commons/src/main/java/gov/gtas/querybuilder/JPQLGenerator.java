package gov.gtas.querybuilder;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.OperatorEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPQLGenerator {
	private static final Logger logger = LoggerFactory.getLogger(JPQLGenerator.class);
	
	/**
	 * 
	 * @param queryObject
	 * @param queryType
	 * @return
	 * @throws InvalidQueryRepositoryException 
	 */
	public static String generateQuery(QueryObject queryObject, EntityEnum queryType) throws InvalidQueryRepositoryException {
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
			else if(queryType == EntityEnum.PASSENGER) {
				String flightsJoinStmt = Constants.JOIN_FETCH + EntityEnum.PASSENGER.getAlias() + Constants.FLIGHT_REF + EntityEnum.FLIGHT.getAlias();
				String documentJoinStmt = Constants.JOIN_FETCH + EntityEnum.PASSENGER.getAlias() + Constants.DOCUMENT_REF + EntityEnum.DOCUMENT.getAlias();
				
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.PASSENGER.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.PASSENGER.getEntityName() + " " + EntityEnum.PASSENGER.getAlias();
				
				if(join.length() == 0) {
					join.append(flightsJoinStmt);
					join.append(documentJoinStmt);
				}
				else {
					if(!join.toString().contains("flights")) {
						join.append(flightsJoinStmt);
					}
					else {
						String condition = Constants.JOIN + EntityEnum.PASSENGER.getAlias() + Constants.FLIGHT_REF;
						String joinFetchCondition = Constants.JOIN_FETCH + EntityEnum.PASSENGER.getAlias() + Constants.FLIGHT_REF;
						int startIndex = join.indexOf(condition);
						join.replace(startIndex, (startIndex + condition.length()), joinFetchCondition);
					}
						
					
					if(!join.toString().contains("documents")) {
						join.append(documentJoinStmt);
					}
					else {
						String condition = Constants.JOIN + EntityEnum.PASSENGER.getAlias() + Constants.DOCUMENT_REF;
						String joinFetchCondition = Constants.JOIN_FETCH + EntityEnum.PASSENGER.getAlias() + Constants.DOCUMENT_REF;
						int startIndex = join.indexOf(condition);
						join.replace(startIndex, (startIndex + condition.length()), joinFetchCondition);
						
						// if this is a passenger query, you don't need the passenger join again because that is already part of the queryPrefix
						// so remove it
						condition = Constants.JOIN + EntityEnum.FLIGHT.getAlias() + Constants.PASSENGER_REF + EntityEnum.PASSENGER.getAlias();
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
	
	private static void parseQueryObject(QueryEntity queryEntity, EntityEnum queryType, StringBuilder join, StringBuilder where, MutableInt positionalParameter, MutableInt level) throws InvalidQueryRepositoryException {
		QueryObject queryObject = null;
		QueryTerm queryTerm = null;
		String condition = null;
		
		if(queryEntity instanceof QueryObject) {
			queryObject = (QueryObject) queryEntity;
			condition = queryObject.getCondition();
			level.increment();
			
			List<QueryEntity> rules = queryObject.getRules();
			
			if(level.intValue() > 0) {
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
						
			if(level.intValue() > 0) {
				where.append(")");
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
				List<String> values = null;
				
				if(queryTerm.getValue() != null && queryTerm.getValue().length > 0) {
					values = Arrays.asList(queryTerm.getValue());
				}
				
				if(values != null && values.size() == 2) {
					
					where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " ?" + positionalParameter);
					positionalParameter.increment();
					where.append(" " + Constants.AND + " ?" + positionalParameter);
				}
			}
			else if(OperatorEnum.IN.toString().equalsIgnoreCase(operator) || 
					OperatorEnum.NOT_IN.toString().equalsIgnoreCase(operator)) {
				where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " (?" + positionalParameter + ")");
			}
			else {
				where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " ?" + positionalParameter);
			}
			
			if(entity != null && !queryType.getEntityName().equalsIgnoreCase(entity)) { 
				String joinCondition = "";
				
				if(entity.equalsIgnoreCase(EntityEnum.DOCUMENT.getEntityName())) {
					joinCondition = getJoinCondition(EntityEnum.PASSENGER);
					
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
	
	private static String getJoinCondition(EntityEnum entity) throws InvalidQueryRepositoryException {
		String joinCondition = "";
		
		switch (entity.getEntityName().toUpperCase()) {
			case Constants.FLIGHT:
				joinCondition = Constants.JOIN + EntityEnum.PASSENGER.getAlias() + Constants.FLIGHT_REF + EntityEnum.FLIGHT.getAlias();
				break;
	        case Constants.PASSENGER:
	        	joinCondition = Constants.JOIN + EntityEnum.FLIGHT.getAlias() + Constants.PASSENGER_REF + EntityEnum.PASSENGER.getAlias();
	        	break;
	        case Constants.DOCUMENT:
	        	joinCondition = Constants.JOIN + EntityEnum.PASSENGER.getAlias() + Constants.DOCUMENT_REF + EntityEnum.DOCUMENT.getAlias();
	            break;
	        default:
	            throw new InvalidQueryRepositoryException("Invalid Entity: " + entity.getEntityName(), null);
		}
		
		return joinCondition;
	}
}
