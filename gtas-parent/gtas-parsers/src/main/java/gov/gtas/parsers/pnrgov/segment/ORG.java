package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * class ORG specifies the sender of the message.(To specify the point of sale details.)
 * (Originator of request details)
 * @author GTAS4
 *
 * The ORG at level 0 is the sender of the data.
 *  The ORG in GR.1 at level 2 is the originator of the booking. For “update” pushes when the push 
 *  flight/date is cancelled from a PNR or the complete PNR is cancelled or not found, the ORG is sent 
 *  as an empty segment,i.e., does not contain data.
 *  The ORG in GR.6 at level4 is the agent id who checked in the passenger for this flight segment.
 *  Examples:
 *  The originator of the message is American Airlines agent in Dallas(ORG+AA:DFW')
 *  The originator of the booking is an LH agent located in Amsterdam hosted on Amadeus.(ORG+1A:MUC+12345678:111111+AMS+LH+A+NL:NLG:NL+0001AASU’)
 *  The originator of the booking is an Amadeus travel agent request.(ORG+1A:NCE+12345678:DDGS+++T')
 *  Origination details for a Worldspan travel agent request.(ORG+1P:HDQ+98567420:IPSU+ATL++T+US:USD+GS')
 *  For a cancelled PNR in an “update” push(ORG’)
 *  The originator of the message is The Australian government.(ORG++++++AU')
 */
public class ORG extends Segment{

	private String senderCode;//2-3 character airline/CRS code
	private String locationCode;//ATA/IATA airport/city code of delivering system
	private String travelAgentIdentifier;//ATA/IATA travel agency ID number
	private String reservationSystemCode;
	private String reservationSystemKey;
	private String agentLocationCode;
	private String companyIdentification;
	private String locationIdentification;
	private String originatorTypeCode;
	private String originatorCountryCode;
	private String originatorCurrencyCode;
	private String originatorLanguageCode;
	
	public ORG(String name, Composite[] composites) {
		super(name, composites);
		
	}

	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getTravelAgentIdentifier() {
		return travelAgentIdentifier;
	}

	public void setTravelAgentIdentifier(String travelAgentIdentifier) {
		this.travelAgentIdentifier = travelAgentIdentifier;
	}

	public String getReservationSystemCode() {
		return reservationSystemCode;
	}

	public void setReservationSystemCode(String reservationSystemCode) {
		this.reservationSystemCode = reservationSystemCode;
	}

	public String getReservationSystemKey() {
		return reservationSystemKey;
	}

	public void setReservationSystemKey(String reservationSystemKey) {
		this.reservationSystemKey = reservationSystemKey;
	}

	public String getAgentLocationCode() {
		return agentLocationCode;
	}

	public void setAgentLocationCode(String agentLocationCode) {
		this.agentLocationCode = agentLocationCode;
	}

	public String getCompanyIdentification() {
		return companyIdentification;
	}

	public void setCompanyIdentification(String companyIdentification) {
		this.companyIdentification = companyIdentification;
	}

	public String getLocationIdentification() {
		return locationIdentification;
	}

	public void setLocationIdentification(String locationIdentification) {
		this.locationIdentification = locationIdentification;
	}

	public String getOriginatorTypeCode() {
		return originatorTypeCode;
	}

	public void setOriginatorTypeCode(String originatorTypeCode) {
		this.originatorTypeCode = originatorTypeCode;
	}

	public String getOriginatorCountryCode() {
		return originatorCountryCode;
	}

	public void setOriginatorCountryCode(String originatorCountryCode) {
		this.originatorCountryCode = originatorCountryCode;
	}

	public String getOriginatorCurrencyCode() {
		return originatorCurrencyCode;
	}

	public void setOriginatorCurrencyCode(String originatorCurrencyCode) {
		this.originatorCurrencyCode = originatorCurrencyCode;
	}

	public String getOriginatorLanguageCode() {
		return originatorLanguageCode;
	}

	public void setOriginatorLanguageCode(String originatorLanguageCode) {
		this.originatorLanguageCode = originatorLanguageCode;
	}

}
