package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

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
	
	public TKT(List<Composite> composites) {
		super(TKT.class.getSimpleName(), composites);
	}
}
