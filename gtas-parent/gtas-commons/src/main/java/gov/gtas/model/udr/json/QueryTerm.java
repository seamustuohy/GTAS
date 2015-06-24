package gov.gtas.model.udr.json;

import gov.gtas.model.udr.json.QueryEntity;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The base query condition term.
 * @author GTAS3 (AB)
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
public class QueryTerm implements QueryEntity {
	
     /**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 6558396573006515297L;
	
	private String entity;
     private String attribute;
     private String operator;
     private ValueObject value;
     
     public QueryTerm(){
    	 
     }
     public QueryTerm(String entity, String attr, String op, ValueObject val){
    	 this.entity = entity;
    	 this.attribute = attr;
    	 this.operator = op;
    	 this.value = val;
     }
     
	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * @return the value
	 */
	public ValueObject getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(ValueObject value) {
		this.value = value;
	}

     
}
