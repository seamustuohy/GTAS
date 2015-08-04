package gov.gtas.parsers.vo.air;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PnrVo {
    private String updateMode;
    private List<FlightVo> flights = new ArrayList<>();
    private List<PaxVo> passengers = new ArrayList<>();
    private List<PnrReportingAgentVo> reportingParties = new ArrayList<>();
    private List<AddressVo> addresses = new ArrayList<>();
    
    private Integer numberOfPassengers;

    public Integer getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(Integer numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
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

    public List<PaxVo> getPassengers() {
        return passengers;
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
