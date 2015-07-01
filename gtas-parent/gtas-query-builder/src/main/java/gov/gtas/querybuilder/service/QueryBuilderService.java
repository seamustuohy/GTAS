package gov.gtas.querybuilder.service;

import gov.gtas.model.BaseEntity;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.repository.QueryBuilderRepository;
import gov.gtas.querybuilder.util.Constants;
import gov.gtas.querybuilder.util.EntityEnum;
import gov.gtas.querybuilder.util.OperatorEnum;
import gov.gtas.querybuilder.util.QueryBuilderUtil;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<? extends BaseEntity> runQuery(QueryObject queryObject, EntityEnum queryType) {
		String query = "";
						
		query = getQuery(queryObject, queryType);
		
		if(query != null && !query.isEmpty()) {
			if(queryType.getName().equalsIgnoreCase(EntityEnum.FLIGHT.getName())) {
				
				return (queryRepository.getFlightsByDynamicQuery(query));
			}
			else if(queryType.getName().equalsIgnoreCase(EntityEnum.PASSENGER.getName())) {
				
				return (queryRepository.getPassengersByDynamicQuery(query));
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	public void saveQuery() {
		
	}
	
	/**
	 * 
	 */
	public void viewQuery() {
		
	}
	
	/**
	 * 
	 */
	public void editQuery() {
		
	}
	
	/**
	 * 
	 */
	public void deleteQuery() {
		
	}
	
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
				queryPrefix = Constants.SELECT_DISTINCT + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.FLIGHT) + 
						" " + Constants.FROM + " " + EntityEnum.FLIGHT.getName() + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.FLIGHT);
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			else if(queryType == EntityEnum.PASSENGER) {
				queryPrefix = Constants.SELECT_DISTINCT + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.PASSENGER) + 
						" " + Constants.FROM + " " + EntityEnum.PASSENGER.getName() + " " + QueryBuilderUtil.getEntityAlias(EntityEnum.PASSENGER);
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			
			logger.info("Parsed Query: " + query);
		}
		
		return query;
	}
	
	private void parseQueryObject(QueryEntity queryEntity, EntityEnum queryType, StringBuilder join, StringBuilder where, MutableInt level) {
		QueryObject queryObject = null;
		QueryTerm queryTerm = null;
		String condition = null;
		
		if(queryEntity instanceof QueryObject) {
			queryObject = (QueryObject) queryEntity;
			condition = queryObject.getCondition();
		}
		else if(queryEntity instanceof QueryTerm) {
			queryTerm = (QueryTerm) queryEntity;
		}
		
		if(condition != null && !condition.isEmpty()) {
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
		else {
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
						
						valueStr.append(type.equalsIgnoreCase("string") ? "'" + value + "'" : value);
					}
				}
			}
			
			if(entity != null && !(queryType.getName().equalsIgnoreCase(entity) || queryType.getName().equalsIgnoreCase(entity))) { 
				String joinCondition = "";
				
				if(entity.equalsIgnoreCase(EntityEnum.DOCUMENT.getName())) {
					joinCondition = QueryBuilderUtil.getJoinCondition(EntityEnum.PASSENGER);
					
					if(join.indexOf(joinCondition) == -1) {
						join.append(joinCondition);
					}
				}
				
				joinCondition = QueryBuilderUtil.getJoinCondition(EntityEnum.valueOf(entity.toUpperCase()));
				if(join.indexOf(joinCondition) == -1) {
					join.append(QueryBuilderUtil.getJoinCondition(EntityEnum.valueOf(entity.toUpperCase())));
				}
			}
			
			where.append(QueryBuilderUtil.getEntityAlias(EntityEnum.valueOf(entity.toUpperCase())) + "." + field + " " + OperatorEnum.getEnum(operator).getValue() + " " + valueStr);
		}
	}
}
