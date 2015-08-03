package gov.gtas.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "hits_summary")
public class HitsSummary extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3436310987156511552L;

	@Column(name = "traveler_id", nullable=false)
	private Long travelerId;

	@Column(name = "rule_id", nullable = false)
	private Long ruleId;

	@Column(length = 100, nullable=false)
	private String description;

	@Column(name = "create_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

}
