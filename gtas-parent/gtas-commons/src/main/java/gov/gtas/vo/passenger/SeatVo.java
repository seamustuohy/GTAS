package gov.gtas.vo.passenger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.validators.Validatable;

public class SeatVo implements Validatable {
    private String number;
    private String travelerReferenceNumber;
    // flight details
    private String carrier;
    private String origin;
    private String destination;
    
	public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getTravelerReferenceNumber() {
        return travelerReferenceNumber;
    }
    public void setTravelerReferenceNumber(String travelerReferenceNumber) {
        this.travelerReferenceNumber = travelerReferenceNumber;
    }
    public String getCarrier() {
        return carrier;
    }
    public void setCarrier(String carrier) {
        this.carrier = carrier;
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
    
    // should be able to get away without carrier
    @Override
	public boolean isValid() {
		return StringUtils.isNotBlank(this.number)
		       && StringUtils.isNotBlank(this.travelerReferenceNumber)
		       && StringUtils.isNotBlank(this.origin)
		       && StringUtils.isNotBlank(this.destination);
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
