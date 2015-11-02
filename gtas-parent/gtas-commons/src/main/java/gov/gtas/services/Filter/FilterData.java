package gov.gtas.services.Filter;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


public class FilterData {

	private static final long serialVersionUID = 1L;
	// e.g. 2015-10-02T18:33:03.412Z
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private final String userId;
	private final String flightDirection;
	private  Set<String> originAirports = new HashSet<String>();
	private  Set<String> destinationAirports = new HashSet<String>();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT) 
	private final Date etaStart;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT) 
	private final Date etaEnd;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT) 
	private final Date etdStart;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT) 
	private final Date etdEnd;
	
	public FilterData()
	{
		userId=null;
		flightDirection=null;
		etaStart=null;
		etdStart=null;
		etaEnd=null;
		etdEnd=null;
	}
	/**
	 * 
	 * @param userId
	 * @param flightDirection
	 * @param originAirports
	 * @param destinationAirports
	 * @param etaStart
	 * @param etaEnd
	 * @param etdStart
	 * @param etdEnd
	 */

	public FilterData(@JsonProperty("userId") String userId, @JsonProperty("flightDirection") String flightDirection,
			@JsonProperty("originAirports") Set<String> originAirports,
			@JsonProperty("destinationAirports") Set<String> destinationAirports,
			@JsonProperty("etaStart") Date etaStart,
			@JsonProperty("etaEnd") Date etaEnd,
			@JsonProperty("etdStart") Date etdStart,
			@JsonProperty("etdEnd") Date etdEnd) {
		this.userId = userId;
		this.flightDirection = flightDirection;
		this.originAirports = originAirports;
		this.destinationAirports = destinationAirports;
		this.etaStart = etaStart;
		this.etdEnd=etdEnd;
		this.etaEnd=etaEnd;
		this.etdStart = etdStart;
	}

	

	@JsonProperty("userId")
	public final String getUserId() {
		return userId;
	}
	
	@JsonProperty("flightDirection")
	public final String getFlightDirection() {
		return flightDirection;
	}
	
	@JsonProperty("destinationAirports")
	public final Set<String> getDestinationAirports() {
		return destinationAirports;
	}
	
	@JsonProperty("originAirports")
	public final Set<String> getOriginAirports() {
		return originAirports;
	}
	
	@JsonProperty("etaStart")
	public final Date getEtaStart() {
		return etaStart;
	}
	@JsonProperty("etaEnd")
	public final Date getEtaEnd() {
		return etaEnd;
	}
	@JsonProperty("etdStart")
	public final Date getEtdStart() {
		return etdStart;
	}
	@JsonProperty("etdEnd")
	public final Date getEtdEnd() {
		return etdEnd;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.flightDirection, this.userId, this.destinationAirports, this.originAirports, this.etaStart,
				this.etdStart,this.etaEnd,this.etdEnd);
	}

	@Override
	public boolean equals(Object target) {

		if (this == target) {
			return true;
		}

		if (!(target instanceof FilterData)) {
			return false;
		}

		FilterData dataTarget = ((FilterData) target);

		return new EqualsBuilder().append(this.etaStart, dataTarget.getEtaStart()).append(this.etdStart, dataTarget.getEtdStart())
				.append(this.etaEnd, dataTarget.getEtaEnd())
				.append(this.etdEnd,dataTarget.getEtdEnd())
				.append(this.originAirports, dataTarget.getOriginAirports())
				.append(this.flightDirection, dataTarget.getFlightDirection())
				.append(this.destinationAirports, dataTarget.getDestinationAirports())
				.append(this.userId, dataTarget.getUserId()).isEquals();
	}

	@Override
	public String toString() {
		return "Filter [  eta Start =" + etaStart+", eta End =" + etaEnd
				+", etd Start="+ etdStart
				+ ", etd End =" + etdEnd
				+", Origin Airports=" + originAirports
				+ ", Desitnation Airports=" + destinationAirports + ", User =" + userId + ", Flight Direction="
				+ flightDirection  + "]";
	}

}
