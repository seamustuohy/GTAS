package gov.gtas.querybuilder.model;

import java.util.Date;

/**
 * 
 * @author GTAS5
 *
 */
public abstract class BaseFlightPaxQueryResult implements IQueryResult {
	
	private Long id;
	private String flightNumber;
	private String carrierCode;
	private String origin;
	private String destination;
	private String departureDt;
	private String arrivalDt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getCarrierCode() {
		return carrierCode;
	}
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDepartureDt() {
		return departureDt;
	}
	public void setDepartureDt(String departureDt) {
		this.departureDt = departureDt;
	}
	public String getArrivalDt() {
		return arrivalDt;
	}
	public void setArrivalDt(String arrivalDt) {
		this.arrivalDt = arrivalDt;
	}
}
