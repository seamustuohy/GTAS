package gov.gtas.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "agency")
public class Agency extends BaseEntityAudit {
    private static final long serialVersionUID = 1L;
    public Agency() { }
    
	@Column(name = "agency_name")
	private String agencyName;
	
	@Column(name = "agency_identifier")
	private String agencyIdentifier;
	
	@Column(name = "agency_city")
	private String agencyCity;
	
	@Column(name = "agency_state")
	private String agencyState;
	
	@Column(name = "agency_country")
	private String agencyCountry;


	private String phoneNumber;
	

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "agency")
    private Set<Pnr> pnrs = new HashSet<>();
    
    public void addPnr(Pnr pnr) {
        if (this.pnrs == null) {
            this.pnrs = new HashSet<>();
        }
        this.pnrs.add(pnr);
        pnr.setAgency(this);
    }
   
	public Set<Pnr> getPnrs() {
		return pnrs;
	}

	public void setPnrs(Set<Pnr> pnrs) {
		this.pnrs = pnrs;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyIdentifier() {
		return agencyIdentifier;
	}

	public void setAgencyIdentifier(String agencyIdentifier) {
		this.agencyIdentifier = agencyIdentifier;
	}

	public String getAgencyCity() {
		return agencyCity;
	}

	public void setAgencyCity(String agencyCity) {
		this.agencyCity = agencyCity;
	}

	public String getAgencyState() {
		return agencyState;
	}

	public void setAgencyState(String agencyState) {
		this.agencyState = agencyState;
	}

	public String getAgencyCountry() {
		return agencyCountry;
	}

	public void setAgencyCountry(String agencyCountry) {
		this.agencyCountry = agencyCountry;
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(this.agencyName,this.agencyIdentifier);
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
        return Objects.equals(this.agencyName, other.agencyName)
        		&& Objects.equals(this.agencyIdentifier, other.agencyIdentifier);
    }    	
}
