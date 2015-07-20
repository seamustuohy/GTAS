package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

import java.util.Date;

/**
 * <p>
 * UNG: FUNCTIONAL GROUP HEADER
 * <p>
 * Function: To head, identify and specify a Functional Group. The conditional
 * Status (C) of elements within this segment is used to indicate that Border
 * Control Agencies may establish bilateral requirements for these data
 * elements.
 */
public class UNG extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";

    private String messageGroupIdentification;
    private String senderIdentification;
    private String recipientIdentification;
    private Date dateAndTimeOfPreparation;
    private String groupReferenceNumber;
    private String controllingAgency;
    private String messageVersionNumber;
    private String messageReleaseNumber;

    public UNG(Composite[] composites) {
        super(UNG.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
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
                String tmp = e[0].getValue() + e[1].getValue();
                this.dateAndTimeOfPreparation = ParseUtils.parseDateTime(tmp, DATE_TIME_FORMAT);
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

    public Date getDateAndTimeOfPreparation() {
        return dateAndTimeOfPreparation;
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
