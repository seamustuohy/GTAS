package gov.gtas.delegates.vo;

import gov.gtas.validators.Validatable;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FrequentFlyerVo extends BaseVo implements Validatable  {
	
    private String airlineCode;
    private String frequentFlyerNumber;
   
    //TODO backward with parser vo.remove in refactoring.
    private String airline;
    private String number;
    
    
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.airlineCode) || StringUtils.isBlank(this.frequentFlyerNumber) ){
			return false;
		}
		return true;
	}
    
}
