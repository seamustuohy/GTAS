package gov.gtas.delegates.vo;

import gov.gtas.model.Document;
import gov.gtas.model.Pnr;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PassengerVo extends BaseVo implements Serializable {

    public PassengerVo() { }
    private String passengerType;
    private Set<FlightVo> flights = new HashSet<FlightVo>();
    private Set<PnrDataVo> pnrs = new HashSet<>();
    private Set<DocumentVo> documents = new HashSet<>();
	private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String gender;
    private String citizenshipCountry;
    private String residencyCountry;
    private Date dob;
    private Integer age;
    private String embarkation;
    private String debarkation;
    private String embarkCountry;
    private String debarkCountry;
    
    
	public Set<PnrDataVo> getPnrs() {
		return pnrs;
	}
	public void setPnrs(Set<PnrDataVo> pnrs) {
		this.pnrs = pnrs;
	}
	public Set<DocumentVo> getDocuments() {
		return documents;
	}
	public void setDocuments(Set<DocumentVo> documents) {
		this.documents = documents;
	}
	public String getPassengerType() {
		return passengerType;
	}
	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}
	public Set<FlightVo> getFlights() {
		return flights;
	}
	public void setFlights(Set<FlightVo> flights) {
		this.flights = flights;
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
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
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
    
    
    //private Set<Document> documents = new HashSet<>();
    
}
