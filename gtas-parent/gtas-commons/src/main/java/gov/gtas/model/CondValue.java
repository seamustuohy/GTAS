package gov.gtas.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * CondValue
 */
@Entity
@Table(name = "cond_value", catalog = "gtas")
public class CondValue extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2701321017207668584L;
	private String valType;
	private Long numVal;
	private Date dtVal;
	private String charVal;
	private int condSeq;
	private String valName;

	public CondValue() {
	}

	public CondValue(long id, String valType) {
		this.id = id;
		this.valType = valType;
	}

	public CondValue(long id, String valType, Long numVal, Date dtVal,
			String charVal, int condSeq, String valName) {
		this.id = id;
		this.valType = valType;
		this.numVal = numVal;
		this.dtVal = dtVal;
		this.charVal = charVal;
		this.condSeq = condSeq;
		this.valName = valName;
	}

	@Column(name = "VAL_TYPE", nullable = false, length = 32)
	public String getValType() {
		return this.valType;
	}

	public void setValType(String valType) {
		this.valType = valType;
	}

	@Column(name = "COND_SEQ", nullable = false)
	public int getCondSeq() {
		return this.condSeq;
	}

	public void setCondSeq(int condSeq) {
		this.condSeq = condSeq;
	}

	@Column(name = "VAL_NAME", nullable = false, length = 64)
	public String getValName() {
		return this.valName;
	}

	public void setValName(String valName) {
		this.valName = valName;
	}

	@Column(name = "NUM_VAL", precision = 10, scale = 0)
	public Long getNumVal() {
		return this.numVal;
	}

	public void setNumVal(Long numVal) {
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
		hashCodeBuilder.append(condSeq);
		hashCodeBuilder.append(valName);
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
		equalsBuilder.append(condSeq, other.condSeq);
		equalsBuilder.append(valName, other.valName);
		equalsBuilder.append(numVal, other.numVal);
		equalsBuilder.append(dtVal, other.dtVal);
		equalsBuilder.append(charVal, other.charVal);
		return equalsBuilder.isEquals();
	}

}
