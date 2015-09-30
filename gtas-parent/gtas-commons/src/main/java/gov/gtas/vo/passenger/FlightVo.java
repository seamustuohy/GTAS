package gov.gtas.vo.passenger;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

import gov.gtas.validators.Validatable;
import gov.gtas.vo.BaseVo;

public class FlightVo extends BaseVo implements Validatable {
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm aaa";

    private String carrier;
    private String flightNumber;
    private String fullFlightNumber;
    private String origin;
    private String originCountry;
    private String destination;
    private String destinationCountry;
    private boolean isOverFlight;
    private String direction;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)    
    private Date flightDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)    
    private Date etd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)    
    private Date eta;
    
    private Integer passengerCount = Integer.valueOf(0);
    private Integer ruleHitCount = Integer.valueOf(0);
    private Integer listHitCount = Integer.valueOf(0);

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
            this.flightDate = stripTime(d);
        }
    }
    
    public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
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
    public String getFullFlightNumber() {
        return fullFlightNumber;
    }
    public void setFullFlightNumber(String fullFlightNumber) {
        this.fullFlightNumber = fullFlightNumber;
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
    public Integer getPassengerCount() {
        return passengerCount;
    }
    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }
    public Integer getRuleHitCount() {
        return ruleHitCount;
    }
    public void setRuleHitCount(Integer ruleHitCount) {
        this.ruleHitCount = ruleHitCount;
    }
    public Integer getListHitCount() {
        return listHitCount;
    }
    public void setListHitCount(Integer listHitCount) {
        this.listHitCount = listHitCount;
    }

    public Date stripTime(Date d) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
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
