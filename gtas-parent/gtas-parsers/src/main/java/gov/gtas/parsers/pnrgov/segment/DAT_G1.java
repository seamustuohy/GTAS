package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrUtils;

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
public class DAT_G1 extends Segment {
    private static String LAST_PNR_TRANS = "700";
    private static String TICKET_ISSUE_DATE = "710";
   
	private Date pnrTransactionDate;
	private Date ticketIssueDate;
	
	public DAT_G1(Composite[] composites) throws ParseException {
		super(DAT_G1.class.getSimpleName(), composites);
		for (int i=0; i<this.composites.length; i++) {
	        Element[] e = this.composites[i].getElements();
		    String code = e[0].getValue();
		    if (code.equals(LAST_PNR_TRANS)) {
		        this.pnrTransactionDate = processDt(e);
		    } else if (code.equals(TICKET_ISSUE_DATE)) {
		        this.ticketIssueDate = processDt(e);
		    }
		}
	}
	
	private Date processDt(Element[] e) throws ParseException {
	    String dt = null;
	    if (e.length >= 2) {
            dt = e[1].getValue();
            if (e.length >= 3) {
                dt += e[2].getValue();
            }
            return PnrUtils.parseDateTime(dt);
        }
	    
	    return null;
	}

    public Date getPnrTransactionDate() {
        return pnrTransactionDate;
    }

    public Date getTicketIssueDate() {
        return ticketIssueDate;
    }
}
