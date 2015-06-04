package gov.cbp.taspd.gtas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Pax {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "passengers",
            targetEntity = Flight.class
    )    
    private Set<Flight> flights = new HashSet<>();

    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String embarkation;
    private String debarkation;

    
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Set<Flight> getFlights() {
        return flights;
    }
    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
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
