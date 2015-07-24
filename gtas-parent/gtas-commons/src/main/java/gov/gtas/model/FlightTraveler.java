package gov.gtas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "flight_traveler")
public class FlightTraveler extends BaseEntityAudit {
	
	@Column(name="flight_id")
	private Integer flight_id;
	
	@Column(name="traveler_id")
	private Integer traveler_id;

	public Integer getFlight_id() {
		return flight_id;
	}

	public void setFlight_id(Integer flight_id) {
		this.flight_id = flight_id;
	}

	public Integer getTraveler_id() {
		return traveler_id;
	}

	public void setTraveler_id(Integer traveler_id) {
		this.traveler_id = traveler_id;
	}
	
	
	
	
	
	

}
