/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Entity
@Table(name = "flight",
    uniqueConstraints={@UniqueConstraint(columnNames={"carrier", "flight_number", "flight_date", "origin", "destination"})},
    indexes = {@Index(name = "carrier_index",  columnList="carrier"),
        @Index(name = "flight_number_index", columnList="flight_number"),
        @Index(name="flight_date_index", columnList="flight_date"),
        @Index(name="origin_index", columnList="origin"),
        @Index(name="destination_index", columnList="destination")}
)
public class Flight extends BaseEntityAudit {
    private static final long serialVersionUID = 1L;  
    public Flight() { }
    
    @Column(nullable = false)
    private String carrier;
    
    @Size(min = 4, max = 4)
    @Column(name = "flight_number", length = 4, nullable = false)
    private String flightNumber;

    /** combination of carrier and flight number used for reporting */
    @Column(name = "full_flight_number")
    private String fullFlightNumber;   
    
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
    
    /** calculated field */
    @Column(name = "etd_date")
    @Temporal(TemporalType.DATE)
    private Date etdDate;
    
    /** calculated field */
    @Column(name = "eta_date")
    @Temporal(TemporalType.DATE)
    private Date etaDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date etd;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date eta;
    
    @Column(length = 3, nullable = false)
    private String direction;
    
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flight", fetch = FetchType.EAGER)
    private Set<HitsSummary> hits = new HashSet<>();
    
    @Column(name = "passenger_count", nullable = false)
    private Integer passengerCount = Integer.valueOf(0);

    @Column(name = "rule_hit_count", nullable = false)
    private Integer ruleHitCount = Integer.valueOf(0);

    @Column(name = "list_hit_count", nullable = false)
    private Integer listHitCount = Integer.valueOf(0);
    
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
    public String getFullFlightNumber() {
        return fullFlightNumber;
    }
    public void setFullFlightNumber(String fullFlightNumber) {
        this.fullFlightNumber = fullFlightNumber;
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
    public Integer getPassengerCount() {
        return passengerCount;
    }
    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }
    public Integer getRuleHitCount() {
        return ruleHitCount;
    }
    public void setRuleHitCount(Integer ruleHitCount) {
        this.ruleHitCount = ruleHitCount;
    }
    public Integer getListHitCount() {
        return listHitCount;
    }
    public void setListHitCount(Integer listHitCount) {
        this.listHitCount = listHitCount;
    }

    /**
     * @return the etdDate
     */
    public Date getEtdDate() {
        return etdDate;
    }

    /**
     * @param etdDate the etdDate to set
     */
    public void setEtdDate(Date etdDate) {
        this.etdDate = etdDate;
    }

    /**
     * @return the etaDate
     */
    public Date getEtaDate() {
        return etaDate;
    }

    /**
     * @param etaDate the etaDate to set
     */
    public void setEtaDate(Date etaDate) {
        this.etaDate = etaDate;
    }

    @Override
    public int hashCode() {
       return Objects.hash(this.carrier, this.flightNumber, this.flightDate, this.origin, this.destination);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
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
