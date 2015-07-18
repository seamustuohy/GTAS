package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;


/**
 * Class TBD holds Traveler Baggage Details(To specify the baggage details, 
 * including number of bags and serial numbers.)
 * This segment is for the checked in baggage and not for excess bag details
 * 
 * @author GTAS4
 *
 * Examples
 * Bag pool members with Head of Pool ticket
 * TBD+++MP:0741234123456’3 bags, weight 84 kilos, Head of Pool, tags 4074902824, 3 in sequence to MSP.
 * TBD++3:84:700++HP+KL:4074902824:3:MSP’Total 5 bags, weight 155 pounds, 2 checked to MSP, 3 short
 * checked to JFK (TBD++5:155:701+++KL: 8074902824:2:MSP+ KL: 8074902826:3:JFK’)
 * 
 * Total 2 bags, weight 20 kilos, head of pool, 2 bags in sequence to CPH with the carrier code of 
 * the airline issuing the bag tags.(TBD++2:20:700++HP:5+LH: 3020523456:2:CPH:220’)
 * 
 * 2 bags, tag QF111111 to Sydney(TBD++2+++QF: 0081111111:2:SYD’)
 * 
 * 1 bag, no weight provided(TBD++1+++UA:4016722105:1:DOH)
 * 
 */
public class TBD extends Segment{
	
	private String quantity;
	private String baggageWeight;
	private String unitIdentifier;//700 for kilos-701 for pounds
	private String pooledCheckedBagIndicator;
	private String pooledCheckedBagIdentifier;
	//bag tag details [] of details
	private String[] airlineDesignatorStrings;
	private String airlineDesignator;
	private String bagLicensePlate;
	private String numberOfTags;
	private String placeOfDestination;
	private String tagIssuerCode;
	private String dataIndicator;
	private String releaseBaggageCode;
	

	public TBD(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}


	public String getQuantity() {
		return quantity;
	}


	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


	public String getBaggageWeight() {
		return baggageWeight;
	}


	public void setBaggageWeight(String baggageWeight) {
		this.baggageWeight = baggageWeight;
	}


	public String getUnitIdentifier() {
		return unitIdentifier;
	}


	public void setUnitIdentifier(String unitIdentifier) {
		this.unitIdentifier = unitIdentifier;
	}


	public String getPooledCheckedBagIndicator() {
		return pooledCheckedBagIndicator;
	}


	public void setPooledCheckedBagIndicator(String pooledCheckedBagIndicator) {
		this.pooledCheckedBagIndicator = pooledCheckedBagIndicator;
	}


	public String getPooledCheckedBagIdentifier() {
		return pooledCheckedBagIdentifier;
	}


	public void setPooledCheckedBagIdentifier(String pooledCheckedBagIdentifier) {
		this.pooledCheckedBagIdentifier = pooledCheckedBagIdentifier;
	}


	public String[] getAirlineDesignatorStrings() {
		return airlineDesignatorStrings;
	}


	public void setAirlineDesignatorStrings(String[] airlineDesignatorStrings) {
		this.airlineDesignatorStrings = airlineDesignatorStrings;
	}


	public String getAirlineDesignator() {
		return airlineDesignator;
	}


	public void setAirlineDesignator(String airlineDesignator) {
		this.airlineDesignator = airlineDesignator;
	}


	public String getBagLicensePlate() {
		return bagLicensePlate;
	}


	public void setBagLicensePlate(String bagLicensePlate) {
		this.bagLicensePlate = bagLicensePlate;
	}


	public String getNumberOfTags() {
		return numberOfTags;
	}


	public void setNumberOfTags(String numberOfTags) {
		this.numberOfTags = numberOfTags;
	}


	public String getPlaceOfDestination() {
		return placeOfDestination;
	}


	public void setPlaceOfDestination(String placeOfDestination) {
		this.placeOfDestination = placeOfDestination;
	}


	public String getTagIssuerCode() {
		return tagIssuerCode;
	}


	public void setTagIssuerCode(String tagIssuerCode) {
		this.tagIssuerCode = tagIssuerCode;
	}


	public String getDataIndicator() {
		return dataIndicator;
	}


	public void setDataIndicator(String dataIndicator) {
		this.dataIndicator = dataIndicator;
	}


	public String getReleaseBaggageCode() {
		return releaseBaggageCode;
	}


	public void setReleaseBaggageCode(String releaseBaggageCode) {
		this.releaseBaggageCode = releaseBaggageCode;
	}

	
}
class BAGTAG{
	
}
class BAGGAGEDETAILS{
	
}