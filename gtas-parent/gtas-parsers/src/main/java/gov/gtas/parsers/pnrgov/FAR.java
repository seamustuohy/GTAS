package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class FAR to hold Fare information for a passenger(s)
 * @author GTAS4
 * Ex:The fare is a 20 percent discounted fare type for an 9 year old child.(FAR+C+9+1:20:US+++YEE3M')
 * The fare is an industry discounted passenger traveling on business with space available(FAR+I++764:4::B2+++C')
 */
public class FAR extends Segment{
	private String passengerType;
	private String age;
	private String numberOfDiscounts;
	private String discount;
	private String countryCode;
	private String discountedFareClassificationType;
	private String fareType;
	private String fareBasisCode;

	public FAR(String name, Composite[] composites) {
		super(name, composites);
		
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNumberOfDiscounts() {
		return numberOfDiscounts;
	}

	public void setNumberOfDiscounts(String numberOfDiscounts) {
		this.numberOfDiscounts = numberOfDiscounts;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDiscountedFareClassificationType() {
		return discountedFareClassificationType;
	}

	public void setDiscountedFareClassificationType(
			String discountedFareClassificationType) {
		this.discountedFareClassificationType = discountedFareClassificationType;
	}

	public String getFareType() {
		return fareType;
	}

	public void setFareType(String fareType) {
		this.fareType = fareType;
	}

	public String getFareBasisCode() {
		return fareBasisCode;
	}

	public void setFareBasisCode(String fareBasisCode) {
		this.fareBasisCode = fareBasisCode;
	}

}
