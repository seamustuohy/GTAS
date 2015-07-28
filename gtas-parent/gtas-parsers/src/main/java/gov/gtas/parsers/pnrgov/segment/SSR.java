package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class SSR holds Special Requirements Details
 * @author GTAS4
 * SSR’s in GR.1 apply to all flights and may apply to all passengers or may apply to specific 
 *   passenger based on the traveler reference number in SSR/9944 and TIF/9944.
 * SSR’s in GR.2 apply to the specific passenger.
 * SSR’s in GR.5 (per TVL) apply to a specific flight and may apply to all passengers or may 
 * apply to a specific passenger based on the traveler reference number in SSR/9944 and TIF/9944.
 * The Traveler Reference Number (9944) in the SSR segment in Gr.1 or Gr. 5 may be used to specify 
 * for which passenger this SSR applies. This is a reference number assigned by the sending system 
 * and should contain the same reference number as that found in the Traveler Reference number in the TIF in Gr.2.
 * Examples:
 * One passenger is an SSR type unaccompanied minor.(SSR+UMNR')
 * Passenger number 2 has requested to transport a bike on a DL flight.(SSR+BIKE:HK:1:DL+::2’)
 * Passenger has been assigned seat 53C on the AA flight from AMS to JFK.(SSR+SEAT:HK:1:AA:::AMS:JFK+53C::2:N’)
 * DOCS information for a passenger on KL.(SSR+DOCS:HK:1:KL::::://///05AUG70/F//STRIND/BENITA+::2’)
 * Other information about passenger one.(SSR+OTHS:HK::AF:::::CORP//***CORPORATE PSGR***+::1')
 * A passenger by the name of Mr. John Meeks supplies a United States Redress number for his PNR:
 * For those systems using automated format:(SSR+DOCO:HK:1:AA:::JFK:LAX:0001Y28JUN//R/1234567890123///US)
 * For those systems using non-automated format:(SSR+DOCO:HK:1:AA::::://R/1234567890123///US)
 * Passenger has been assigned seat 22C on the PY flight from AUA to PBM.(SSR+SEAT:HK:1:PY:::AUA:PBM NOTICKET/TOM:+22C’)
 * Passenger is an infant traveling with an adult on PY flight from PBM to MIA and the date of birth is 12Jul09
 * (SSR+INFT:HK:1:PY:::PBM:MIA:INFANT/BABY 12JUL09’)
 * A bassinet has been confirmed for the PY flight from MIA to PBM(SSR+BSCT:HK:1:PY:::MIA:PBM’)
 * Passenger has requested a generic seat on the AA flight from DCA to MIA(SSR+NSSA:NN:1:AA:::DCA:MIA:MADDOX/MOLLY’)
 * Passenger traveling with a British passport and 1st and 2nd given names in separate fields:
 * (SSR+DOCS:HK::DL:::::/P/GBR/123456789/GBR/12JUL64/M/23AUG19/SMITHJR/JONATHON/ROBERT’)
 * Passenger traveling with a British passport and 1st and 2nd given names in same field:
 * (SSR+DOCS:HK::DL:::::/P/GBR/987654321/GBR/12JUL15/M/15JAN13/COOPER/GARYWILLIAM’)
 * Passenger traveling with a British passport and 1st and 2nd given names in same field:
 * (SSR+DOCS:HK::DL:::::/P/GBR/123456789/GBR/12JUL12/M/23AUG15/WAYNE/JOHNALVA’)
 * 
 */
public class SSR extends Segment {

	private String typeOfRequest;
	private String actionCodeForRequest;
	private String numberOfRequestsProcessed;
	private String airlineCode;
	private String boardCity;
	private String offCity;
	private String serviceRequestText;
	private String specialRequirementData;
	private String unitQualifier;
	private String travellerReferenceNumber;
	private String seatCharacteristics;
	
	public SSR(Composite[] composites) {
		super(SSR.class.getSimpleName(), composites);
	}
}
