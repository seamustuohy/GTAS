package gov.gtas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "apis_message")
public class ApisMessage extends Message {
    private static final long serialVersionUID = 1L;
    public ApisMessage() { }
    
    @Embedded
    private EdifactMessage edifactMessage;

    @ManyToMany(
        targetEntity=ReportingParty.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="apis_message_reporting_party",
        joinColumns=@JoinColumn(name="apis_message_id"),
        inverseJoinColumns=@JoinColumn(name="reporting_party_id")
    )    
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

    public void addReportingParty(ReportingParty rp) {
        if (this.reportingParties == null) {
            this.reportingParties = new HashSet<>();
        }
        this.reportingParties.add(rp);
    }
    
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
