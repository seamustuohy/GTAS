package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * NAT NATIONALITY
 * <p>
 * Function: To specify a nationality.
 * <p>
 * Example: NAT+2+CAN’ Indicates current nationality as a Canadian
 */
public class NAT extends Segment {
    /** ICAO 9303/ISO 3166 codes */
    private String nationalityCode;
    
    public NAT(Composite[] composites) {
        super(NAT.class.getSimpleName(), composites);
        this.nationalityCode = composites[1].getValue();
    }

    public String getNationalityCode() {
        return nationalityCode;
    }
}
