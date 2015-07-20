package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

public class BGM extends Segment {
    public enum DocCode {
        PASSENGER_LIST,
        GATE_PASS_REQUEST,
        FLIGHT_STATUS_UPDATE,
        CREW_LIST_DECLARATION,
        MASTER_CREW_LIST
    }
    
    private DocCode documentNameCode;
    private String c_documentIdentifier;
    
    public BGM(Composite[] composites) {
        super(BGM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                Integer tmp = Integer.valueOf(c.getValue());
                switch (tmp) {
                case 745:
                    this.documentNameCode = DocCode.PASSENGER_LIST;
                    break;
                case 655:
                    this.documentNameCode = DocCode.GATE_PASS_REQUEST;
                    break;
                case 266:
                    this.documentNameCode = DocCode.FLIGHT_STATUS_UPDATE;
                    break;
                case 250:
                    this.documentNameCode = DocCode.CREW_LIST_DECLARATION;
                    break;
                case 336:
                    this.documentNameCode = DocCode.MASTER_CREW_LIST;
                    break;
                default:
                    logger.error("BGM: unknown document code");
                }
                break;
                
            case 1:
                this.c_documentIdentifier = c.getValue();
                break;
            }
        }
    }

    public DocCode getDocumentNameCode() {
        return documentNameCode;
    }

    public String getC_documentIdentifier() {
        return c_documentIdentifier;
    }
}
