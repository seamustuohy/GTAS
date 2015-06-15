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
    @JoinColumn(referencedColumnName="id")     
    private Carrier carrier;
    
    private String flightNumber;

    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
    private Airport origin;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
    private Country originCountry;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
    private Airport destination;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
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
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((carrier == null) ? 0 : carrier.hashCode());
        result = prime * result
                + ((destination == null) ? 0 : destination.hashCode());
        result = prime
                * result
                + ((destinationCountry == null) ? 0 : destinationCountry
                        .hashCode());
        result = prime * result
                + ((flightDate == null) ? 0 : flightDate.hashCode());
        result = prime * result
                + ((flightNumber == null) ? 0 : flightNumber.hashCode());
        result = prime * result + ((origin == null) ? 0 : origin.hashCode());
        result = prime * result
                + ((originCountry == null) ? 0 : originCountry.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Flight other = (Flight) obj;
        if (carrier != other.carrier)
            return false;
        if (destination == null) {
            if (other.destination != null)
                return false;
        } else if (!destination.equals(other.destination))
            return false;
        if (destinationCountry != other.destinationCountry)
            return false;
        if (flightDate == null) {
            if (other.flightDate != null)
                return false;
        } else if (!flightDate.equals(other.flightDate))
            return false;
        if (flightNumber == null) {
            if (other.flightNumber != null)
                return false;
        } else if (!flightNumber.equals(other.flightNumber))
            return false;
        if (origin == null) {
            if (other.origin != null)
                return false;
        } else if (!origin.equals(other.origin))
            return false;
        if (originCountry != other.originCountry)
            return false;
        return true;
    }
}
