package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * MSG class holds the MSG segment value and specifies the function of the message.
 * (Message action details)
 * @author GTAS4
 * Value 22 means a Full transmission of all PNR data for a flight 
 * Value 141 means changed PNRs only.Ex:MSG+:22' or MSG+:141'
 * Business Function, Coded (Element 4025) is only used in the MSG Gr9 of PNRGOV to specify the 
 * type of service (car, hotel, train, etc.)
 * If MSG is used at Level 0 of PNRGOV or ACKRES, 4025 is not needed
 * Data element responseCode is N/A if the MSG is used in the PNRGOV and GOVREQ messages.
 * Data element responseCode is M* if the MSG is used in the ACKRES message
 * 
 * To specify that the TVL is for a hotel segment.(MSG+8')
 * Push PNR data to States(MSG+:22’)
 * To identify a change PNRGOV message(MSG+:141’)
 */

public class MSG extends Segment{

	private String messageTypeCode;
	private String actionRequested;
	private String responseCode;//Not Applicable for PNRGOV
	
	
	public MSG(String name, Composite[] composites) {
		super(name, composites);
	}


	public String getMessageTypeCode() {
		return messageTypeCode;
	}


	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}


	public String getActionRequested() {
		return actionRequested;
	}


	public void setActionRequested(String actionRequested) {
		this.actionRequested = actionRequested;
	}


	public String getResponseCode() {
		return responseCode;
	}


	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

}
