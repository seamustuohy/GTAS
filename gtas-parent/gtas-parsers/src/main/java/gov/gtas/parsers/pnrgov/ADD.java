package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class ADD to hold Address Information of a traveler in PNR
 * @author GTAS4
 * 
 * The ADD in GR.1 at level 2 may contain a contact address for the PNR.
 * The ADD in GR.2 at level 3 may contain emergency contact information and or/ UMNR delivery and collection
	addresses
 * The ADD in GR.4 at level 5 may contain the address of the payer of the ticket.
 * If the address and/or telephone information cannot be broken down in separate elements, the information may
	be found in OSIs and SSRs.
 *	Ex:The contact address is 4532 Wilson Street, Philadelphia, zip code 34288
 * (ADD++700:4532 WILSON STREET:PHILADELPHIA:PA::US:34288’)
 */
public class ADD extends Segment{

	private String addressType;
	private String streetDetails;
	private String cityName;
	private String stateCode;
	private String countryCode;
	private String zipCode;
	private String telePhoneText;
	
	
	public ADD(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}
	public String getAddressType() {
		return addressType;
	}


	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}


	public String getStreetDetails() {
		return streetDetails;
	}


	public void setStreetDetails(String streetDetails) {
		this.streetDetails = streetDetails;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getStateCode() {
		return stateCode;
	}


	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getTelePhoneText() {
		return telePhoneText;
	}


	public void setTelePhoneText(String telePhoneText) {
		this.telePhoneText = telePhoneText;
	}

}
