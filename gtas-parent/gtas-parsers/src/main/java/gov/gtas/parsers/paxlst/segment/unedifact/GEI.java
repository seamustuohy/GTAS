package gov.gtas.parsers.paxlst.segment.unedifact;

import java.text.ParseException;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
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

    public GEI(Composite[] composites) throws ParseException {
        super(GEI.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                if (Integer.valueOf(c.getValue()) != 4) {
                    throw new ParseException("unknown gei qualifier: " + c.getValue(), -1);
                }
                break;

            case 1:
                String code = null;
                if (c.getValue() != null) {
                    code = c.getValue();
                } else if (e != null && e.length >= 1) {
                    code = e[0].getValue();
                } else {
                    throw new ParseException("unknown verification code", -1);
                }
                
                switch (code) {
                case "173":
                case "ZZZ":
                    this.verified = true;
                    break;
                case "174":
                    this.verified = false;
                    break;
                default:
                    throw new ParseException("unknown verification code: " + c.getValue(), -1);
                }
                break;
            }
        }
    }

    public boolean isVerified() {
        return verified;
    }
}
