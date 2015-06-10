package gov.cbp.taspd.gtas.parsers.paxlst.unedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class TDT extends Segment {
    private String transportStageQualifier;
    private String c_journeyIdentifier;
    private String c_carrierIdentifier;
    
    public TDT(Composite[] composites) {
        super(TDT.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.transportStageQualifier = c.getValue();
                break;
            case 1:
                this.c_journeyIdentifier = c.getValue();
                break;
            case 4:
                this.c_carrierIdentifier = c.getValue();
                break;
            }
        }
    }

    public String getTransportStageQualifier() {
        return transportStageQualifier;
    }

    public String getC_journeyIdentifier() {
        return c_journeyIdentifier;
    }

    public String getC_carrierIdentifier() {
        return c_carrierIdentifier;
    }
}
