package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class SSR holds Special Requirements Details
 * SSR’s in GR.1 apply to all flights and may apply to all passengers or may apply to specific 
 *   passenger based on the traveler reference number in SSR/9944 and TIF/9944.
 */
public class SSR_G1 extends Segment {

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
	
	public SSR_G1(Composite[] composites) {
		super("SSR", composites);
	}
}
