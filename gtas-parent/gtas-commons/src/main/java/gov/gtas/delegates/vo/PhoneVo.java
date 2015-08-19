package gov.gtas.delegates.vo;

import gov.gtas.model.Pnr;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class PhoneVo extends BaseVo implements Serializable {

	private String phoneType;
	private String phoneNumber;
	private Pnr pnrData;
	
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
	
	
}
