package gov.gtas.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.validators.Validatable;
import gov.gtas.vo.MessageVo;
import gov.gtas.vo.passenger.AddressVo;
import gov.gtas.vo.passenger.AgencyVo;
import gov.gtas.vo.passenger.CreditCardVo;
import gov.gtas.vo.passenger.EmailVo;
import gov.gtas.vo.passenger.FlightVo;
import gov.gtas.vo.passenger.FrequentFlyerVo;
import gov.gtas.vo.passenger.PassengerVo;
import gov.gtas.vo.passenger.PhoneVo;
import gov.gtas.vo.passenger.PnrReportingAgentVo;

public class PnrVo extends MessageVo implements Validatable{
    private String messageCode;
    
    private String recordLocator;
    private String carrier;
    private String origin;
    private String originCountry;
    private Date dateBooked;
    private Date dateReceived;
    private Date departureDate;
    private Integer passengerCount;
    private Integer bagCount;
    private String formOfPayment;
    private String updateMode;
    private Integer totalDwellTime;
    private String dwellAirport;
    private String dwellCountry;
    private Integer dwellDuration;
    private String route;
    private String raw;
    private List<String> rawList = new ArrayList<String>();
    private Integer daysBookedBeforeTravel;
    private AgencyVo agency;
 
    private List<FlightVo> flights = new ArrayList<>();
    private List<PassengerVo> passengers = new ArrayList<>();
    private List<PnrReportingAgentVo> reportingParties = new ArrayList<>();
    private List<AddressVo> addresses = new ArrayList<>();
    private List<PhoneVo> phoneNumbers = new ArrayList<>();
    private List<CreditCardVo> creditCards = new ArrayList<>();
    private List<FrequentFlyerVo> frequentFlyerDetails = new ArrayList<>();
    private List<EmailVo> emails = new ArrayList<>();
    
    public PnrVo() {
        this.bagCount = 0;
        this.passengerCount = 0;
    }
     
    public List<EmailVo> getEmails() {
		return emails;
	}

	public void setEmails(List<EmailVo> emails) {
		this.emails = emails;
	}


	public Integer getTotalDwellTime() {
		return totalDwellTime;
	}

	public void setTotalDwellTime(Integer totalDwellTime) {
		this.totalDwellTime = totalDwellTime;
	}

	public String getDwellAirport() {
		return dwellAirport;
	}

	public void setDwellAirport(String dwellAirport) {
		this.dwellAirport = dwellAirport;
	}

	public String getDwellCountry() {
		return dwellCountry;
	}

	public void setDwellCountry(String dwellCountry) {
		this.dwellCountry = dwellCountry;
	}

	public Integer getDwellDuration() {
		return dwellDuration;
	}

	public void setDwellDuration(Integer dwellDuration) {
		this.dwellDuration = dwellDuration;
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

	public List<String> getRawList() {
		return rawList;
	}

	public void setRawList(List<String> rawList) {
		this.rawList = rawList;
	}

	public Integer getDaysBookedBeforeTravel() {
		return daysBookedBeforeTravel;
	}

	public void setDaysBookedBeforeTravel(Integer daysBookedBeforeTravel) {
		this.daysBookedBeforeTravel = daysBookedBeforeTravel;
	}

	public AgencyVo getAgency() {
		return agency;
	}

	public void setAgency(AgencyVo agency) {
		this.agency = agency;
	}

	public String getMessageCode() {
        return messageCode;
    }


    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
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

    public String getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(String updateMode) {
        this.updateMode = updateMode;
    }

    public List<FlightVo> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightVo> flights) {
        this.flights = flights;
    }

    public List<PassengerVo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerVo> passengers) {
        this.passengers = passengers;
    }

    public List<PnrReportingAgentVo> getReportingParties() {
        return reportingParties;
    }

    public void setReportingParties(List<PnrReportingAgentVo> reportingParties) {
        this.reportingParties = reportingParties;
    }

    public List<AddressVo> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressVo> addresses) {
        this.addresses = addresses;
    }

    public List<PhoneVo> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneVo> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<CreditCardVo> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardVo> creditCards) {
        this.creditCards = creditCards;
    }

    public List<FrequentFlyerVo> getFrequentFlyerDetails() {
        return frequentFlyerDetails;
    }

    public void setFrequentFlyerDetails(List<FrequentFlyerVo> frequentFlyerDetails) {
        this.frequentFlyerDetails = frequentFlyerDetails;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.recordLocator) || StringUtils.isBlank(this.carrier)){
			return false;
		}
		return true;
	}
}
