package gov.gtas.vo.passenger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SeatVo {
    private String number;
    private Boolean apis = Boolean.valueOf(false);
    
    /** unique id to reference back to a passenger */
    private String travelerReferenceNumber;
    
    /*
     * flight details: origin and dest should be sufficient
     * to uniquely identify a flight within a pnr itinerary.
     */
    private String origin;
    private String destination;
    
    /*
     * for display purposes only.
     */
    private String flightNumber;
    private String firstName;
    private String lastName;
    
	public String getNumber() {
        return number;
    }
    public Boolean getApis() {
        return apis;
    }
    public void setApis(Boolean apis) {
        this.apis = apis;
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
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
