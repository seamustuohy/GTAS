package gov.cbp.taspd.gtas.parsers.unedifact.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class UNG extends Segment {
    private String messageGroupIdentification;
    private String senderIdentification;
    private String recipientIdentification;
    private String date;
    private String time;
    private String groupReferenceNumber;
    private String controllingAgency;
    private String messageVersionNumber;
    private String messageReleaseNumber;
    
    public UNG(Composite[] composites) {
        super(UNG.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.messageGroupIdentification = c.getValue();
                break;
            case 1:
                this.senderIdentification = c.getValue();
                break;
            case 2:
                this.recipientIdentification = c.getValue();
                break;
            case 3:
                this.date = e[0].getValue();
                this.time = e[1].getValue();
                break;
            case 4:
                this.groupReferenceNumber = c.getValue();
                break;
            case 5:
                this.controllingAgency = c.getValue();
                break;
            case 6:
                this.messageVersionNumber = e[0].getValue();
                this.messageReleaseNumber = e[1].getValue();
                break;        
            }
        }
    }

    public String getMessageGroupIdentification() {
        return messageGroupIdentification;
    }

    public String getSenderIdentification() {
        return senderIdentification;
    }

    public String getRecipientIdentification() {
        return recipientIdentification;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getGroupReferenceNumber() {
        return groupReferenceNumber;
    }

    public String getControllingAgency() {
        return controllingAgency;
    }

    public String getMessageVersionNumber() {
        return messageVersionNumber;
    }

    public String getMessageReleaseNumber() {
        return messageReleaseNumber;
    }
}
