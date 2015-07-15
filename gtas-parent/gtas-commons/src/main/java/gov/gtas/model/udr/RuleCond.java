package gov.gtas.model.udr;

import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.util.DateCalendarUtils;
import gov.gtas.util.ValidationUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "rule_cond")
public class RuleCond implements Serializable {
    
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1969876980229903470L;
	
	private static final String VALUE_NAME_PREFIX = "value";
	
	@EmbeddedId
	private RuleCondPk id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ENTITY_NAME", nullable = false, length = 64)
	private EntityLookupEnum entityName;
	
	@Column(name = "ATTR_NAME", nullable = false, length = 256)
	private String attrName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OP_CODE", nullable = false, length = 16)
	private OperatorCodeEnum opCode;

	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private List<CondValue> values;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="RULE_ID", nullable=false, insertable=false, updatable=false, referencedColumnName="id")     
    private Rule parent;

	public RuleCond() {
	}

	public RuleCond(RuleCondPk id, EntityLookupEnum entityName, String attrName, OperatorCodeEnum opCode) {
		this.id = id;
		this.entityName = entityName;
		this.attrName = attrName;
		this.opCode = opCode;
	}
    /**
     * Set the parent rule's ID to the dependent objects. 
     * @param ruleId
     */
	public void refreshParentRuleId(Long ruleId){
		if(ruleId != null){
			this.getId().setRuleId(ruleId);
			List<CondValue> values = this.getValues();
			if(!CollectionUtils.isEmpty(values)){
				for(CondValue val : values){
					val.getId().setRuleId(ruleId);
				}
			}
		}
	}
	/**
	 * @return the id
	 */
	public RuleCondPk getId() {
		return id;
	}

	/**
     * Adds a value to the rule condition.
     * @param valName the name of the value (e.g., "start date").
     * @param val the value.
     */
    public void  addValuesToCondition(String[] valueArray, ValueTypesEnum type) throws ParseException{
		if(this.values == null){
			this.values = new LinkedList<CondValue>();
		}
		int valIndx = 1;
		for(String val:valueArray){
		     this.values.add(createCondValue(VALUE_NAME_PREFIX+valIndx, type, val));
		     ++valIndx;
		}
    }
    public void  addValueToCondition(String value, ValueTypesEnum type) throws ParseException{
		if(this.values == null){
			this.values = new LinkedList<CondValue>();
		}
		this.values.add(createCondValue(VALUE_NAME_PREFIX, type, value));
    }
    /**
     * Creates a value and adds it to the list of values.
     * @param valName
     * @param val
     * @return
     */
    private CondValue createCondValue(String valName, ValueTypesEnum type, String val) throws ParseException{
 	   CondValuePk pk = new CondValuePk(this.id, valName);
 	   switch(type){
	 	   case STRING:
	 		  return new CondValue(pk, val);
	 	   case INTEGER:
	 		  return new CondValue(pk, Integer.valueOf(val));
	 	   case LONG:
	 		  return new CondValue(pk, Long.valueOf(val));
	 	   case DOUBLE:
	 		  return new CondValue(pk, Double.valueOf(val));
	 	   case DATE:
	 		  return new CondValue(pk, DateCalendarUtils.parseJsonDate(val));
	 	   case TIMESTAMP:
	 		  return new CondValue(pk, DateCalendarUtils.parseJsonDate(val));
	 	   case BOOLEAN:
	 		  return new CondValue(pk, ValidationUtils.isStringTruthy(val)?"Y":"N");
	 	   case OBJECT_REF:
	 		  return new CondValue(pk, val, true);
	 	   default:
	 		  return new CondValue(pk, val);  
 	   }
    }
    
	/**
	 * @return the entityName
	 */
	public EntityLookupEnum getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(EntityLookupEnum entityName) {
		this.entityName = entityName;
	}

	public String getAttrName() {
		return this.attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public OperatorCodeEnum getOpCode() {
		return this.opCode;
	}

	public void setOpCode(OperatorCodeEnum opCode) {
		this.opCode = opCode;
	}

	/**
	 * @return the values
	 */
	public List<CondValue> getValues() {
		return values;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(id);
//		hashCodeBuilder.append(entityName);
//		hashCodeBuilder.append(attrName);
//		hashCodeBuilder.append(opCode);
		return hashCodeBuilder.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RuleCond)) {
			return false;
		}
		RuleCond other = (RuleCond) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(id, other.id);
//		equalsBuilder.append(condSeq, other.condSeq);
//		equalsBuilder.append(entityName, other.entityName);
//		equalsBuilder.append(attrName, other.attrName);
//		equalsBuilder.append(opCode, other.opCode);
		return equalsBuilder.isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("id", id.toString());
		toStringBuilder.append("entityName", entityName);
		toStringBuilder.append("attrName", attrName);
		toStringBuilder.append("opCode", opCode);
		return toStringBuilder.toString();
	}

}
