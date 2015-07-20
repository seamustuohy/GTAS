package gov.gtas.parsers.paxlst.segment.usedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class UNH extends Segment {
    private String messageReferenceNumber;
    private String messageType;
    private String versionNumber;
    private String c_releaseNumber;
    private String c_controllingAgency;
    private String commonAccessReference;
    
    public UNH(Composite[] composites) {
        super(UNH.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.messageReferenceNumber = c.getValue();
                break;
            case 1:
                this.messageType = e[0].getValue();
                this.versionNumber = e[1].getValue();
                
                if (e.length >= 3) {
                    this.c_releaseNumber = e[2].getValue();
                }
                if (e.length >= 4) {
                    this.c_controllingAgency = e[3].getValue();
                }
            }
        }
    }

    public String getMessageReferenceNumber() {
        return messageReferenceNumber;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getC_releaseNumber() {
        return c_releaseNumber;
    }

    public String getC_controllingAgency() {
        return c_controllingAgency;
    }

    public String getCommonAccessReference() {
        return commonAccessReference;
    }
}