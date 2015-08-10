package gov.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pnr")
public class PnrData extends BaseEntityAudit{
    @ManyToOne
    @JoinColumn(name = "pnr_message_id")
    private PnrMessage pnrMessage;
    
	@Column(name = "record_locator", length = 20)
	private String recordLocator;
	
    private String carrier;

    private String origin;
    
    @Column(name = "origin_country")
    private String originCountry;
    
    @Column(name = "booked", length = 20)
    private String booked;
    
    @Column(name = "received", length = 20)
    private String received;
    
    @Column(name = "departure_date")
    private Date departureDate;
    
    @Column(name = "days_booked_before_travel")
    private Integer daysBookedBeforeTravel;
    
    @Column(name = "passenger_count")
    private Integer passengerCount;
    
    @Column(name = "bag_count")
    private Integer bagCount;
    
    @Column(name = "raw")
    private String raw;
    
    @Column(name = "payment_form")
    private String formOfPayment;
    
    @Column(name = "total_dwell_time")
    private Integer totalDwellTime;
    
    @Column(name = "email")
    private String email;
 
    @ManyToMany(
        targetEntity=Flight.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_flight",
        joinColumns=@JoinColumn(name="flight_id"),
        inverseJoinColumns=@JoinColumn(name="pnr_id")
    )    
    private Set<Flight> flights = new HashSet<>();
    
	@ManyToMany(
        targetEntity = Passenger.class,
        cascade={CascadeType.ALL}
    ) 
    @JoinTable(
        name="pnr_passenger",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="passenger_id")
    )   
    private Set<Passenger> passengers = new HashSet<>();
 
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name="cc_id", referencedColumnName="id") 
    private CreditCard creditCard;
 
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name="agency_id",referencedColumnName="id") 
    private Agency agency;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name="ff_id",referencedColumnName="id") 
    private FrequentFlyer frequentFlyer;	

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pnrData")
    private Set<Address> adresses = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pnrData")
    private Set<Phone> phones = new HashSet<>();   
    
	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Set<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(Set<Passenger> passengers) {
		this.passengers = passengers;
	}

  
	public Set<Address> getAdresses() {
		return adresses;
	}

	public void setAdresses(Set<Address> adresses) {
		this.adresses = adresses;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRecordLocator() {
		return recordLocator;
	}

	public void setRecordLocator(String recordLocator) {
		this.recordLocator = recordLocator;
	}

	public PnrMessage getPnrMessage() {
        return pnrMessage;
    }

    public void setPnrMessage(PnrMessage pnrMessage) {
        this.pnrMessage = pnrMessage;
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

    public String getBooked() {
		return booked;
	}

	public void setBooked(String booked) {
		this.booked = booked;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Integer getDaysBookedBeforeTravel() {
		return daysBookedBeforeTravel;
	}

	public void setDaysBookedBeforeTravel(Integer daysBookedBeforeTravel) {
		this.daysBookedBeforeTravel = daysBookedBeforeTravel;
	}

	public Integer getPassengerCount() {
		return passengerCount;
	}

	public void setPassengerCount(Integer passengerCount) {
		this.passengerCount = passengerCount;
	}

	public Integer getBagCount() {
		return bagCount;
	}

	public void setBagCount(Integer bagCount) {
		this.bagCount = bagCount;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getFormOfPayment() {
		return formOfPayment;
	}

	public void setFormOfPayment(String formOfPayment) {
		this.formOfPayment = formOfPayment;
	}

	public Integer getTotalDwellTime() {
		return totalDwellTime;
	}

	public void setTotalDwellTime(Integer totalDwellTime) {
		this.totalDwellTime = totalDwellTime;
	}
 
    public Set<Flight> getFlights() {
		return flights;
	}

	public void setFlights(Set<Flight> flights) {
		this.flights = flights;
	}

	public FrequentFlyer getFrequentFlyer() {
		return frequentFlyer;
	}

	public void setFrequentFlyer(FrequentFlyer frequentFlyer) {
		this.frequentFlyer = frequentFlyer;
	}
	
    @Override
    public int hashCode() {
       return Objects.hash(this.carrier, this.departureDate, this.origin);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PnrData other = (PnrData)obj;
        return Objects.equals(this.carrier, other.carrier)
                && Objects.equals(this.departureDate, other.departureDate)
                 && Objects.equals(this.origin, other.origin);
    }    
}
