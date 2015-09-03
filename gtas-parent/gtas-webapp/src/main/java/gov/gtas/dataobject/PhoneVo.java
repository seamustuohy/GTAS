package gov.gtas.dataobject;


public class PhoneVo {
	
	private String phoneType;
	private String phoneNumber;
	private PnrVo pnrData;
	private String number;
	private String city;
	
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
	public PnrVo getPnrData(){
		return pnrData;
	}
	public void setPnrData(PnrVo pnrData) {
		this.pnrData = pnrData;
	}
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
	
	

}
