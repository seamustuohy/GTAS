package gov.gtas.parsers.paxlst.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ApisMessageVo {
    private byte[] raw;
    private String hashCode;
    private List<FlightVo> flights = new ArrayList<>();
    private List<PaxVo> passengers = new ArrayList<>();
    private List<ReportingPartyVo> reportingParties = new ArrayList<>();
    public ApisMessageVo() { }
    public void addFlight(FlightVo f) {
        flights.add(f);
    }
    public void addPax(PaxVo p) {
        passengers.add(p);
    }
    public void addReportingParty(ReportingPartyVo rp) {
        reportingParties.add(rp);
    }
    public void setRaw(byte[] raw) {
        this.raw = raw;
    }
    public byte[] getRaw() {
        return raw;
    }
    public List<FlightVo> getFlights() {
        return flights;
    }
    public List<PaxVo> getPassengers() {
        return passengers;
    }
    public List<ReportingPartyVo> getReportingParties() {
        return reportingParties;
    }
    public String getHashCode() {
        return hashCode;
    }
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }            
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
