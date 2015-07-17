package gov.gtas.parsers.paxlst.segment.unedifact;

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
    private boolean isMasterCrewList;
    
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
                this.isMasterCrewList = c.getValue().endsWith("MCL");
                this.c_journeyIdentifier = c.getValue().replace("MCL", "");
                break;
            case 4:
                this.c_carrierIdentifier = c.getValue();
                break;
            }
        }
        
        if (this.c_carrierIdentifier != null) {
            this.flightNumber = this.c_journeyIdentifier.replace(this.c_carrierIdentifier, "");
        } else {
            StringBuffer fn = new StringBuffer();
            int j;
            for (j=this.c_journeyIdentifier.length() - 1; j>=0; j--) {
                char c = this.c_journeyIdentifier.charAt(j);
                if (Character.isDigit(c)) {
                    fn.append(c);
                    if (fn.length() == 4) {
                        break;
                    }
                } else {
                    break;
                }
            }
            this.flightNumber = fn.reverse().toString();
            this.c_carrierIdentifier = this.c_journeyIdentifier.substring(0, j+1); 
        }
        
//        System.out.println("MAC: " + c_carrierIdentifier + " " +  flightNumber);
    }

    public String getFlightNumber() {
        return flightNumber;
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

    public boolean isMasterCrewList() {
        return isMasterCrewList;
    }
}
