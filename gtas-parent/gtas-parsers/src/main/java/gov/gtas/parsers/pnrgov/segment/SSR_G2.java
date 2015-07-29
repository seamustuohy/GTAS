package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * SSR: SPECIAL REQUIREMENTS DETAILS
 * <p>To specify special requests or services information relating to a traveler.
 * SSR’s in GR.2 apply to the specific passenger.
 */
public class SSR_G2 extends Segment {

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
	
	public SSR_G2(Composite[] composites) {
		super("SSR", composites);
	}
}
