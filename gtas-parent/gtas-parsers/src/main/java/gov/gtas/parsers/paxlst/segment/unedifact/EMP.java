package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * EMP EMPLOYMENT DETAILS
 * <p>
 * Function: To specify employment details.
 */
public class EMP extends Segment {
    public EMP(Composite[] composites) {
        super(EMP.class.getSimpleName(), composites);
    }
}
