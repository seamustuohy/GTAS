package gov.gtas.parsers.paxlst.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ApisMessageVo {
    private byte[] raw;
    private String hashCode;
    private String transmissionSource;
    private Date transmissionDate;
    private String messageType;
    private String version;
    private String messageCode;
    
    /**
     * The sender may choose to use fields in the UNH segment to specify a block
     * sequence number and indicate the initial and final blocks that are being
     * sent. It is ￼￼￼Customs and Border Protection Page 9 DHS Consolidated User
     * Guide Part 4 – UN/EDIFACT Implementation Guide v3.5 03 Jan 2011 important
     * to note, there is no guarantee that DHS will receive and process the
     * blocks in the order that they were sent. While DHS may use the block
     * sequence numbers and the initial/final indicators as a reference for
     * troubleshooting missing or corrupted blocks, DHS will not employ an
     * automated validation or reporting of “missing” blocks.
     */
    private String sequenceNumber;
    
    private List<ReportingPartyVo> reportingParties = new ArrayList<>();
    private List<FlightVo> flights = new ArrayList<>();
    private List<PaxVo> passengers = new ArrayList<>();
    
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
    public String getTransmissionSource() {
        return transmissionSource;
    }

    public void setTransmissionSource(String transmissionSource) {
        this.transmissionSource = transmissionSource;
    }

    public Date getTransmissionDate() {
        return transmissionDate;
    }

    public void setTransmissionDate(Date transmissionDate) {
        this.transmissionDate = transmissionDate;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
