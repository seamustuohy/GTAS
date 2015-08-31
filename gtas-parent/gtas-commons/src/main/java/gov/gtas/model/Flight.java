package gov.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "flight")

public class Flight extends BaseEntityAudit {
    private static final long serialVersionUID = 1L;  
    public Flight() { }
    
    @Column(nullable = false)
    private String carrier;
    
    @Size(min = 4, max = 4)
    @Column(name = "flight_number", length = 4, nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String origin;
    
    @Column(name = "origin_country", length = 3)
    private String originCountry;
    
    @Column(nullable = false)
    private String destination;
    
    @Column(name = "destination_country", length = 3)
    private String destinationCountry;

    /** calculated field */
    @Column(name = "flight_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date flightDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date etd;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date eta;
    
    @Column(length = 3, nullable = false)
    private String direction;
    
    private Boolean thru;
    
    @ManyToMany(
        targetEntity=Passenger.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="flight_passenger",
        joinColumns=@JoinColumn(name="flight_id"),
        inverseJoinColumns=@JoinColumn(name="passenger_id")
    )    
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToMany(
        mappedBy = "flights",
        targetEntity = Pnr.class
    ) 
    private Set<Pnr> pnrs = new HashSet<>();
    
    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.getFlights().add(this);
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }
    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
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
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
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
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDestinationCountry() {
        return destinationCountry;
    }
    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public Set<Pnr> getPnrs() {
		return pnrs;
	}
	public void setPnrs(Set<Pnr> pnrs) {
		this.pnrs = pnrs;
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
