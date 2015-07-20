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
	
	
	public SSD(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}


	public String getSeatNumber() {
		return seatNumber;
	}


	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}


	public String getCabinClass() {
		return cabinClass;
	}


	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

}
