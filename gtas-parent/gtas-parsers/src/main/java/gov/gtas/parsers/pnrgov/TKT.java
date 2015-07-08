package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class TKT holds Ticket number detail(To convey information related to a specific ticket)
 * @author GTAS4
 *
 *Ex:
 *The ticket number for a passenger(TKT+0062230534212:T')
 *Conjunctive ticket – 2 booklets(TKT+0271420067693:T:2')
 *A Ticketless passenger(TKT+:1’)
 */
public class TKT extends Segment{

	private String ticketNumber;
	private String ticketType;//"1" for ticket less.
	private String numberOfBooklets;
	private String dataIndicator;
	private String documentNumberEMD;
	
	public TKT(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getNumberOfBooklets() {
		return numberOfBooklets;
	}

	public void setNumberOfBooklets(String numberOfBooklets) {
		this.numberOfBooklets = numberOfBooklets;
	}

	public String getDataIndicator() {
		return dataIndicator;
	}

	public void setDataIndicator(String dataIndicator) {
		this.dataIndicator = dataIndicator;
	}

	public String getDocumentNumberEMD() {
		return documentNumberEMD;
	}

	public void setDocumentNumberEMD(String documentNumberEMD) {
		this.documentNumberEMD = documentNumberEMD;
	}

}
