package gov.gtas.parsers.pnrgov.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.gtas.parsers.paxlst.vo.AddressVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.pnrgov.segment.TKT;

public class PnrVo extends PaxVo{

	private String recordLocator;
	private String carrierCode;
	private String origin;
	private String booked;
	private String received;
	private Date departureDate;
	private String daysBookedBeforeTravel;
	private String passengerCount;
	private String bagCount;
	private String route;
	private String raw;
	private String createDate;
	private String hashCode;
	private String formOfPayment;
	private String totalDwellTimeMins;
	private String pnrAgencyId;
	private String pnrAgentIdentification;
	private String pnrAgencyName;
	private String pnrAgencyAirlineCode;
	private String pnrAgencyLocationCode;
	private String pnrAgencyTypeCode;
	private String pnrAgencyCity;
	private String pnrAgencyState;
	private String pnrAgencyCountry;
	private String pnrEmailId;
	private String pnrDomain;
	private String pnrAddressLine1;
	private String pnrAddressLine2;
	private String pnrAddressLine3;
	private String pnrAddressCity;
	private String pnrAddressState;
	private String pnrAddressCountry;
	private String pnrAddressPostalCode;
	private String pnrPhoneNumber;
	private String pnrPhoneCity;
	private String creditCardType;
	private String creditCardNumber;
	private String creditCardExpiration;
	private String creditCardHolderName;
	private String freequentFlyerNumber;
	private String freequentFlyerAirline;
	private String flightNumber;
	private FlightVo flight;
	private String passengerType;
	private String frequentMemberLevelInfo;
	private String reservationControlNumber;//M*
	private String airlineCode;
	private String chkDateQualifier;
	private Date checkInDate;
	private String otherServiceInformation;
	private List<AddressVo> adresses=new ArrayList<>();
	private String ticketNumber;
	private String ticketType;
	private String numberOfBooklets;

