package gov.cbp.taspd.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Type;

@Entity
public class Pax extends BaseEntity {
    public Pax() { }
    
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "passengers",
            targetEntity = Flight.class
    )    
    private Set<Flight> flights = new HashSet<>();

    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    
    @Type(type="date")
    private Date dob;
    private Integer age;
    private String embarkation;
    private String debarkation;
    
    public Set<Flight> getFlights() {
        return flights;
    }
    public void setFlights(Set<Flight> flights) {
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
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
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
    
    
}
