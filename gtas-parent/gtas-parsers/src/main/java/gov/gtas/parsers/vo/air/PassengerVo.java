package gov.gtas.parsers.vo.air;

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
    private String travelerType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)
    private Date dob;
    private String embarkation;
    private String debarkation;
    private String embarkCountry;
    private String debarkCountry;
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
    public String getTravelerType() {
        return travelerType;
    }
    public void setTravelerType(String travelerType) {
        this.travelerType = travelerType;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
