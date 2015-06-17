package gov.gtas.model.udr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Embeddable
public class RuleCondPk implements Serializable {
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -1678873197327409223L;

    @Column(name = "RULE_ID", nullable = false, updatable = false)
	private long ruleId;
    @Column(name = "COND_SEQ", nullable = false, updatable = false)
	private int condSeq;

	public RuleCondPk() {
	}

	public RuleCondPk(final long ruleId, final int seq) {
		this.ruleId = ruleId;
		this.condSeq = seq;
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

    @Override  
    public int hashCode() {  
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(ruleId);
		hashCodeBuilder.append(condSeq);
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
  
        RuleCondPk other = (RuleCondPk) object;  
        if (this.getRuleId() != other.getRuleId() || this.getCondSeq() != other.getCondSeq()) {  
            return false;  
        }  
        return true;  
    }  
  
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }        

}
