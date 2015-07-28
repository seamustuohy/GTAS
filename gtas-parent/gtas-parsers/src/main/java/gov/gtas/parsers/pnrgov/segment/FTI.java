package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class FTI to hold Frequent Traveler Information for a passenger
 * @author GTAS4
 *
 * Ex:A United Airlines Frequent Traveller.(FTI+UA:12345678964')
 * Passenger is using frequent flyer account on airline ZZ.(FTI+ZZ:001012693109')
 * Passenger has a British Airways Frequent Traveller number, is a BA GOLD member and description 
 * of tier level is GOLD. Passenger also has a One World (code 701) alliance Emerald member.
 * (FTI+BA:12345678:::GOLD::GOLD+BA:12345678:::EMER::EMERALD:701')
 */
public class FTI extends Segment{

	private String airlineCode;
	private String freqTravelerNumber;
	private String memberInfo;//membership level
	private String itemDescription;//tier description
	private String companyIdentification;//
	
	public FTI(Composite[] composites) {
		super(FTI.class.getSimpleName(), composites);
	}
}
