package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * 
 * @author GTAS4
 *
 */
public class UNE extends Segment{

	private String numberOfMessages;
	private String identificationNumber;

	public UNE(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}
	
	public String getNumberOfMessages() {
		return numberOfMessages;
	}

	public void setNumberOfMessages(String numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}



}
