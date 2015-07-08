package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class DAT to hold Date and time information
 * To convey information regarding estimated or actual dates and times of operational events.
 *  * @author GTAS4
 *
 *  DAT at GR1 can contain ticket issue date and last PNR transaction date/Time
 *  DAT at GR6 will be check-in transaction date/time as stored by RES systems holding DC data
 *  DAT at GR10 will hold PNR History transaction date/time
 *  DAT at Group 6 holds Check-in information. C688/2005 will be used to specify that date/time is in free text
		format in data element C688/9916.
 *  Unless specifically stated otherwise in bilateral agreement, the time is in Universal Time Coordinated (UTC)
 *  Examples:
 *  Ticket issuance date and time( DAT+710:041159:0730')
 *  Check-in transaction date/time(DAT+2:010604:1800’)
 *  Latest PNR transaction date and time(DAT+700:241097:1005')
 *  Check-in including date time is expressed as free text(DAT+3:L FT WW D014357 12AUG121423Z 1D5723')
 */
public class DAT extends Segment {
	
	private String dateQualifier;
	private String dateString;
	private String timeString;
	
	public DAT(String name, Composite[] composites) {
		super(name, composites);
		
	}

	public String getDateQualifier() {
		return dateQualifier;
	}

	public void setDateQualifier(String dateQualifier) {
		this.dateQualifier = dateQualifier;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

}
