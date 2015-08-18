package gov.gtas.delegates.vo;

import java.io.Serializable;

import javax.persistence.Column;

public class AgencyVo extends BaseVo implements Serializable  {
	
	private String agencyName;
	private String agencyIdentifier;
	private String agencyCity;
	private String agencyState;
	private String agencyCountry;
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
	
	
}
