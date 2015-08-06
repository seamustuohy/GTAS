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

	/**
	 * 
	 */
	private static final long serialVersionUID = 3436310987156511552L;

	@Column(name = "traveler_id", nullable = false)
	private Long travelerId;

	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HitDetail> hitdetails = new ArrayList<HitDetail>();

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

	public List<HitDetail> getHitdetails() {
		return hitdetails;
	}

	public void setHitdetails(List<HitDetail> hitdetails) {
		this.hitdetails = hitdetails;
	}
}
