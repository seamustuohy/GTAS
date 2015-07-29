package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * GEI: Processing Information - GR. 4
 * <p>
 * Function: To identify that information for this passenger has been validated.
 * <p>
 * Examples:
 * 
 * GEI+4+173' Indicates that the information contained for this passenger has
 * been verified.
 */
public class GEI extends Segment {
    private boolean verified;

    public GEI(Composite[] composites) {
        super(GEI.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 1:
                switch (c.getElement(0)) {
                case "173":
                case "ZZZ":
                    this.verified = true;
                    break;
                case "174":
                    this.verified = false;
                    break;
                default:
                    this.verified = false;
                }
                break;
            }
        }
    }

    public boolean isVerified() {
        return verified;
    }
}
