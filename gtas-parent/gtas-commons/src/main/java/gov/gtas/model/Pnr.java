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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "pnr")
public class Pnr extends BaseEntityAudit{
    private static final long serialVersionUID = 1L;  
    public Pnr() { }
    
    @ManyToOne
    @JoinColumn(name = "pnr_message_id")
    private PnrMessage pnrMessage;
    
	@Column(name = "record_locator", length = 20)
	private String recordLocator;
	
    private String carrier;

    private String origin;
    
    @Column(name = "origin_country", length = 3)
    private String originCountry;
    
    @Column(name = "date_booked")
    @Temporal(TemporalType.DATE)
    private Date dateBooked;
    
    @Column(name = "date_received")
    @Temporal(TemporalType.DATE)
    private Date dateReceived;
    
    @Column(name = "departure_date")
    @Temporal(TemporalType.DATE)
    private Date departureDate;
    
    @Column(name = "days_booked_before_travel")
    private Integer daysBookedBeforeTravel;
    
    @Column(name = "passenger_count")
    private Integer passengerCount;
    
    @Column(name = "bag_count")
    private Integer bagCount;
    
    @Column(name = "form_of_payment")
    private String formOfPayment;
    
    @Column(name = "total_dwell_time")
    private Integer totalDwellTime;
    
    private String dwellAirport;
    private String dwellCountry;
    private Integer dwellDuration;
    private String route;
    
    @ManyToMany(
        targetEntity=Flight.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_flight",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="flight_id")
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

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id")
    private Agency agency;
    
    @ManyToMany(
        targetEntity=CreditCard.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_credit_card",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="credit_card_id")
    )    
    private Set<CreditCard> creditCards = new HashSet<>();
 
    @ManyToMany(
        targetEntity=FrequentFlyer.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_frequent_flyer",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="ff_id")
    )    
    private Set<FrequentFlyer> frequentFlyers = new HashSet<>();

    @ManyToMany(
        targetEntity=Address.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_address",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="address_id")
    )    
    private Set<Address> addresses = new HashSet<>();
    
    @ManyToMany(
        targetEntity=Phone.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_phone",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="phone_id")
    )    
    private Set<Phone> phones = new HashSet<>();

    @ManyToMany(
        targetEntity=Email.class,
        cascade={CascadeType.ALL}
    )
    @JoinTable(
        name="pnr_email",
        joinColumns=@JoinColumn(name="pnr_id"),
        inverseJoinColumns=@JoinColumn(name="email_id")
    )    
    private Set<Email> emails = new HashSet<>();

    public void addPassenger(Passenger p) {
        if (this.passengers == null) {
            this.passengers = new HashSet<>();
        }
        this.passengers.add(p);
    }
    
    public void addCreditCard(CreditCard cc) {
        if (this.creditCards == null) {
            this.creditCards = new HashSet<>();
        }
        this.creditCards.add(cc);
    }

    public void addFrequentFlyer(FrequentFlyer ff) {
        if (this.frequentFlyers == null) {
            this.frequentFlyers = new HashSet<>();
        }
        this.frequentFlyers.add(ff);
    }

    public void addAddress(Address address) {
        if (this.addresses == null) {
            this.addresses = new HashSet<>();
        }
        this.addresses.add(address);
    }
    
    public void addPhone(Phone phone) {
        if (this.phones == null) {
            this.phones = new HashSet<>();
        }
        this.phones.add(phone);
    }

    public void addEmail(Email email) {
        if (this.emails == null) {
            this.emails = new HashSet<>();
        }
        this.emails.add(email);
    }

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public Set<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(Set<Passenger> passengers) {
		this.passengers = passengers;
	}
 
	public Set<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public Set<FrequentFlyer> getFrequentFlyers() {
        return frequentFlyers;
    }

    public void setFrequentFlyers(Set<FrequentFlyer> frequentFlyers) {
        this.frequentFlyers = frequentFlyers;
    }

    public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	public Set<Email> getEmails() {
        return emails;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
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

	public Date getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(Date dateBooked) {
        this.dateBooked = dateBooked;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
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

    @Override
    public int hashCode() {
       return Objects.hash(this.recordLocator);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Pnr other = (Pnr)obj;
        return Objects.equals(this.recordLocator, other.recordLocator);
    }    
}