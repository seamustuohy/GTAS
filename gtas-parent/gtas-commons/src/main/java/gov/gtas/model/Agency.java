package gov.gtas.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "agency")
public class Agency extends BaseEntityAudit {

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

/*	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pnr_id",referencedColumnName="id")     
    private PnrData pnrData;
	
	public PnrData getPnrData() {
		return pnrData;
	}

	public void setPnrData(PnrData pnrData) {
		this.pnrData = pnrData;
	}*/

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
        return Objects.hash(this.id);
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
        return Objects.equals(this.id, other.id);
    }    	
}
