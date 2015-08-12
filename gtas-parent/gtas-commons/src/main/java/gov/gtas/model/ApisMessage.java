package gov.gtas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "apis_message")
public class ApisMessage extends Message {
    private static final long serialVersionUID = 1L;
    public ApisMessage() { }
    
    @Embedded
    private EdifactMessage edifactMessage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apisMessage")
    Set<ReportingParty> reportingParties = new HashSet<>();
    
    @ManyToMany(
        targetEntity=Flight.class,
        cascade={CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
        name="message_flight",
        joinColumns=@JoinColumn(name="message_id"),
        inverseJoinColumns=@JoinColumn(name="flight_id")
    )        
    private Set<Flight> flights = new HashSet<>();

    public Set<ReportingParty> getReportingParties() {
        return reportingParties;
    }

    public void setReportingParties(Set<ReportingParty> reportingParties) {
        this.reportingParties = reportingParties;
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }

    public EdifactMessage getEdifactMessage() {
        return edifactMessage;
    }

    public void setEdifactMessage(EdifactMessage edifactMessage) {
        this.edifactMessage = edifactMessage;
    }
}
