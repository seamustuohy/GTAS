package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;


/**
 * Class TRI holds Traveller Reference Information(information regarding a traveller or traveller account .)
 * @author GTAS4
 * 
 * The Traveler Reference Number (9944) in the TRI segment in Gr.7 may be used to specify for which 
 * passenger the check-in information applies so that the TIF in this group does not need to be sent. 
 * This is a reference number assigned by the sending system and should contain the same reference 
 * number as that found in the Traveler Reference number in the TIF in Gr.2.
 * 
 * Each occurrence of the TRI handles only one passenger (i.e. one surname and one given name) at a time, 
 * thus the Composite C671 does not repeat
 *
 * Examples
 * The sequence number for this passenger is 108.(TRI++108')
 * The sequence number for passenger, which has reference number 4, is 220.(TRI++220:::4')
 * The sequence number for passenger, which has reference number 10, is JFK-058.(TRI++JFK-058:::10')
 * No sequence number for the passenger, which has reference number 11.(TRI++:::11')
 */
public class TRI extends Segment{

	private String identityNumber;//null always..not getting set in this version.
	private String boardingNumber;
	//Used to indicate which passenger is being checked in refer to TIF in GR2 level 2.
	private String travelerRefNumber;
	
	public TRI(Composite[] composites) {
		super(TRI.class.getSimpleName(), composites);
	}
}
