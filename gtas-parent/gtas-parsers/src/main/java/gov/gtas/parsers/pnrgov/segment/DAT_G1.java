package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.exception.ParseException;

/**
 * <p>
 * DAT: DATE AND TIME INFORMATION
 * <p>
 * To convey information regarding estimated or actual dates and times of
 * operational events.
 * <p>
 * DAT at GR1 can contain ticket issue date and last PNR transaction date/Time
 * <p>
 * Unless specifically stated otherwise in bilateral agreement, the time is in
 * Universal Time Coordinated (UTC)
 * <p>
 * Examples: Ticket issuance date and time( DAT+710:041159:0730') 
 */
public class DAT_G1 extends DAT {
    private static String LAST_PNR_TRANS = "700";
    private static String TICKET_ISSUE_DATE = "710";
   
	private Date pnrTransactionDate;
	private Date ticketIssueDate;
	
	public DAT_G1(List<Composite> composites) throws ParseException {
		super(composites);
		
        for (DatDetails d : getDateTimes()) {
		    String code = d.getType();
		    if (code.equals(LAST_PNR_TRANS)) {
		        this.pnrTransactionDate = d.getDateTime();
		    } else if (code.equals(TICKET_ISSUE_DATE)) {
		        this.ticketIssueDate = d.getDateTime();
		    }
		}
	}
	
    public Date getPnrTransactionDate() {
        return pnrTransactionDate;
    }

    public Date getTicketIssueDate() {
        return ticketIssueDate;
    }
}