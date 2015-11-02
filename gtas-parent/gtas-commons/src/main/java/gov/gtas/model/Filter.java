package gov.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;

import gov.gtas.model.lookup.Airport;

@Entity
@Table(name = "Filter")
public class Filter extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne( targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(targetEntity = FlightDirection.class, cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "flight_direction", nullable = false, referencedColumnName = "code")
	private FlightDirection flightDirection=new FlightDirection();

	public FlightDirection getFlightDirection() {
		return flightDirection;
	}

	public void setFlightDirection(FlightDirection flightDirection) {
		this.flightDirection = flightDirection;
	}

	@ManyToMany(targetEntity = Airport.class, cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "filter_origin_airports", joinColumns = @JoinColumn(name = "id") , inverseJoinColumns = @JoinColumn(name = "airport_id") )
	private Set<Airport> originAirports = new HashSet<Airport>();

	@ManyToMany(targetEntity = Airport.class, cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "filter_destination_airports", joinColumns = @JoinColumn(name = "id") , inverseJoinColumns = @JoinColumn(name = "airport_id") )
	private Set<Airport> destinationAirports = new HashSet<Airport>();

	public Set<Airport> getDestinationAirports() {
		return destinationAirports;
	}

	public void setDestinationAirports(Set<Airport> destinationAirports) {
		this.destinationAirports = destinationAirports;
	}

	public Set<Airport> getOriginAirports() {
		return originAirports;
	}

	public void setOriginAirports(Set<Airport> originAirports) {
		this.originAirports = originAirports;
	}

	@Column(name = "eta_start", columnDefinition = "DATETIME")
	@Temporal(TemporalType.DATE)
	private Date etaStart;

	public Date getEtaStart() {
		return etaStart;
	}

	public void setEtaStart(Date etaStart) {
		this.etaStart = etaStart;
	}

	@Column(name = "eta_end", columnDefinition = "DATETIME")
	@Temporal(TemporalType.DATE)
	private Date etaEnd;

	public Date getEtaEnd() {
		return etaEnd;
	}

	public void setEtaEnd(Date etaEnd) {
		this.etaEnd = etaEnd;
	}
	
	@Column(name = "etd_start", columnDefinition = "DATETIME")
	@Temporal(TemporalType.DATE)
	private Date etdStart;

	public Date getEtdStart() {
		return etdStart;
	}

	public void setEtdStart(Date etdStart) {
		this.etdStart = etdStart;
	}

	@Column(name = "etd_end", columnDefinition = "DATETIME")
	@Temporal(TemporalType.DATE)
	private Date etdEnd;

	public Date getEtdEnd() {
		return etaEnd;
	}

	public void setEtdEnd(Date etaEnd) {
		this.etaEnd = etaEnd;
	}


	@Override
	public int hashCode() {
		return Objects.hash(this.flightDirection, this.etaEnd,this.etaStart,this.etdStart,this.etdEnd,this.user.getUserId());
	}

	@Override
	public boolean equals(Object target) {

		if (this == target) {
			return true;
		}

		if (!(target instanceof Filter)) {
			return false;
		}

		Filter dataTarget = ((Filter) target);

		return new EqualsBuilder().append(this.etaStart, dataTarget.getEtaStart())
				.append(this.etaEnd, dataTarget.getEtaEnd())
				.append(this.etdStart, dataTarget.getEtdStart())
				.append(this.etdEnd, dataTarget.getEtdEnd())
				.append(this.originAirports, dataTarget.getOriginAirports())
				.append(this.flightDirection, dataTarget.getFlightDirection())
				.append(this.destinationAirports, dataTarget.getDestinationAirports())
				.append(this.user, dataTarget.getUser()).isEquals();
	}

	@Override
	public String toString() {
		return "Filter [id=" + user.getUserId() + ", eta Start=" + etaStart
				+ ", eta End=" + etaEnd
				+", etd Start=" + etdStart
				+", etd End=" + etdEnd
				+ ", Origin Airports=" + originAirports
				+ ", Desitnation Airports=" + destinationAirports + ", User =" + user + ", Flight Direction="
				+ flightDirection + "]";
	}

}
