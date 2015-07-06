package gov.gtas.model.udr.json;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Recursive query condition object.
 * @author GTAS3 (AB)
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class QueryObject implements QueryEntity{
   /**
	 * serial  version UID.
	 */
	private static final long serialVersionUID = -1825443604051080662L;
	
    private String condition;
    private List<QueryEntity> rules;
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	/**
	 * @return the rules
	 */
	public List<QueryEntity> getRules() {
		return rules;
	}
	/**
	 * @param rules the rules to set
	 */
	public void setRules(List<QueryEntity> rules) {
		this.rules = rules;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.model.udr.json.QueryEntity#getFlattenedList()
	 */
	@Override
	public List<List<QueryTerm>> createFlattenedList(){
		List<List<QueryTerm>> flatList = new LinkedList<List<QueryTerm>>();
		final QueryConditionEnum condOp = QueryConditionEnum.valueOf(this.condition);
		if(condOp == QueryConditionEnum.OR){
			for(QueryEntity qtn:this.getRules()){
				flatList.addAll(qtn.createFlattenedList());
			}
		} else if(condOp == QueryConditionEnum.AND){
			for(QueryEntity qtn:this.getRules()){
				if(flatList.isEmpty()){
					flatList.addAll(qtn.createFlattenedList());
				} else {
				   flatList = multiplyFlatLists(flatList, qtn.createFlattenedList());
				}
			}
			
		}else{
			//TODO Error
		}
		return flatList;
	}
	private List<List<QueryTerm>> multiplyFlatLists(List<List<QueryTerm>> list1, List<List<QueryTerm>> list2){
		List<List<QueryTerm>> flatList = new LinkedList<List<QueryTerm>>();
		for(List<QueryTerm> l1:list1){
			for(List<QueryTerm> l2:list2){
				List<QueryTerm> newList = new LinkedList<QueryTerm>();
				newList.addAll(l1);
				newList.addAll(l2);
				flatList.add(newList);
			}
		}
		return flatList;
	}
  
}
