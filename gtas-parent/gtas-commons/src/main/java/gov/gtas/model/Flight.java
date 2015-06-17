package gov.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "flight")
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
    @JoinColumn(referencedColumnName="id")     
    private Carrier carrier;
    
    @Size(min = 4, max = 4)
    @Column(name = "flight_number", length = 4)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
    private Airport origin;
    
    @ManyToOne
    @JoinColumn(name = "origin_country_id", referencedColumnName="id")     
    private Country originCountry;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
    private Airport destination;
    
    @ManyToOne
    @JoinColumn(name = "destination_country_id", referencedColumnName="id")     
    private Country destinationCountry;

    @Column(name = "flight_date")
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
    public Date getFlightDate() {
        return flightDate;
    }
    public Airport getOrigin() {
        return origin;
    }
    public void setOrigin(Airport origin) {
        this.origin = origin;
    }
    public Airport getDestination() {
        return destination;
    }
    public void setDestination(Airport destination) {
        this.destination = destination;
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
    public Country getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(Country originCountry) {
        this.originCountry = originCountry;
    }
    public Country getDestinationCountry() {
        return destinationCountry;
    }
    public void setDestinationCountry(Country destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    @Override
    public int hashCode() {
       return Objects.hash(this.carrier, this.flightNumber, this.flightDate, this.origin, this.destination);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Flight other = (Flight)obj;
        return Objects.equals(this.carrier, other.carrier)
                && Objects.equals(this.flightNumber, other.flightNumber)
                && Objects.equals(this.flightDate, other.flightDate)
                && Objects.equals(this.origin, other.origin)
                && Objects.equals(this.destination, other.destination);
    }
}
