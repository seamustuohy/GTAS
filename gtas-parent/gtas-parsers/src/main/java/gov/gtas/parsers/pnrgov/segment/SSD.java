package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class SSD holds Seat Selection Details
 * @author GTAS4
 * Ex:The passenger has been assigned seat 24A in coach.(SSD+24A++++Y’)
 */
public class SSD extends Segment{

	private String seatNumber;
	private String cabinClass;
	
	
	public SSD(Composite[] composites) {
		super(SSD.class.getSimpleName(), composites);
	}
}
