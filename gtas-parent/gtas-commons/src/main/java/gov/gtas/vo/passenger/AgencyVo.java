package gov.gtas.vo.passenger;

import gov.gtas.validators.Validatable;

import gov.gtas.vo.BaseVo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AgencyVo implements Validatable  {
	
	private String agencyName;
	private String agencyIdentifier;
	private String agencyCity;
	private String agencyState;
	private String agencyCountry;
	
	public AgencyVo(){
		
	}
	
	public AgencyVo(String aName,String identifier,String city,String state,String country){
		this.agencyName=aName;
		this.agencyIdentifier=identifier;
		this.agencyCity=city;
		this.agencyState=state;
		this.agencyCountry=country;
		
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.agencyIdentifier) ){
			return false;
		}
		return true;
	}
	
}
