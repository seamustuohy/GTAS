package gov.cbp.taspd.gtas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class RuleCond extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1969876980229903470L;
	private String entityName;
	private String attrName;
	private String opCode;
	private int condSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID")
	private Rule rule;

	public RuleCond() {
	}

	public RuleCond(long id, String entityName, String attrName, String opCode,
			int condSeq) {
		this.id = id;
		this.condSeq = condSeq;
		this.entityName = entityName;
		this.attrName = attrName;
		this.opCode = opCode;
	}

	@Column(name = "COND_SEQ", nullable = false)
	public int getCondSeq() {
		return this.condSeq;
	}

	public void setCondSeq(int condSeq) {
		this.condSeq = condSeq;
	}

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

	@Column(name = "OP_CODE", nullable = false, length = 16)
	public String getOpCode() {
		return this.opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(condSeq);
		hashCodeBuilder.append(entityName);
		hashCodeBuilder.append(attrName);
		hashCodeBuilder.append(opCode);
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
		equalsBuilder.append(condSeq, other.condSeq);
		equalsBuilder.append(entityName, other.entityName);
		equalsBuilder.append(attrName, other.attrName);
		equalsBuilder.append(opCode, other.opCode);
		return equalsBuilder.isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("condSeq", condSeq);
		toStringBuilder.append("entityName", entityName);
		toStringBuilder.append("attrName", attrName);
		toStringBuilder.append("opCode", opCode);
		return toStringBuilder.toString();
	}

}
