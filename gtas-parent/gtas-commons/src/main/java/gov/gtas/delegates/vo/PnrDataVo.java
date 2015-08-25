package gov.gtas.delegates.vo;

import gov.gtas.model.PnrMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Class PnrDataVo holds the reservation information for passengers.
 * it contain all the reservation details from pnrgov message and 
 * can have one or more flidht details with one or more passengers.
 * All top level properties need to be set and need to add the passengers 
 * and flights for this reservation.
 * @author GTAS4
 *
 */
public class PnrDataVo extends BaseVo implements Serializable {

    private PnrMessageVo pnrMessage;
 	private String recordLocator;
    private String carrier;
    private String origin;
    private String originCountry;
    private Date dateBooked;
    private Date dateReceived;
    private Date departureDate;
    private Integer daysBookedBeforeTravel;
    private Integer passengerCount;
    private Integer bagCount;
    private String formOfPayment;
    private Integer totalDwellTime;
    private Set<FlightVo> flights = new HashSet<>();
    private Set<PassengerVo> passengers = new HashSet<>();
    private AgencyVo agency;
    private Set<CreditCardVo> creditCards = new HashSet<>();
    private Set<FrequentFlyerVo> frequentFlyers = new HashSet<>();
    private Set<AddressVo> addresses = new HashSet<>();
    private Set<PhoneVo> phones = new HashSet<>();
    private Set<EmailVo> emails = new HashSet<>();
    

	public PnrMessageVo getPnrMessage() {
		return pnrMessage;
	}
	public void setPnrMessage(PnrMessageVo pnrMessage) {
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
	public AgencyVo getAgency() {
		return agency;
	}
	public void setAgency(AgencyVo agency) {
		this.agency = agency;
	}
	public Set<CreditCardVo> getCreditCards() {
		return creditCards;
	}
	public void setCreditCards(Set<CreditCardVo> creditCards) {
		this.creditCards = creditCards;
	}
	public Set<FrequentFlyerVo> getFrequentFlyers() {
		return frequentFlyers;
	}
	public void setFrequentFlyers(Set<FrequentFlyerVo> frequentFlyers) {
		this.frequentFlyers = frequentFlyers;
	}
	public Set<AddressVo> getAddresses() {
		return addresses;
	}
	public void setAddresses(Set<AddressVo> addresses) {
		this.addresses = addresses;
	}
	public Set<PhoneVo> getPhones() {
		return phones;
	}
	public void setPhones(Set<PhoneVo> phones) {
		this.phones = phones;
	}
	public Set<EmailVo> getEmails() {
		return emails;
	}
	public void setEmails(Set<EmailVo> emails) {
		this.emails = emails;
	}
    
    

}
