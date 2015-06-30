package gov.gtas.model.udr.json.util;

import gov.gtas.model.udr.ConditionValueTypeEnum;
import gov.gtas.model.udr.EntityLookupEnum;
import gov.gtas.model.udr.OperatorCodeEnum;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryConditionEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class UdrSpecificationBuilder {
	private Stack<QueryObject> queryObjectStack;
	private Long id;
	private MetaData meta;
	
	public UdrSpecificationBuilder(Long id){
		this.id = id;
	    queryObjectStack = new Stack<QueryObject>();
	}
	public UdrSpecificationBuilder(Long id, QueryConditionEnum condition){
		this.id = id;
	    queryObjectStack = new Stack<QueryObject>();
	    this.addNestedQueryObject(condition);
	}
	public UdrSpecification build(){
		while(queryObjectStack.size() > 1){
			QueryObject obj = queryObjectStack.pop();
			queryObjectStack.peek().getRules().add(obj);
		}
		QueryObject obj = null;
		if(queryObjectStack.size() > 0){
			obj = queryObjectStack.pop();
		}
		return new UdrSpecification(id, obj, meta);
	}
	public UdrSpecificationBuilder addMeta(String title, String descr, Date startDate, Date endDate, boolean enabled, String author){
		meta = new MetaData(title,descr,startDate,author);
		meta.setAuthor(author);
		meta.setEnabled(enabled);
		meta.setEndDate(endDate);
		return this;
	}
	public UdrSpecificationBuilder addTerm(EntityLookupEnum entity, String attr, ConditionValueTypeEnum type, OperatorCodeEnum op, String[] val){
		queryObjectStack.peek().getRules().add(new QueryTerm(entity.toString(),attr,type.toString(),op.toString(),val));
		return this;
	}
	public UdrSpecificationBuilder addNestedQueryObject(QueryConditionEnum condition){
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition(condition.toString());
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
	    queryObject.setRules(rules);
		queryObjectStack.push(queryObject);
		return this;
	}
	public UdrSpecificationBuilder addSiblingQueryObject(QueryConditionEnum condition){
		if(queryObjectStack.size() <= 1){
			throw new RuntimeException("UdrSpecificationBuilder.addSiblingQueryObject() - Cannot add sibling to stack size ="+queryObjectStack.size());
		}
		//first add the previous sibling to parent
		QueryObject lastChild = queryObjectStack.pop();
		queryObjectStack.peek().getRules().add(lastChild);
		
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition(condition.toString());
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
	    queryObject.setRules(rules);
		queryObjectStack.push(queryObject);
		return this;
	}
    public  UdrSpecification createSampleSpec(){
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate","Date","EQUAL", new String[]{new Date().toString()});
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "String", "EQUAL", new String[]{"Jones"}));

		QueryObject queryObjectEmbedded = new QueryObject();
		queryObjectEmbedded.setCondition("AND");
		List<QueryEntity> rules2 = new LinkedList<QueryEntity>();
		
		QueryTerm trm2 = new QueryTerm("Pax", "embarkation.name","String", "IN", new String[]{"DBY","PKY","FLT"});
		rules2.add(trm2);
		rules2.add(new QueryTerm("Pax", "debarkation.name", "String", "EQUAL", new String[]{"IAD"}));
		queryObjectEmbedded.setRules(rules2);

		queryObject.setRules(rules);
		
		UdrSpecification resp = new UdrSpecification(null, queryObject, new MetaData("Hello Rule 1", "This is a test", new Date(), "jpjones"));
    	return resp;
    }

}
