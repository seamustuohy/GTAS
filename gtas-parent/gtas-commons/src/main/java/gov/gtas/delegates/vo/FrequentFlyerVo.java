package gov.gtas.delegates.vo;

import java.io.Serializable;

import javax.persistence.Column;

public class FrequentFlyerVo extends BaseVo implements Serializable  {
	
    private String airlineCode;
    private String frequentFlyerNumber;
    
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
    
    
}
