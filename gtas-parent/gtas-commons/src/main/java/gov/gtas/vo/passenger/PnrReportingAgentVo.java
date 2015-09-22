package gov.gtas.vo.passenger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.validators.Validatable;

public class PnrReportingAgentVo implements Validatable{
    private String airlineCode;
    private String locationCode;
    private String identificationNumber;
    private String identificationCode;
    private String locationIdentificationCode;
    private String countryCode;
    private String currencyCode;
    private String localeCode;
    
    public String getAirlineCode() {
        return airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }
    public String getLocationCode() {
        return locationCode;
    }
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
    public String getIdentificationNumber() {
        return identificationNumber;
    }
    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
    public String getIdentificationCode() {
        return identificationCode;
    }
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    public String getLocationIdentificationCode() {
        return locationIdentificationCode;
    }
    public void setLocationIdentificationCode(String locationIdentificationCode) {
        this.locationIdentificationCode = locationIdentificationCode;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public String getLocaleCode() {
        return localeCode;
    }
    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }

	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.airlineCode) ){
			return false;
		}
		return true;
	}
}
