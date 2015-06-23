package gov.gtas.parsers.paxlst.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

public class TDT extends Segment {
    private enum TdtType {
        ARRIVING_OR_DEPARTING_FLIGHT,
        OVER_FLIGHT
    }

    private TdtType  transportStageQualifier;
    private String c_journeyIdentifier;
    private String c_carrierIdentifier;    
    private String flightNumber;
    
    public TDT(Composite[] composites) {
        super(TDT.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                int code = Integer.valueOf(c.getValue());
                if (code == 20) {
                    this.transportStageQualifier = TdtType.ARRIVING_OR_DEPARTING_FLIGHT;    
                } else if (code == 34) {
                    this.transportStageQualifier = TdtType.OVER_FLIGHT;
                } else {
                    logger.error("unknown TDT type: " + c.getValue());
                }
                
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

    public TdtType getTransportStageQualifier() {
        return transportStageQualifier;
    }

    public String getC_journeyIdentifier() {
        return c_journeyIdentifier;
    }

    public String getC_carrierIdentifier() {
        return c_carrierIdentifier;
    }
}
