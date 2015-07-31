package gov.gtas.parsers.pnrgov.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;

public class PnrVo {
    private String updateMode;
    private List<FlightVo> flights = new ArrayList<>();
    private List<PnrPax> passengers = new ArrayList<>();
    private List<PnrReportingAgentVo> reportingParties = new ArrayList<>();
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

    public List<PnrPax> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PnrPax> passengers) {
        this.passengers = passengers;
    }

    public List<PnrReportingAgentVo> getReportingParties() {
        return reportingParties;
    }

    public void setReportingParties(List<PnrReportingAgentVo> reportingParties) {
        this.reportingParties = reportingParties;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
