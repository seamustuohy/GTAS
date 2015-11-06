package gov.gtas.vo.passenger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.validators.Validatable;

public class SeatVo implements Validatable {
    private String number;
    
    /** unique id to reference back to a passenger */
    private String travelerReferenceNumber;
    
    /*
     * flight details: origin and dest should be sufficient
     * to uniquely identify a flight within a pnr itinerary.
     */
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
