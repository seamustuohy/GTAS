package gov.cbp.taspd.gtas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "pax")
public class Pax extends BaseEntityAudit {
    public Pax() { }
    
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        mappedBy = "passengers",
        targetEntity = Flight.class
    )    
    private Set<Flight> flights = new HashSet<>();

    private String title;
    
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    private String suffix;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @ManyToOne
    @JoinColumn(name = "citizenship_country", referencedColumnName="id")     
    private Country citizenshipCountry;

    @ManyToOne
    @JoinColumn(name = "residency_country", referencedColumnName="id")     
    private Country residencyCountry;

    @Enumerated(EnumType.STRING)
    private PaxType type;
    
    @Type(type="date")
    private Date dob;
    private Integer age;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")         
    private Airport embarkation;

    @ManyToOne
    @JoinColumn(referencedColumnName="id")         
    private Airport debarkation;
    
    @ManyToOne
    @JoinColumn(name = "embark_country", referencedColumnName="id")     
    private Country embarkCountry;

    @ManyToOne
    @JoinColumn(name = "debark_country", referencedColumnName="id")     
    private Country debarkCountry;
    
    @OneToMany(mappedBy="pax")
    private Set<Document> documents = new HashSet<>();
    
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
    public PaxType getType() {
        return type;
    }
    public void setType(PaxType type) {
        this.type = type;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Airport getEmbarkation() {
        return embarkation;
    }
    public void setEmbarkation(Airport embarkation) {
        this.embarkation = embarkation;
    }
    public Airport getDebarkation() {
        return debarkation;
    }
    public void setDebarkation(Airport debarkation) {
        this.debarkation = debarkation;
    }
    public Country getCitizenshipCountry() {
        return citizenshipCountry;
    }
    public void setCitizenshipCountry(Country citizenshipCountry) {
        this.citizenshipCountry = citizenshipCountry;
    }
    public Country getResidencyCountry() {
        return residencyCountry;
    }
    public void setResidencyCountry(Country residencyCountry) {
        this.residencyCountry = residencyCountry;
    }
    public Country getEmbarkCountry() {
        return embarkCountry;
    }
    public void setEmbarkCountry(Country embarkCountry) {
        this.embarkCountry = embarkCountry;
    }
    public Country getDebarkCountry() {
        return debarkCountry;
    }
    public void setDebarkCountry(Country debarkCountry) {
        this.debarkCountry = debarkCountry;
    }
    public Set<Document> getDocuments() {
        return documents;
    }
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        result = prime * result + ((dob == null) ? 0 : dob.hashCode());
        result = prime * result
                + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result
                + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result
                + ((middleName == null) ? 0 : middleName.hashCode());
        result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pax other = (Pax) obj;
        if (age == null) {
            if (other.age != null)
                return false;
        } else if (!age.equals(other.age))
            return false;
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
        if (gender != other.gender)
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
        if (suffix == null) {
            if (other.suffix != null)
                return false;
        } else if (!suffix.equals(other.suffix))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }
}
