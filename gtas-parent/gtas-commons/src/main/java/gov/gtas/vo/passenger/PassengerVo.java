package gov.gtas.vo.passenger;

import gov.gtas.validators.Validatable;
import gov.gtas.vo.BaseVo;
import gov.gtas.vo.PnrVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PassengerVo extends BaseVo implements Validatable {
    /**
     * a unique passenger reference identifier (from PNR) used to cross
     * reference passenger information in a PNR
     */
    private String travelerReferenceNumber;
    
    private String title;    
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String gender;
    private String citizenshipCountry;
    private String residencyCountry;
    private String passengerType;
    private Integer age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)
    private Date dob;
    private String embarkation;
    private String debarkation;
    private String embarkCountry;
    private String debarkCountry;
    private Boolean deleted = Boolean.FALSE;
    private String seat;
    private String paxId;
    // flight info
    private String flightId;
    private String flightNumber;
    private String fullFlightNumber;
    private String carrier;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)        
    private Date etd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)        
    private Date eta;
    // hits info
    private Boolean onRuleHitList = Boolean.FALSE;
    private Boolean onWatchList = Boolean.FALSE;
    private String flightOrigin;
    private String flightDestination;
    private String flightETD;
    private String flightETA;
    private int ruleHits;
    private int listHits;
    private int paxListHit;
    private int docListHit;
    private List<PassengerVo> passengers;
	private List<DocumentVo> documents = new ArrayList<>();    
	private FlightHistoryVo flightHistoryVo;
    private PnrVo pnrVo;
    
    
	public String getPaxId() {
		return paxId;
	}
	public void setPaxId(String paxId) {
		this.paxId = paxId;
	}
	public String getFlightOrigin() {
		return flightOrigin;
	}
	public void setFlightOrigin(String flightOrigin) {
		this.flightOrigin = flightOrigin;
	}
	public String getFlightDestination() {
		return flightDestination;
	}
	public void setFlightDestination(String flightDestination) {
		this.flightDestination = flightDestination;
	}
	public String getFlightETD() {
		return flightETD;
	}
	public void setFlightETD(String flightETD) {
		this.flightETD = flightETD;
	}
	public String getFlightETA() {
		return flightETA;
	}
	public void setFlightETA(String flightETA) {
		this.flightETA = flightETA;
	}
	public int getRuleHits() {
		return ruleHits;
	}
	public void setRuleHits(int ruleHits) {
		this.ruleHits = ruleHits;
	}
	public int getListHits() {
		return listHits;
	}
	public void setListHits(int listHits) {
		this.listHits = listHits;
	}
	public int getPaxListHit() {
		return paxListHit;
	}
	public void setPaxListHit(int paxListHit) {
		this.paxListHit = paxListHit;
	}
	public int getDocListHit() {
		return docListHit;
	}
	public void setDocListHit(int docListHit) {
		this.docListHit = docListHit;
	}
	public List<PassengerVo> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PassengerVo> passengers) {
		this.passengers = passengers;
	}
	public PnrVo getPnrVo() {
		return pnrVo;
	}
	public void setPnrVo(PnrVo pnrVo) {
		this.pnrVo = pnrVo;
	}
	public String getFlightId() {
        return flightId;
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
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
    public FlightHistoryVo getFlightHistoryVo() {
		return flightHistoryVo;
	}
	public void setFlightHistoryVo(FlightHistoryVo flightHistoryVo) {
		this.flightHistoryVo = flightHistoryVo;
	}
	public String getCarrier() {
        return carrier;
    }
    public void setCarrier(String carrier) {
        this.carrier = carrier;
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
    public void addDocument(DocumentVo d) {
        documents.add(d);
    }
    public List<DocumentVo> getDocuments() {
        return documents;
    }
    public String getTravelerReferenceNumber() {
        return travelerReferenceNumber;
    }
    public void setTravelerReferenceNumber(String travelerReferenceNumber) {
        this.travelerReferenceNumber = travelerReferenceNumber;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getCitizenshipCountry() {
        return citizenshipCountry;
    }
    public void setCitizenshipCountry(String citizenshipCountry) {
        this.citizenshipCountry = citizenshipCountry;
    }
    public String getResidencyCountry() {
        return residencyCountry;
    }
    public void setResidencyCountry(String residencyCountry) {
        this.residencyCountry = residencyCountry;
    }
    public String getPassengerType() {
        return passengerType;
    }
    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }
    public String getEmbarkation() {
        return embarkation;
    }
    public void setEmbarkation(String embarkation) {
        this.embarkation = embarkation;
    }
    public String getDebarkation() {
        return debarkation;
    }
    public void setDebarkation(String debarkation) {
        this.debarkation = debarkation;
    }
    public String getEmbarkCountry() {
        return embarkCountry;
    }
    public void setEmbarkCountry(String embarkCountry) {
        this.embarkCountry = embarkCountry;
    }
    public String getDebarkCountry() {
        return debarkCountry;
    }
    public void setDebarkCountry(String debarkCountry) {
        this.debarkCountry = debarkCountry;
    }
    public String getSeat() {
        return seat;
    }
    public void setSeat(String seat) {
        this.seat = seat;
    }
    public void setDocuments(List<DocumentVo> documents) {
        this.documents = documents;
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public Boolean getOnRuleHitList() {
        return onRuleHitList;
    }
    public void setOnRuleHitList(Boolean onRuleHitList) {
        this.onRuleHitList = onRuleHitList;
    }
    public Boolean getOnWatchList() {
        return onWatchList;
    }
    public void setOnWatchList(Boolean onWatchList) {
        this.onWatchList = onWatchList;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }

	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.firstName) || StringUtils.isBlank(this.lastName) 
				|| StringUtils.isBlank(this.gender) || this.age == null || this.dob == null 
				|| StringUtils.isBlank(this.citizenshipCountry)){
			return false;
		}
		return true;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dob == null) ? 0 : dob.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
        result = prime * result + ((passengerType == null) ? 0 : passengerType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PassengerVo other = (PassengerVo) obj;
        if (dob == null) {
            if (other.dob != null)
                return false;
        } else if (!dob.equals(other.dob))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (middleName == null) {
            if (other.middleName != null)
                return false;
        } else if (!middleName.equals(other.middleName))
            return false;
        if (passengerType == null) {
            if (other.passengerType != null)
                return false;
        } else if (!passengerType.equals(other.passengerType))
            return false;
        return true;
    }
}