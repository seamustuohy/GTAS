package gov.gtas.model.udr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Embeddable
public class CondValuePk implements Serializable {
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -1678873197327409223L;

	@Column(name = "RULE_ID", nullable = false,  insertable=false, updatable = false)
	private long ruleId;
	@Column(name = "COND_SEQ", nullable = false, insertable=false, updatable=false)
	private int condSeq;

	@Column(name = "VAL_NAME", nullable = false, insertable=false, updatable = false)
	private String valName;

	public CondValuePk() {
	}

	public CondValuePk(final RuleCondPk parentId, final String valName) {
		this.ruleId = parentId.getRuleId();
		this.condSeq = parentId.getCondSeq();
		this.valName = valName;
	}

	/**
	 * @return the ruleId
	 */
	public long getRuleId() {
		return ruleId;
	}

	/**
	 * @return the condSeq
	 */
	public int getCondSeq() {
		return condSeq;
	}

	/**
	 * @return the valName
	 */
	public String getValName() {
		return valName;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(ruleId);
		hashCodeBuilder.append(condSeq);
		hashCodeBuilder.append(valName);
		return hashCodeBuilder.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;

		CondValuePk other = (CondValuePk) object;
		if (this.getRuleId() == other.getRuleId()
				&& this.getCondSeq() == other.getCondSeq()
				&& this.getValName().equals(other.getValName())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