	public String getPnrAgencyTypeCode() {
		return pnrAgencyTypeCode;
	}
	public void setPnrAgencyTypeCode(String pnrAgencyTypeCode) {
		this.pnrAgencyTypeCode = pnrAgencyTypeCode;
	}
	public String getPnrAgentIdentification() {
		return pnrAgentIdentification;
	}
	public void setPnrAgentIdentification(String pnrAgentIdentification) {
		this.pnrAgentIdentification = pnrAgentIdentification;
	}
	public String getPnrAgencyAirlineCode() {
		return pnrAgencyAirlineCode;
	}
	public void setPnrAgencyAirlineCode(String pnrAgencyAirlineCode) {
		this.pnrAgencyAirlineCode = pnrAgencyAirlineCode;
	}
	public String getPnrAgencyLocationCode() {
		return pnrAgencyLocationCode;
	}
	public void setPnrAgencyLocationCode(String pnrAgencyCountryCode) {
		this.pnrAgencyLocationCode = pnrAgencyCountryCode;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getNumberOfBooklets() {
		return numberOfBooklets;
	}
	public void setNumberOfBooklets(String numberOfBooklets) {
		this.numberOfBooklets = numberOfBooklets;
	}
	public List<AddressVo> getAdresses() {
		return adresses;
	}
	public void setAdresses(List<AddressVo> adresses) {
		this.adresses = adresses;
	}
	public String getOtherServiceInformation() {
		return otherServiceInformation;
	}
	public void setOtherServiceInformation(String otherServiceInformation) {
		this.otherServiceInformation = otherServiceInformation;
	}
	public String getChkDateQualifier() {
		return chkDateQualifier;
	}
	public void setChkDateQualifier(String chkDateQualifier) {
		this.chkDateQualifier = chkDateQualifier;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}
	public String getReservationControlNumber() {
		return reservationControlNumber;
	}
	public void setReservationControlNumber(String reservationControlNumber) {
		this.reservationControlNumber = reservationControlNumber;
	}
	public String getAirlineCode() {
		return airlineCode;
	}
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}
	public String getFrequentMemberLevelInfo() {
		return frequentMemberLevelInfo;
	}
	public void setFrequentMemberLevelInfo(String frequentMemberLevelInfo) {
		this.frequentMemberLevelInfo = frequentMemberLevelInfo;
	}
	public String getPassengerType() {
		return passengerType;
	}
	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}
	public String getPnrAgencyName() {
		return pnrAgencyName;
	}
	public void setPnrAgencyName(String pnrAgencyName) {
		this.pnrAgencyName = pnrAgencyName;
	}
	public String getPnrAgencyCity() {
		return pnrAgencyCity;
	}
	public void setPnrAgencyCity(String pnrAgencyCity) {
		this.pnrAgencyCity = pnrAgencyCity;
	}
	public String getPnrAgencyState() {
		return pnrAgencyState;
	}
	public void setPnrAgencyState(String pnrAgencyState) {
		this.pnrAgencyState = pnrAgencyState;
	}
	public String getPnrAgencyCountry() {
		return pnrAgencyCountry;
	}
	public void setPnrAgencyCountry(String pnrAgencyCountry) {
		this.pnrAgencyCountry = pnrAgencyCountry;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public FlightVo getFlight() {
		return flight;
	}
	public void setFlight(FlightVo flight) {
		this.flight = flight;
	}
	public String getRecordLocator() {
		return recordLocator;
	}
	public void setRecordLocator(String recordLocator) {
		this.recordLocator = recordLocator;
	}
	public String getCarrierCode() {
		return carrierCode;
	}
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
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
	public String getDaysBookedBeforeTravel() {
		return daysBookedBeforeTravel;
	}
	public void setDaysBookedBeforeTravel(String daysBookedBeforeTravel) {
		this.daysBookedBeforeTravel = daysBookedBeforeTravel;
	}
	public String getPassengerCount() {
		return passengerCount;
	}
	public void setPassengerCount(String passengerCount) {
		this.passengerCount = passengerCount;
	}
	public String getBagCount() {
		return bagCount;
	}
	public void setBagCount(String bagCount) {
		this.bagCount = bagCount;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getRaw() {
		return raw;
	}
	public void setRaw(String raw) {
		this.raw = raw;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	public String getFormOfPayment() {
		return formOfPayment;
	}
	public void setFormOfPayment(String formOfPayment) {
		this.formOfPayment = formOfPayment;
	}
	public String getTotalDwellTimeMins() {
		return totalDwellTimeMins;
	}
	public void setTotalDwellTimeMins(String totalDwellTimeMins) {
		this.totalDwellTimeMins = totalDwellTimeMins;
	}
	public String getPnrAgencyId() {
		return pnrAgencyId;
	}
	public void setPnrAgencyId(String pnrAgencyId) {
		this.pnrAgencyId = pnrAgencyId;
	}
	public String getPnrEmailId() {
		return pnrEmailId;
	}
	public void setPnrEmailId(String pnrEmailId) {
		this.pnrEmailId = pnrEmailId;
	}
	public String getPnrDomain() {
		return pnrDomain;
	}
	public void setPnrDomain(String pnrDomain) {
		this.pnrDomain = pnrDomain;
	}
	public String getPnrAddressLine1() {
		return pnrAddressLine1;
	}
	public void setPnrAddressLine1(String pnrAddressLine1) {
		this.pnrAddressLine1 = pnrAddressLine1;
	}
	public String getPnrAddressLine2() {
		return pnrAddressLine2;
	}
	public void setPnrAddressLine2(String pnrAddressLine2) {
		this.pnrAddressLine2 = pnrAddressLine2;
	}
	public String getPnrAddressLine3() {
		return pnrAddressLine3;
	}
	public void setPnrAddressLine3(String pnrAddressLine3) {
		this.pnrAddressLine3 = pnrAddressLine3;
	}
	public String getPnrAddressCity() {
		return pnrAddressCity;
	}
	public void setPnrAddressCity(String pnrAddressCity) {
		this.pnrAddressCity = pnrAddressCity;
	}
	public String getPnrAddressState() {
		return pnrAddressState;
	}
	public void setPnrAddressState(String pnrAddressState) {
		this.pnrAddressState = pnrAddressState;
	}
	public String getPnrAddressCountry() {
		return pnrAddressCountry;
	}
	public void setPnrAddressCountry(String pnrAddressCountry) {
		this.pnrAddressCountry = pnrAddressCountry;
	}
	public String getPnrAddressPostalCode() {
		return pnrAddressPostalCode;
	}
	public void setPnrAddressPostalCode(String pnrAddressPostalCode) {
		this.pnrAddressPostalCode = pnrAddressPostalCode;
	}
	public String getPnrPhoneNumber() {
		return pnrPhoneNumber;
	}
	public void setPnrPhoneNumber(String pnrPhoneNumber) {
		this.pnrPhoneNumber = pnrPhoneNumber;
	}
	public String getPnrPhoneCity() {
		return pnrPhoneCity;
	}
	public void setPnrPhoneCity(String pnrPhoneCity) {
		this.pnrPhoneCity = pnrPhoneCity;
	}
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getCreditCardExpiration() {
		return creditCardExpiration;
	}
	public void setCreditCardExpiration(String creditCardExpiration) {
		this.creditCardExpiration = creditCardExpiration;
	}
	public String getCreditCardHolderName() {
		return creditCardHolderName;
	}
	public void setCreditCardHolderName(String creditCardHolderName) {
		this.creditCardHolderName = creditCardHolderName;
	}
	public String getFreequentFlyerNumber() {
		return freequentFlyerNumber;
	}
	public void setFreequentFlyerNumber(String freequentFlyerNumber) {
		this.freequentFlyerNumber = freequentFlyerNumber;
	}
	public String getFreequentFlyerAirline() {
		return freequentFlyerAirline;
	}
	public void setFreequentFlyerAirline(String freequentFlyerAirline) {
		this.freequentFlyerAirline = freequentFlyerAirline;
	}
	
}
