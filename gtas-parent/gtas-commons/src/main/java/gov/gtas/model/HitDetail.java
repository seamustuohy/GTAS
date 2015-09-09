package gov.gtas.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hit_detail")
public class HitDetail extends BaseEntity {
    private static final long serialVersionUID = 5219262569468670275L;
    public HitDetail() { }
    
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "hits_summary_id", nullable = false, referencedColumnName = "id")
	private HitsSummary parent;

	/**
	 * String representation of matched conditions; it can be splitted into
	 * String[]
	 */
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "cond_text", columnDefinition = "TEXT NULL")
	private String ruleConditions;

	@Column(name = "created_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "rule_id", nullable = false)
	private Long ruleId;

	public HitsSummary getParent() {
		return parent;
	}

	public void setParent(HitsSummary parent) {
		this.parent = parent;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
