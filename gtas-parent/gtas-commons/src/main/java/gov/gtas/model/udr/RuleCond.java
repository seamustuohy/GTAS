package gov.gtas.model.udr;

import gov.gtas.model.udr.OperatorCodeEnum;
import gov.gtas.model.udr.RuleCondPk;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 */
@Entity
@Table(name = "rule_cond", catalog = "gtas")
public class RuleCond implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1969876980229903470L;
	
	@EmbeddedId
	private RuleCondPk id;
	
	private String entityName;
	private String attrName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OP_CODE", nullable = false, length = 16)
	private OperatorCodeEnum opCode;

	@OneToMany(mappedBy="parent")
	private List<CondValue> values;
	
	public RuleCond() {
	}

	public RuleCond(RuleCondPk id, String entityName, String attrName, OperatorCodeEnum opCode) {
		this.id = id;
//		this.condSeq = condSeq;
		this.entityName = entityName;
		this.attrName = attrName;
		this.opCode = opCode;
	}

//	@Column(name = "COND_SEQ", nullable = false)
//	public int getCondSeq() {
//		return this.condSeq;
//	}

//	public void setCondSeq(int condSeq) {
//		this.condSeq = condSeq;
//	}

	@Column(name = "ENTITY_NAME", nullable = false, length = 64)
	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Column(name = "ATTR_NAME", nullable = false, length = 256)
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
