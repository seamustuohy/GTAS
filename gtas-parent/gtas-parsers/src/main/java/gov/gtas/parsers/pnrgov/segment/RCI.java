package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class RCI to hold Reservation control information
 * @author GTAS4
 * 
 * The composite will appear at least once and may be repeated up to eight more times.
 * Examples:
 * SAS passenger record reference.(RCI+SK:12DEF')
 * Galileo and SAS record references.(RCI+SK:123EF+1G:345ABC')
 * Delta is the operating carrier and the PNR was created on 24 February 2010 at 2230 GMT.
 * (RCI+DL:ABC456789::240210:2230')
 * CX is the operating carrier and no PNR was received from the reservation system at a station 
 * handled by a ground handler; therefore the CX reservation PNR locator is not available and 
 * “DCS reference” is the Reservation Control Type.(RCI+CX:89QM3LABML:C’)
 * 
 *
 */
public class RCI extends Segment{

	private String reservationControlNumber;//M*
	private String airlineCode;//M*
	private String reservationControType;
	private String dateCreated;//ddmmyy
	private String timeCreated;//msmsms
	private String[] reservationControlNumbers;
	private String[] airlineCodes;
	
	
	
	public RCI(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	public String getReservationControlNumber() {
		return reservationControlNumber;
	}

	public void setReservationControlNumber(String reservationControlNumber) {
		this.reservationControlNumber = reservationControlNumber;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getReservationControType() {
		return reservationControType;
	}

	public void setReservationControType(String reservationControType) {
		this.reservationControType = reservationControType;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String[] getReservationControlNumbers() {
		return reservationControlNumbers;
	}

	public void setReservationControlNumbers(String[] reservationControlNumbers) {
		this.reservationControlNumbers = reservationControlNumbers;
	}

	public String[] getAirlineCodes() {
		return airlineCodes;
	}

	public void setAirlineCodes(String[] airlineCodes) {
		this.airlineCodes = airlineCodes;
	}

}
