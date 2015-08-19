package gov.gtas.querybuilder;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.OperatorEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.exceptions.InvalidQueryRepositoryException;

import java.util.ArrayList;
import java.util.Arrays;
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
			List<EntityEnum> joinEntities = new ArrayList<>();
			StringBuilder where = new StringBuilder();
			String join = "";
			MutableInt positionalParameter = new MutableInt();
			MutableInt level = new MutableInt();
			
			logger.debug("Parsing QueryObject...");
			
			generateWhereCondition(queryEntity, queryType, joinEntities, where, positionalParameter, level);
			
			if(queryType == EntityEnum.FLIGHT) {
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias();
				
				if(!joinEntities.isEmpty()) {
					
					// remove Flight from the List because it is already
					// part of the queryPrefix statement
					joinEntities.remove(EntityEnum.FLIGHT);
					
					// join with Passenger because all remaining entities
					// require a join with Passenger
					if(!joinEntities.isEmpty()) {
						joinEntities.remove(EntityEnum.PASSENGER);
						joinEntities.add(0, EntityEnum.PASSENGER);
						
						// add join to PNR if there is a PNR
						// entity in the query
						if(hasPNREntity(joinEntities)) {
							joinEntities.remove(EntityEnum.PNR);
							joinEntities.add(1, EntityEnum.PNR);
							
							// remove join to Agency entity because it is
							// a ManyToOne relationship with PNR
							joinEntities.remove(EntityEnum.TRAVEL_AGENCY);
						}
					}
					
					join = generateJoinCondition(joinEntities, queryType);
				}
				
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			else if(queryType == EntityEnum.PASSENGER) {
				queryPrefix = Constants.SELECT + " " + EntityEnum.PASSENGER.getAlias() + ", " + EntityEnum.FLIGHT.getAlias() + ", " + EntityEnum.DOCUMENT.getAlias() + " " + 
						Constants.FROM + " " + EntityEnum.PASSENGER.getEntityName() + " " + EntityEnum.PASSENGER.getAlias() +
						Constants.LEFT_JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.FLIGHT.getEntityReference() + " " + EntityEnum.FLIGHT.getAlias() + 
						Constants.LEFT_JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.DOCUMENT.getEntityReference() + " " + EntityEnum.DOCUMENT.getAlias();
						
				if(joinEntities != null && !joinEntities.isEmpty()) {
					
					// remove Flight, Passenger, and Document from the List because it is already
					// part of the queryPrefix statement
					joinEntities.remove(EntityEnum.FLIGHT);
					joinEntities.remove(EntityEnum.PASSENGER);
					joinEntities.remove(EntityEnum.DOCUMENT);
					
					// add join to PNR if there is a PNR
					// entity in the query
					if(hasPNREntity(joinEntities)) {
						joinEntities.remove(EntityEnum.PNR);
						joinEntities.add(0, EntityEnum.PNR);
						
						// remove join to Agency entity because it has
						// a ManyToOne relationship with PNR
						joinEntities.remove(EntityEnum.TRAVEL_AGENCY);
					}
					
					join = generateJoinCondition(joinEntities, queryType);
				}
				
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
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
	private static void generateWhereCondition(QueryEntity queryEntity, EntityEnum queryType, List<EntityEnum> joinEntities, StringBuilder where, 
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
			
			// add entity to data structure if not already present
			// will later be used for generating the join condition
			if(!joinEntities.contains(EntityEnum.getEnum(entity))) {
				joinEntities.add(EntityEnum.getEnum(entity));
			}
			
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
	
	private static String generateJoinCondition(List<EntityEnum> entity, EntityEnum queryType) throws InvalidQueryRepositoryException {
		StringBuilder joinCondition = new StringBuilder();
		
		if(entity == null) {
			throw new InvalidQueryRepositoryException("No Entity specified for join", null);
		}
			
		Iterator<EntityEnum> it = entity.iterator();
		
		while(it.hasNext()) {
			EntityEnum entityEnum = it.next();
			
			joinCondition.append(getJoinCondition(entityEnum, queryType));
		}
		
		return joinCondition.toString();
	}
	
	private static String getJoinCondition(EntityEnum entity, EntityEnum queryType) throws InvalidQueryRepositoryException {
		String joinCondition = "";
		
		switch (entity.getEntityName().toUpperCase()) {
			case Constants.ADDRESS:
				joinCondition = Constants.LEFT_JOIN + EntityEnum.PNR.getAlias() + EntityEnum.ADDRESS.getEntityReference() + " " + EntityEnum.ADDRESS.getAlias();
				break;
			case Constants.CREDITCARD:
				joinCondition = Constants.LEFT_JOIN + EntityEnum.PNR.getAlias() + EntityEnum.CREDIT_CARD.getEntityReference() + " " + EntityEnum.CREDIT_CARD.getAlias();
				break;
			case Constants.DOCUMENT:
				joinCondition = Constants.LEFT_JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.DOCUMENT.getEntityReference() + " " + EntityEnum.DOCUMENT.getAlias();
	            break;
			case Constants.EMAIL:
				joinCondition = Constants.LEFT_JOIN + EntityEnum.PNR.getAlias() + EntityEnum.EMAIL.getEntityReference() + " " + EntityEnum.EMAIL.getAlias();
				break;
			case Constants.FLIGHT:
				joinCondition = Constants.JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.FLIGHT.getEntityReference() + " " + EntityEnum.FLIGHT.getAlias();
				break;
			case Constants.FREQUENTFLYER:
				joinCondition = Constants.LEFT_JOIN + EntityEnum.PNR.getAlias() + EntityEnum.FREQUENT_FLYER.getEntityReference() + " " + EntityEnum.FREQUENT_FLYER.getAlias();
				break;
			case Constants.HITS:
				joinCondition = "";
				break;
	        case Constants.PASSENGER:
	        	joinCondition = Constants.JOIN + EntityEnum.FLIGHT.getAlias() + EntityEnum.PASSENGER.getEntityReference() + " " + EntityEnum.PASSENGER.getAlias();
	        	break;
	        case Constants.PHONE:
	        	joinCondition = Constants.LEFT_JOIN + EntityEnum.PNR.getAlias() + EntityEnum.PHONE.getEntityReference() + " " + EntityEnum.PHONE.getAlias();
	        	break;
	        case Constants.PNR:
	        	if(queryType == EntityEnum.FLIGHT) {
	        		joinCondition = Constants.LEFT_JOIN + EntityEnum.FLIGHT.getAlias() + EntityEnum.PNR.getEntityReference() + " " + EntityEnum.PNR.getAlias();
	        	} else if(queryType == EntityEnum.PASSENGER) {
	        		joinCondition = Constants.LEFT_JOIN + EntityEnum.PASSENGER.getAlias() + EntityEnum.PNR.getEntityReference() + " " + EntityEnum.PNR.getAlias();
	        	}
	        	break;
	        default:
	            throw new InvalidQueryRepositoryException("Invalid Entity: " + entity.getEntityName(), null);
		}
		
		return joinCondition;
	}
	
	private static boolean hasPNREntity(List<EntityEnum> entity) {
		
		if(entity != null && !entity.isEmpty()) {
			Iterator<EntityEnum> it = entity.iterator();
			
			while(it.hasNext()) {
				EntityEnum entityEnum = it.next();
				
				if(entityEnum == EntityEnum.ADDRESS || entityEnum == EntityEnum.CREDIT_CARD ||
					entityEnum == EntityEnum.EMAIL || entityEnum == EntityEnum.FREQUENT_FLYER ||
					entityEnum == EntityEnum.PHONE || entityEnum == EntityEnum.PNR || entityEnum == EntityEnum.TRAVEL_AGENCY) {
					return true;
				}
			}
		}
		
		return false;
	}
}
