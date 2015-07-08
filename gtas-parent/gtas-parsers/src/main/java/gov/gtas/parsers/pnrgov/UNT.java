package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * To end and check the completeness of a message by counting the segments in the message
 * (including UNH and UNT) and validating that the message reference number equates to data
 * element 0062 in the UNH segment
 * ex:UNT+2578+MSG001´ ,UNT+2578+1’
 * @author GTAS4
 *
 */
public class UNT extends Segment {
	
	private String numberOfSegments;
	private String messageRefNumber;

	public UNT(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	public String getNumberOfSegments() {
		return numberOfSegments;
	}


	public void setNumberOfSegments(String numberOfSegments) {
		this.numberOfSegments = numberOfSegments;
	}


	public String getMessageRefNumber() {
		return messageRefNumber;
	}


	public void setMessageRefNumber(String messageRefNumber) {
		this.messageRefNumber = messageRefNumber;
	}

}
