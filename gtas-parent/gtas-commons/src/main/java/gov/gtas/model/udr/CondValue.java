package gov.gtas.model.udr;

import gov.gtas.model.udr.CondValuePk;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * CondValue
 */
@Entity
@Table(name = "cond_value")
public class CondValue implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 2701321017207668584L;
	//the following two fields are key columns
	@EmbeddedId
	private CondValuePk id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "VAL_TYPE", nullable = false, length = 16)
	private ValueTypesEnum valType;
	
	private BigDecimal numVal;
	private Date dtVal;
	private String charVal;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="RULE_ID", referencedColumnName="RULE_ID", insertable=false, updatable=false),
		@JoinColumn(name="COND_SEQ", referencedColumnName="COND_SEQ", insertable=false, updatable=false)		
	})
	private RuleCond parent;
	
	/**
	 * Constructor for JPA EntityManager.
	 */
	public CondValue() {
	}
	
	public CondValue(CondValuePk id, String value, boolean isObjRef) {
		this.id = id;
		this.valType = ValueTypesEnum.OBJECT_REF;
		this.charVal = value;
	}
	public CondValue(CondValuePk id, String value) {
		this.id = id;
		this.valType = ValueTypesEnum.STRING;
		this.charVal = value;
	}
	public CondValue(CondValuePk id, long value) {
		this.id = id;
		this.valType = ValueTypesEnum.LONG;
		this.numVal = new BigDecimal(value);
	}
	public CondValue(CondValuePk id, double value) {
		this.id = id;
		this.valType = ValueTypesEnum.DOUBLE;
		this.numVal = new BigDecimal(value);
	}
	public CondValue(CondValuePk id, int value) {
		this.id = id;
		this.valType = ValueTypesEnum.INTEGER;
		this.numVal = new BigDecimal(value);
	}
	public CondValue(CondValuePk id, Date value) {
		this(id,value,false);
	}
	public CondValue(CondValuePk id, Date value, boolean isDateTime) {
		this.id = id;
		if(isDateTime){
			this.valType = ValueTypesEnum.DATETIME;
		} else {
		    this.valType = ValueTypesEnum.DATE;
		}
		this.dtVal = value;
	}

	/**
	 * @return the id
	 */
	public CondValuePk getId() {
		return id;
	}

	/**
	 * @return the valType
	 */
	public ValueTypesEnum getValType() {
		return valType;
	}

	/**
	 * @param valType the valType to set
	 */
	public void setValType(ValueTypesEnum valType) {
		this.valType = valType;
	}

	@Column(name = "NUM_VAL", precision = 16, scale = 4)
	public BigDecimal getNumVal() {
		return this.numVal;
	}

	public void setNumVal(BigDecimal numVal) {
		this.numVal = numVal;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_VAL", length = 10)
	public Date getDtVal() {
		return this.dtVal;
	}

	public void setDtVal(Date dtVal) {
		this.dtVal = dtVal;
	}

	@Column(name = "CHAR_VAL", length = 2048)
	public String getCharVal() {
		return this.charVal;
	}

	public void setCharVal(String charVal) {
		this.charVal = charVal;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(valType);
		hashCodeBuilder.append(id);
		hashCodeBuilder.append(numVal);
		hashCodeBuilder.append(dtVal);
		hashCodeBuilder.append(charVal);
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
		if (!(obj instanceof CondValue)) {
			return false;
		}
		CondValue other = (CondValue) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(valType, other.valType);
		equalsBuilder.append(id, other.id);
		equalsBuilder.append(numVal, other.numVal);
		equalsBuilder.append(dtVal, other.dtVal);
		equalsBuilder.append(charVal, other.charVal);
		return equalsBuilder.isEquals();
	}

}
