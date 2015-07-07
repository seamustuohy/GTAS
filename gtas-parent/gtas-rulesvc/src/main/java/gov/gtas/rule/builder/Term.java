package gov.gtas.rule.builder;

import java.util.LinkedList;
import java.util.List;

import gov.gtas.model.udr.enumtype.OperatorCodeEnum;

public final class Term extends QueryTreeNode{
    private String entity;
    private String attribute;
    private OperatorCodeEnum operator;
    private Object value;
    
    /**
     * Constructor.
     * @param entity
     * @param attribute
     * @param op
     * @param value
     */
    public Term(QueryTreeNode parent, String entity, String attribute, OperatorCodeEnum op, Object value){
    	super(parent,CondOpEnum.NONE);
    	this.entity = entity;
    	this.attribute = attribute;
    	this.operator = op;
    	this.value = value;
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
	public OperatorCodeEnum getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(OperatorCodeEnum operator) {
		this.operator = operator;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.QueryTreeNode#getChildren()
	 */
	@Override
	public List<QueryTreeNode> getChildren() {
		return null;
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.QueryTreeNode#addChild(gov.gtas.rule.builder.QueryTreeNode)
	 */
	@Override
	public void addChild(QueryTreeNode child) {
		//TODO throw exception
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.QueryTreeNode#getFlattenedList()
	 */
	@Override
	public List<List<Term>> getFlattenedList() {
		List<Term> thisList = new LinkedList<Term>();
		thisList.add(this);
		List<List<Term>> ret = new LinkedList<List<Term>>();
		ret.add(thisList);
		return ret;
	}    
}
