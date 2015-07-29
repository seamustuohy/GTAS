package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class TRA holds Transport identifier
 * To specify transport service(s) which is/are to be updated or cancelled.
 *
 * Examples:
 * Flight number 123 operated by Delta(TRA+DL+123:Y”)
 * Gr.5 portion of the message
 * TVL+121210:0915::1230+LHR+JFK+DL+324:B'
 * TRA+KL+8734:B’ Operating carrier information
 *
 *
 */
public class TRA extends Segment{
	
	//operating airline designator code when different from the marketing airline.
	private String airlineDesignatorCode;
	private String flightNumber;
	private String bookingDesignator;
	private String flightSuffix;
		

	public TRA(Composite[] composites) {
		super(TRA.class.getSimpleName(), composites);
	}
}
