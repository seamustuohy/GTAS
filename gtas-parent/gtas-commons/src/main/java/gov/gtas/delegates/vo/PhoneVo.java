package gov.gtas.delegates.vo;

import gov.gtas.model.Pnr;
import gov.gtas.validators.Validatable;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PhoneVo extends BaseVo implements Validatable {

	private String phoneType;
	private String phoneNumber;
	private Pnr pnrData;
	//TODO remove in refactoring..
	private String number;
	private String city;
	
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Pnr getPnrData() {
		return pnrData;
	}
	public void setPnrData(Pnr pnrData) {
		this.pnrData = pnrData;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.number) ){
			return false;
		}
		return true;
	}
	
}
