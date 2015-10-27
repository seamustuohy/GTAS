package gov.gtas.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "agency")
public class Agency extends BaseEntityAudit {
    private static final long serialVersionUID = 1L;
    public Agency() { }
    
	private String name;
	
	private String identifier;
	
	private String city;
	
	private String state;
	
	private String country;

	private String phone;
	
    @ManyToMany(
        mappedBy = "agencies",
        targetEntity = Pnr.class
    )
    private Set<Pnr> pnrs = new HashSet<>();
    
	public Set<Pnr> getPnrs() {
		return pnrs;
	}

	public void setPnrs(Set<Pnr> pnrs) {
		this.pnrs = pnrs;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(this.name,this.identifier);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Agency other = (Agency) obj;
        return Objects.equals(this.name, other.name)
        		&& Objects.equals(this.identifier, other.identifier);
    }    	
}
