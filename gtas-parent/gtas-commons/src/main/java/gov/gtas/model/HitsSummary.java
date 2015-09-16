package gov.gtas.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "hits_summary")
public class HitsSummary extends BaseEntity {
	private static final long serialVersionUID = 3436310987156511552L;

	public HitsSummary() {
	}

	@Column(name = "passenger_id", nullable = false)
	private Long passengerId;

	@Column(name = "created_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HitDetail> hitdetails = new ArrayList<HitDetail>();

	@Column(name = "flight_id")
	private Long flightId;

	@Column(name = "title", nullable = false)
	private String Title;

	@Column(name = "description")
	private String Description;

	@Column(name = "rule_hit_count")
	private Integer ruleHitCount;

	@Column(name = "hit_type")
	private String hitType;

	@Column(name = "wl_hit_count")
	private Integer watchListHitCount;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(Long passengerId) {
		this.passengerId = passengerId;
	}

	public List<HitDetail> getHitdetails() {
		return hitdetails;
	}

	public void setHitdetails(List<HitDetail> hitdetails) {
		this.hitdetails = hitdetails;
	}

	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getHitType() {
		return hitType;
	}

	public void setHitType(String hitType) {
		this.hitType = hitType;
	}

	public Integer getRuleHitCount() {
		return ruleHitCount;
	}

	public void setRuleHitCount(Integer ruleHitCount) {
		this.ruleHitCount = ruleHitCount;
	}

	public Integer getWatchListHitCount() {
		return watchListHitCount;
	}

	public void setWatchListHitCount(Integer watchListHitCount) {
		this.watchListHitCount = watchListHitCount;
	}
}
