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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "flight")
public class Flight extends BaseEntityAudit {
    public Flight() { }
    
    @ManyToMany(
        targetEntity=Traveler.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="flight_traveler",
        joinColumns=@JoinColumn(name="flight_id"),
        inverseJoinColumns=@JoinColumn(name="traveler_id")
    )    
    private Set<Traveler> travelers = new HashSet<>();

    @ManyToMany(
            mappedBy = "flights",
            targetEntity = PnrData.class
    ) 
    private Set<PnrData> pnrs = new HashSet<>();
    
    private String carrier;
    
    @Size(min = 4, max = 4)
    @Column(name = "flight_number", length = 4)
    private String flightNumber;

    private String origin;
    
    @Column(name = "origin_country")
    private String originCountry;
    
    private String destination;
    
    @Column(name = "destination_country")
    private String destinationCountry;

    /** calculated field */
    @Column(name = "flight_date")
    @Temporal(TemporalType.DATE)
    private Date flightDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date etd;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date eta;
    
    @Column(length = 3)
    private String direction;
    
    public Set<Traveler> getPassengers() {
        return travelers;
    }
    public void setPassengers(Set<Traveler> passengers) {
        this.travelers = passengers;
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
    public Set<Traveler> getTravelers() {
        return travelers;
    }
    public void setTravelers(Set<Traveler> travelers) {
        this.travelers = travelers;
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

    public Set<PnrData> getPnrs() {
		return pnrs;
	}
	public void setPnrs(Set<PnrData> pnrs) {
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
