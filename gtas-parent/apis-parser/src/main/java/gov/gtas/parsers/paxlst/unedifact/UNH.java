package gov.gtas.parsers.paxlst.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class UNH extends Segment {
    public enum TransferIndicator { 
        CONTINUANCE,
        FINAL 
    }
    
    private String messageReferenceNumber;
    private String messageType;
    private String messageTypeVersion;
    private String messageTypeReleaseNumber;
    private String controllingAgency;
    private String c_associationAssignedCode;
    private String c_commonAccessReference;
    private String c_sequenceMessageTransferNumber;
    private TransferIndicator c_transferIndicator;
    
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
                this.messageTypeVersion = e[1].getValue();
                this.messageTypeReleaseNumber = e[2].getValue();
                this.controllingAgency = e[3].getValue();
                if (e.length > 4) {
                    this.c_associationAssignedCode = e[4].getValue();
                }
                break;
            case 2:
                this.c_commonAccessReference = c.getValue();
                break;
            case 3:
                if (e.length > 0) {
                    this.c_sequenceMessageTransferNumber = e[0].getValue();
                }
                if (e.length > 1) {
                    /*
                     * A value of 'C' indicates this transmission is a continuance of previously
                     * transmitted data for a particular flight. A value of 'F' must be used to
                     * indicate a FINAL transmission of passenger/crew data reporting.
                     */
                    String tmp = e[1].getValue();
                    if (tmp.equals("C")) {
                        this.c_transferIndicator = TransferIndicator.CONTINUANCE;
                    } else if (tmp.equals("F")) {
                        this.c_transferIndicator = TransferIndicator.FINAL;
                    } else {
                        System.err.println("UNH: invalid transfer indicator");
                    }
                }
                break;
            }
        }
    }

    public String getMessageReferenceNumber() {
        return messageReferenceNumber;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageTypeVersion() {
        return messageTypeVersion;
    }

    public String getMessageTypeReleaseNumber() {
        return messageTypeReleaseNumber;
    }

    public String getControllingAgency() {
        return controllingAgency;
    }

    public String getC_associationAssignedCode() {
        return c_associationAssignedCode;
    }

    public String getC_commonAccessReference() {
        return c_commonAccessReference;
    }

    public String getC_sequenceMessageTransferNumber() {
        return c_sequenceMessageTransferNumber;
    }

    public TransferIndicator getC_transferIndicator() {
        return c_transferIndicator;
    }   
}
