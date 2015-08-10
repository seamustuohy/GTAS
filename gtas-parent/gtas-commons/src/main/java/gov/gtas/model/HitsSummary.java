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

	@Column(name = "passenger_id", nullable = false)
	private Long passengerId;

	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HitDetail> hitdetails = new ArrayList<HitDetail>();

	@Column(name = "flight_id")
	private Long flightId;

	@Column(name = "title", nullable = false)
	private String Title;

	@Column(name = "description")
	private String Description;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
}
