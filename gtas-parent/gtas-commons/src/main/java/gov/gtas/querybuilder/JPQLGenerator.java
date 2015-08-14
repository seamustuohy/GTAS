package gov.gtas.querybuilder;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.OperatorEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the QueryEntity and generates a JPQL Statement
 *
 */
public class JPQLGenerator {
	private static final Logger logger = LoggerFactory.getLogger(JPQLGenerator.class);
	
	/**
	 * 
	 * @param queryObject
	 * @param queryType
	 * @return
	 * @throws InvalidQueryRepositoryException
	 */
	public static String generateQuery(QueryEntity queryEntity, EntityEnum queryType) throws InvalidQueryRepositoryException {
		String query = "";
		
		if(queryEntity != null && queryType != null) {
			String queryPrefix = "";
			Set<EntityEnum> joinEntities = new HashSet<>();
			StringBuilder where = new StringBuilder();
			StringBuilder join = new StringBuilder();
			MutableInt positionalParameter = new MutableInt();
			MutableInt level = new MutableInt();
			
			logger.debug("Parsing QueryObject...");
			generateWhereCondition(queryEntity, queryType, joinEntities, where, positionalParameter, level);
			logger.info("where: " + where);
			logger.debug("Finished Parsing QueryObject");
			
			if(queryType == EntityEnum.FLIGHT) {
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias();
				generateJoinCondition(joinEntities, queryType);
//				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			else if(queryType == EntityEnum.PASSENGER) {
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.PASSENGER.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.PASSENGER.getEntityName() + " " + EntityEnum.PASSENGER.getAlias();
				generateJoinCondition(joinEntities, queryType);
//				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			
			}
			
			logger.info("Parsed Query: " + query);
		}
		
		return query;
	}
	
	/**
	 * This method parses the query entity and generates the where clause of the query
	 * @param queryEntity contains the user's ad-hoc query
	 * @param queryType indicates whether the user is querying against the flight or passenger data
	 * @param joinEntities contains the list of entities that will later be used to generate the join condition
	 * @param where the generated where clause
	 * @param positionalParameter parameter's position in where clause
	 * @param level used to group conditions
	 * @throws InvalidQueryRepositoryException
	 */
	private static void generateWhereCondition(QueryEntity queryEntity, EntityEnum queryType, Set<EntityEnum> joinEntities, StringBuilder where, 
		MutableInt positionalParameter, MutableInt level) throws InvalidQueryRepositoryException {
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
				generateWhereCondition(rule, queryType, joinEntities, where, positionalParameter, level);
				index++;
			}
						
			if(level.intValue() > 1) {
				where.append(")");
			}
		}
		else if(queryEntity instanceof QueryTerm) {
			queryTerm = (QueryTerm) queryEntity;
			
			String entity = queryTerm.getEntity();
			String field = queryTerm.getField();
			String operator = queryTerm.getOperator();
			
			// add entity to Set which will later be used for
			// generating the join condition
			joinEntities.add(EntityEnum.getEnum(entity));
			
			positionalParameter.increment(); // parameter position in the query
			
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
		}
		
	}
	
	private static String generateJoinCondition(Set<EntityEnum> entity, EntityEnum queryType) throws InvalidQueryRepositoryException {
		StringBuilder joinCondition = new StringBuilder();
		
		if(entity == null) {
			throw new InvalidQueryRepositoryException("No Entity specified for join", null);
		}
			
		Iterator<EntityEnum> it = entity.iterator();
		
		while(it.hasNext()) {
			EntityEnum entityEnum = it.next();
			
			switch (entityEnum.getEntityName().toUpperCase()) {
				case Constants.ADDRESS:
					joinCondition.append(Constants.JOIN + EntityEnum.PNR.getAlias() + EntityEnum.ADDRESS.getEntityReference() + " " + EntityEnum.ADDRESS.getAlias());
					break;
				case Constants.CREDITCARD:
					joinCondition.append(Constants.JOIN + EntityEnum.PNR.getAlias() + EntityEnum.CREDIT_CARD.getEntityReference() + " " + EntityEnum.CREDIT_CARD.getAlias());
					break;
				case Constants.DOCUMENT:
					joinCondition.append(Constants.LEFT_JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.DOCUMENT.getEntityReference() + " " + EntityEnum.DOCUMENT.getAlias());
		            break;
				case Constants.EMAIL:
					joinCondition.append(Constants.JOIN + EntityEnum.PNR.getAlias() + EntityEnum.EMAIL.getEntityReference() + " " + EntityEnum.EMAIL.getAlias());
					break;
				case Constants.FLIGHT:
					joinCondition.append(Constants.JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.FLIGHT.getEntityReference() + " " + EntityEnum.FLIGHT.getAlias());
					break;
				case Constants.FREQUENTFLYER:
					joinCondition.append(Constants.JOIN + EntityEnum.PNR.getAlias() + EntityEnum.FREQUENT_FLYER.getEntityReference() + " " + EntityEnum.FREQUENT_FLYER.getAlias());
					break;
				case Constants.HITS:
					joinCondition.append("");
					break;
		        case Constants.PASSENGER:
		        	joinCondition.append(Constants.JOIN + EntityEnum.FLIGHT.getAlias() + EntityEnum.PASSENGER.getEntityReference() + " " + EntityEnum.PASSENGER.getAlias());
		        	break;
		        case Constants.PHONE:
		        	joinCondition.append(Constants.JOIN + EntityEnum.PNR.getAlias() + EntityEnum.PHONE.getEntityReference() + " " + EntityEnum.PHONE.getAlias());
		        	break;
		        case Constants.PNR:
		        	if(queryType == EntityEnum.FLIGHT) {
		        		joinCondition.append(Constants.JOIN + EntityEnum.FLIGHT.getAlias() + EntityEnum.PNR.getEntityReference() + " " + EntityEnum.PNR.getAlias());
		        	} else if(queryType == EntityEnum.PASSENGER) {
		        		joinCondition.append(Constants.JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.PNR.getEntityReference() + " " + EntityEnum.PNR.getAlias());
		        	}
		        	break;
		        case Constants.TRAVELAGENCY:
		        	joinCondition.append(Constants.JOIN + EntityEnum.PNR.getAlias() + EntityEnum.TRAVEL_AGENCY.getEntityReference() + " " + EntityEnum.TRAVEL_AGENCY.getAlias());
		        	break;
		        default:
		            throw new InvalidQueryRepositoryException("Invalid Entity: " + entityEnum.getEntityName(), null);
			}
		}
		
		return joinCondition.toString();
	}
}
