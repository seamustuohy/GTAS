package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * ORG: ORIGINATOR OF REQUEST DETAILS (LEVEL 0)
 * <p>
 * Specifies the sender of the message.(To specify the point of sale details.)
 * (Originator of request details)
 */
public class ORG_L0 extends Segment {

    private String airlineCode;
    private String airportCode;
    
    public ORG_L0(Composite[] composites) {
        super("ORG", composites);
        Composite c = this.composites[0];
        this.airlineCode = c.getElement(0);
        this.airportCode = c.getElement(1);
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public String getAirportCode() {
        return airportCode;
    }
}
