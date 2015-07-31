package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>TRI: TRAVELLER REFERENCE INFORMATION 
 * <p>Check-in info; sequence number.
 */
public class TRI extends Segment{
	public TRI(List<Composite> composites) {
		super(TRI.class.getSimpleName(), composites);
	}
}
