package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.parsers.vo.air.AddressVo;
import gov.gtas.parsers.vo.air.FlightVo;
import gov.gtas.parsers.vo.air.PassengerVo;
import gov.gtas.parsers.vo.air.PnrReportingAgentVo;

public class PnrVo {
    private String recordLocator;
    private String carrier;
    private String origin;
    private String originCountry;
    
    private Date dateBooked;
    private Date dateReceived;
    private Date departureDate;

    private Integer numPassengers;
    private Integer numBags;

    private String updateMode;
    
    private List<FlightVo> flights = new ArrayList<>();
    private List<PassengerVo> passengers = new ArrayList<>();
    private List<PnrReportingAgentVo> reportingParties = new ArrayList<>();
    private List<AddressVo> addresses = new ArrayList<>();
    
    public String getRecordLocator() {
        return recordLocator;
    }

    public void setRecordLocator(String recordLocator) {
        this.recordLocator = recordLocator;
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

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public Date getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(Date dateBooked) {
        this.dateBooked = dateBooked;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Integer getNumPassengers() {
        return numPassengers;
    }

    public void setNumPassengers(Integer numPassengers) {
        this.numPassengers = numPassengers;
    }

    public Integer getNumBags() {
        return numBags;
    }

    public void setNumBags(Integer numBags) {
        this.numBags = numBags;
    }

    public String getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(String updateMode) {
        this.updateMode = updateMode;
    }

    public List<FlightVo> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightVo> flights) {
        this.flights = flights;
    }

    public List<PassengerVo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerVo> passengers) {
        this.passengers = passengers;
    }

    public List<PnrReportingAgentVo> getReportingParties() {
        return reportingParties;
    }

    public void setReportingParties(List<PnrReportingAgentVo> reportingParties) {
        this.reportingParties = reportingParties;
    }

    public List<AddressVo> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressVo> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
