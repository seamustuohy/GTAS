package gov.cbp.taspd.gtas.parsers.paxlst.usedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class TDT extends Segment {
    private String c_flightNumber;
    private String c_modeOfTransport;
    private String c_airlineCode;
    private boolean isCrewOnlyManifest;
    
    public TDT(Composite[] composites) {
        super(TDT.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 1:
                this.c_flightNumber = c.getValue();
                if (this.c_flightNumber != null && this.c_flightNumber.endsWith("C")) {
                    isCrewOnlyManifest = true;
                } else {
                    isCrewOnlyManifest = false;
                }
                break;
            case 2:
                this.c_modeOfTransport = c.getValue();
                break;
            case 3:
                this.c_airlineCode = c.getValue();
                break;
            }
        }
    }

    public String getC_flightNumber() {
        return c_flightNumber;
    }

    public String getC_modeOfTransport() {
        return c_modeOfTransport;
    }

    public String getC_airlineCode() {
        return c_airlineCode;
    }

    public boolean isCrewOnlyManifest() {
        return isCrewOnlyManifest;
    }
}