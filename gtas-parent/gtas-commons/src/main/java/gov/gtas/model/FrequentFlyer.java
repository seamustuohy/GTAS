package gov.gtas.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "frequent_flyer")
public class FrequentFlyer extends BaseEntityAudit {
    private static final long serialVersionUID = 1L;  
    public FrequentFlyer() { }
    
	@Column(name = "airline_code", length = 4)
    private String airlineCode;
	
	@Column(name = "frequent_flyer_number", nullable = false)
    private String frequentFlyerNumber;
	
    @ManyToMany(
        mappedBy = "frequentFlyers",
        targetEntity = Pnr.class
    )
    private Set<Pnr> pnrs = new HashSet<>();
    
	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getFrequentFlyerNumber() {
		return frequentFlyerNumber;
	}

	public void setFrequentFlyerNumber(String frequentFlyerNumber) {
		this.frequentFlyerNumber = frequentFlyerNumber;
	}
	
    public Set<Pnr> getPnrs() {
        return pnrs;
    }

    public void setPnrs(Set<Pnr> pnrs) {
        this.pnrs = pnrs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.frequentFlyerNumber,this.airlineCode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final FrequentFlyer other = (FrequentFlyer) obj;
        return Objects.equals(this.frequentFlyerNumber, other.frequentFlyerNumber ) && 
        		Objects.equals(this.airlineCode, other.airlineCode);
    }    	
}
