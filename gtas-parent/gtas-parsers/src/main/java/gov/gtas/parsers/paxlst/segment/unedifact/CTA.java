package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * CTA CONTACT INFORMATION
 * <p>
 * Function: To identify a person or a department to whom communication should
 * be directed.Contact information
 * <p>
 * Note: this segment appears in version 13b of the UN paxlst spec. It does not
 * exist in prior versions.
 */
public class CTA extends Segment {
    public CTA(Composite[] composites) {
        super(CTA.class.getSimpleName(), composites);
    }
}
