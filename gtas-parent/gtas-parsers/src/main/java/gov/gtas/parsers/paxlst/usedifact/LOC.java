package gov.gtas.parsers.paxlst.usedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class LOC extends Segment {
    public enum LocCode {
        DEPARTURE,
        ARRIVAL
    }
    
    private LocCode locationCode;
    private String iataCountryCode;
    private String iataAirportCode;
    private String c_codeListIdentifier;

    public LOC(Composite[] composites) {
        super(LOC.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                switch (c.getValue()) {
                case "005":
                    this.locationCode = LocCode.DEPARTURE;
                    break;
                case "008":
                    this.locationCode = LocCode.ARRIVAL;
                    break;
                default:
                    System.err.println("unknown location code: " + c.getValue());
                    return;
                }
                break;
            
            case 1:
                // Two-character Country code (IATA), followed by a 3-character Airport code (IATA)
                String code = e[0].getValue();
                this.iataCountryCode = code.substring(0, 2);
                this.iataAirportCode = code.substring(2, code.length());
                
                if (e.length >= 2) {
                    this.c_codeListIdentifier = e[1].getValue();
                }
                break;
            }
        }
    }

    public LocCode getLocationCode() {
        return locationCode;
    }

    public String getIataCountryCode() {
        return iataCountryCode;
    }

    public String getIataAirportCode() {
        return iataAirportCode;
    }

    public String getC_codeListIdentifier() {
        return c_codeListIdentifier;
    }
}