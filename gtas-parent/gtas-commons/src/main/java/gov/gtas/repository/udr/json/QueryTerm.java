package gov.gtas.repository.udr.json;

import java.io.Serializable;

public class QueryTerm extends QueryObject {
	
     /**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 6558396573006515297L;
	
	private String entity;
     private String attribute;
     private String operator;
     private Serializable value;
     
     public QueryTerm(String entity, String attr, String op, Serializable val){
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
	public Serializable getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Serializable value) {
		this.value = value;
	}

     
}
