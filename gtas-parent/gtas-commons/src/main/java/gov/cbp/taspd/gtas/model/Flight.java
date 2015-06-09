package gov.cbp.taspd.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Flight extends BaseEntityAudit {
    public Flight() { }
    
    @ManyToMany(
        targetEntity=Pax.class,
        cascade={CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
        name="flight_pax",
        joinColumns=@JoinColumn(name="flight_id"),
        inverseJoinColumns=@JoinColumn(name="pax_id")
    )    
    private Set<Pax> passengers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="carrier", referencedColumnName="id")     
    private Carrier carrier;
    
    private String flightNumber;
    private String origin;
    
    @ManyToOne
    @JoinColumn(name="country", referencedColumnName="id")     
    private Country originCountry;
    
    private String destination;
    
    @ManyToOne
    @JoinColumn(name="country", referencedColumnName="id")     
    private Country destinationCountry;

    private Date flightDate;
    private Date etd;
    private Date eta;
    
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        mappedBy = "flights",
        targetEntity = ApisMessage.class
    )    
    private Set<ApisMessage> messages = new HashSet<>();
    
    public Set<Pax> getPassengers() {
        return passengers;
    }
    public void setPassengers(Set<Pax> passengers) {
        this.passengers = passengers;
    }
    public Carrier getCarrier() {
        return carrier;
    }
    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public Country getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(Country originCountry) {
        this.originCountry = originCountry;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public Country getDestinationCountry() {
        return destinationCountry;
    }
    public void setDestinationCountry(Country destinationCountry) {
        this.destinationCountry = destinationCountry;
    }
    public Date getFlightDate() {
        return flightDate;
    }
    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }
    public Date getEtd() {
        return etd;
    }
    public void setEtd(Date etd) {
        this.etd = etd;
    }
    public Date getEta() {
        return eta;
    }
    public void setEta(Date eta) {
        this.eta = eta;
    }
}
