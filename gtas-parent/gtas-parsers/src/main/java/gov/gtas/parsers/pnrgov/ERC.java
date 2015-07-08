package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
/**
 * To identify errors in the message sent to the States
 * 
 * Ex:Application Error - Invalid Departure Time(ERC+103')
 * Ex:Invalid flight number(ERC+114')
 * @author GTAS4
 *
 */
public class ERC extends Segment{

	private String errorCode;
	
	public ERC(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
