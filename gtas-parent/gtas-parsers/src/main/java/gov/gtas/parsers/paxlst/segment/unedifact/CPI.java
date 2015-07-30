package gov.gtas.parsers.paxlst.segment.unedifact;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * CPI CHARGE PAYMENT INSTRUCTIONS
 * <p>
 * Function: To identify a charge.
 */
public class CPI extends Segment {
    public CPI(List<Composite> composites) {
        super(CPI.class.getSimpleName(), composites);
    }
}
