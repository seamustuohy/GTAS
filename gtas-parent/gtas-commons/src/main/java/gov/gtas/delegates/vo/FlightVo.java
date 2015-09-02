package gov.gtas.delegates.vo;

import gov.gtas.util.ServiceUtils;
import gov.gtas.validators.Validatable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;

public class FlightVo extends BaseVo implements Validatable {
	
    private String flightId;
    private String carrier;
    private String flightNumber;
    private String origin;
    private String originCountry;
    private String destination;
    private String destinationCountry;
    private boolean isOverFlight;
    private String direction;
  
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm aaa";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date flightDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date etd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date eta;
    private Set<PassengerVo> passengers = new HashSet<>();
    private Set<PnrDataVo> pnrs = new HashSet<>();
   
    
    
    public Set<PassengerVo> getPassengers() {
		return passengers;
	}
	public void setPassengers(Set<PassengerVo> passengers) {
		this.passengers = passengers;
	}
	public Set<PnrDataVo> getPnrs() {
		return pnrs;
	}
	public void setPnrs(Set<PnrDataVo> pnrs) {
		this.pnrs = pnrs;
	}
	public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getFlightId() {
        return flightId;
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
    public String getCarrier() {
        return carrier;
    }
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public String getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDestinationCountry() {
        return destinationCountry;
    }
    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }
    public Date getFlightDate() {
        return flightDate;
    }
    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }
    public Date getEtd() {
        return etd;
    }
    public void setEtd(Date etd) {
        this.etd = etd;
    }
    public Date getEta() {
        return eta;
    }
    public void setEta(Date eta) {
        this.eta = eta;
    }
    public boolean isOverFlight() {
        return isOverFlight;
    }
    public void setOverFlight(boolean isOverFlight) {
        this.isOverFlight = isOverFlight;
    }

    /**
     * rules for setting calculated field 'flightDate'
     */
    public void setFlightDate(Date etd, Date eta, Date transmissionDate) {
        Date d = null;
        if (etd != null) {
            d = etd;
        } else if (eta != null) {
            d = eta;
        } else {
            // TODO: verify this case
            d = transmissionDate;
        }

        if (d != null) {
            this.flightDate = ServiceUtils.stripTime(d);
        }
    }
    
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }

	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.destination) || StringUtils.isBlank(this.origin) 
				|| StringUtils.isBlank(this.flightNumber) || this.flightDate == null 
				|| StringUtils.isBlank(this.carrier)){
			return false;
		}
		return true;
	}  
}

