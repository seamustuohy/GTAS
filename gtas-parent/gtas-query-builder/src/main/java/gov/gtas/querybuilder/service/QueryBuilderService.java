package gov.gtas.querybuilder.service;

import gov.gtas.model.BaseEntity;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.exceptions.QueryAlreadyExistsException;
import gov.gtas.querybuilder.model.Query;
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
			if(queryType == EntityEnum.FLIGHT) {
				
				return (queryRepository.getFlightsByDynamicQuery(query));
			}
			else if(queryType == EntityEnum.PAX) {
				
				return (queryRepository.getPassengersByDynamicQuery(query));
			}
		}
		
		return null;
	}
	
	/**
	 * @throws QueryAlreadyExistsException 
	 * 
	 */
	public Query saveQuery(Query query) throws QueryAlreadyExistsException {
		
		return queryRepository.saveQuery(query);
	}
	
	/**
	 * 
	 */
	public List<Query> listQueryByUser(String userId) {
		
		return queryRepository.listQueryByUser(userId);
	}
	
	public Query getQuery(int id) {
		
		return queryRepository.getQuery(id);
	}
	
	/**
	 * @throws QueryAlreadyExistsException 
	 * 
	 */
	public Query editQuery(Query query) throws QueryAlreadyExistsException {
		
		return queryRepository.editQuery(query);
	}
	
	/**
	 * 
	 */
	public void deleteQuery(String userId, int id) {
		
		queryRepository.deleteQuery(userId, id);
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
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.FLIGHT.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.FLIGHT.getEntityName() + " " + EntityEnum.FLIGHT.getAlias();
				query = queryPrefix + join + " " + Constants.WHERE + " " + where;
			}
			else if(queryType == EntityEnum.PAX) {
				queryPrefix = Constants.SELECT_DISTINCT + " " + EntityEnum.PAX.getAlias() + 
						" " + Constants.FROM + " " + EntityEnum.PAX.getEntityName() + " " + EntityEnum.PAX.getAlias();
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
						valueStr.append(type.equalsIgnoreCase("string") || type.equalsIgnoreCase("date") ? "'" + value + "'" : value);
					}
				}
			}
			
			if(entity != null && !(queryType.getEntityName().equalsIgnoreCase(entity) || queryType.getEntityName().equalsIgnoreCase(entity))) { 
				String joinCondition = "";
				
				if(entity.equalsIgnoreCase(EntityEnum.DOCUMENT.getEntityName())) {
					joinCondition = QueryBuilderUtil.getJoinCondition(EntityEnum.PAX);
					
					if(join.indexOf(joinCondition) == -1) {
						join.append(joinCondition);
					}
				}
				
				joinCondition = QueryBuilderUtil.getJoinCondition(EntityEnum.valueOf(entity.toUpperCase()));
				if(join.indexOf(joinCondition) == -1) {
					join.append(QueryBuilderUtil.getJoinCondition(EntityEnum.valueOf(entity.toUpperCase())));
				}
			}
			
			where.append(EntityEnum.getEnum(entity).getAlias() + "." + field + " " + OperatorEnum.getEnum(operator).getOperator() + " " + valueStr);
		}
	}
}

