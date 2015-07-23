package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * CPI CHARGE PAYMENT INSTRUCTIONS
 * <p>
 * Function: To identify a charge.
 */
public class CPI extends Segment {
    public CPI(Composite[] composites) {
        super(CPI.class.getSimpleName(), composites);
    }
}
