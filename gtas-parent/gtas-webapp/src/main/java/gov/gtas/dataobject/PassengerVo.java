package gov.gtas.dataobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PassengerVo {
    private String title;    
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String gender;
    private String citizenshipCountry;
    private String residencyCountry;
    private String passengerType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)
    private Date dob;
    private String embarkation;
    private String debarkation;
    private String embarkCountry;
    private String debarkCountry;
    private String paxId;
    private String seat;
    private String flightId;
    private String flightNumber;
    private String carrier;
    private String flightOrigin;
    private String flightDestination;
    private String flightETD;
    private String flightETA;
    private int ruleHits;
    private int listHits;
    private int paxListHit;
    private int docListHit;

    
    private List<PassengerVo> passengers;
    
    private PnrVo pnrVo;
    
    /**
	 * @return the ruleHits
	 */
	public int getRuleHits() {
		return ruleHits;
	}
	/**
	 * @param ruleHits the ruleHits to set
	 */
	public void setRuleHits(int ruleHits) {
		this.ruleHits = ruleHits;
	}
	public String getFlightId() {
		return flightId;
	}
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}
	/**
	 * @return the listHits
	 */
	public int getListHits() {
		return listHits;
	}
	/**
	 * @param listHits the listHits to set
	 */
	public void setListHits(int listHits) {
		this.listHits = listHits;
	}
    
	public List<PassengerVo> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PassengerVo> passengers) {
		this.passengers = passengers;
	}

	private List<DocumentVo> documents = new ArrayList<>();
    
    public void addDocument(DocumentVo d) {
        documents.add(d);
    }
    
    public List<DocumentVo> getDocuments() {
        return documents;
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
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public PnrVo getPnrVo() {
		return pnrVo;
	}
	public void setPnrVo(PnrVo pnrVo) {
		this.pnrVo = pnrVo;
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
    public String getPaxId() {
		return paxId;
	}
	public void setPaxId(String paxId) {
		this.paxId = paxId;
	}
	public String getPassengerType() {
        return passengerType;
    }
    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
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
    public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
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
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
