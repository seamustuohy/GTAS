package gov.gtas.delegates.vo;

import gov.gtas.model.PnrMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PnrDataVo extends BaseVo implements Serializable {

    private PnrMessage pnrMessage;
	private String recordLocator;
    private String carrier;
    private String origin;
    private String originCountry;
    private String booked;
    private String received;
    private Date departureDate;
    private Integer daysBookedBeforeTravel;
    private Integer passengerCount;
    private Integer bagCount;
    private String raw;
    private String formOfPayment;
    private Integer totalDwellTime;
    private String email;
    private Set<FlightVo> flights = new HashSet<>();
    private Set<PassengerVo> passengers = new HashSet<>();
    private CreditCardVo creditCard;
    private AgencyVo agency;
    private FrequentFlyerVo frequentFlyer;	
    private Set<AddressVo> addresses = new HashSet<>();
    private Set<PhoneVo> phones = new HashSet<>();
	public PnrMessage getPnrMessage() {
		return pnrMessage;
	}
	public void setPnrMessage(PnrMessage pnrMessage) {
		this.pnrMessage = pnrMessage;
	}
	public String getRecordLocator() {
		return recordLocator;
	}
	public void setRecordLocator(String recordLocator) {
		this.recordLocator = recordLocator;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<FlightVo> getFlights() {
		return flights;
	}
	public void setFlights(Set<FlightVo> flights) {
		this.flights = flights;
	}
	public Set<PassengerVo> getPassengers() {
		return passengers;
	}
	public void setPassengers(Set<PassengerVo> passengers) {
		this.passengers = passengers;
	}
	public CreditCardVo getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCardVo creditCard) {
		this.creditCard = creditCard;
	}
	public AgencyVo getAgency() {
		return agency;
	}
	public void setAgency(AgencyVo agency) {
		this.agency = agency;
	}
	public FrequentFlyerVo getFrequentFlyer() {
		return frequentFlyer;
	}
	public void setFrequentFlyer(FrequentFlyerVo frequentFlyer) {
		this.frequentFlyer = frequentFlyer;
	}
	public Set<AddressVo> getAddresses() {
		return addresses;
	}
	public void setAddresses(Set<AddressVo> adresses) {
		this.addresses = adresses;
	}
	public Set<PhoneVo> getPhones() {
		return phones;
	}
	public void setPhones(Set<PhoneVo> phones) {
		this.phones = phones;
	} 
    
    
}
