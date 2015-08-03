package gov.gtas.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "hit_detail")
public class HitDetail extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5219262569468670275L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hits_summary_id", nullable = false, referencedColumnName = "id")
	private HitsSummary parent;

	
	/**
	 * The serialization version of array of condition strings
	 */
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "cond_text", columnDefinition = "TEXT NULL")
	private String ruleConditions;

	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = "rule_id", nullable = false)
	private Long ruleId;

	public HitsSummary getParent() {
		return parent;
	}

	public void setParent(HitsSummary parent) {
		this.parent = parent;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRuleConditions() {
		return ruleConditions;
	}

	public void setRuleConditions(String ruleConditions) {
		this.ruleConditions = ruleConditions;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

}
