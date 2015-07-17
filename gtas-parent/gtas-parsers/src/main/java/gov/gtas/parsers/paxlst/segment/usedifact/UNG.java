package gov.gtas.parsers.paxlst.segment.usedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

import java.util.Date;

public class UNG extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";

    private String functionalGroupIdentifier;
    private String applicationSenderId;
    private String senderQualifier;
    private String applicationReceiverId;
    private String receiverQualifier;
    private Date dateTimeAssembled;
    private String groupReferenceNumber;
    private String controllingAgencyCode;
    private String versionNumber;
    private String releaseNumber;
    
    public UNG(Composite[] composites) {
        
        super(UNG.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.functionalGroupIdentifier = c.getValue();
                break;
            case 1:
                this.applicationSenderId = e[0].getValue();
                this.senderQualifier = e[1].getValue();
                break;
            case 2:
                this.applicationReceiverId = e[0].getValue();
                this.receiverQualifier = e[1].getValue();
                break;
            case 3:
                String tmp = e[0].getValue() + e[1].getValue();
                this.dateTimeAssembled = ParseUtils.parseDateTime(tmp, DATE_TIME_FORMAT);
                break;
            case 4:
                this.groupReferenceNumber = c.getValue();
                break;
            case 5:
                this.controllingAgencyCode = c.getValue();
                break;
            case 6:
                this.versionNumber = e[0].getValue();
                this.releaseNumber = e[1].getValue();
                break;
            }
        }
    }

    public String getFunctionalGroupIdentifier() {
        return functionalGroupIdentifier;
    }

    public void setFunctionalGroupIdentifier(String functionalGroupIdentifier) {
        this.functionalGroupIdentifier = functionalGroupIdentifier;
    }

    public String getApplicationSenderId() {
        return applicationSenderId;
    }

    public void setApplicationSenderId(String applicationSenderId) {
        this.applicationSenderId = applicationSenderId;
    }

    public String getSenderQualifier() {
        return senderQualifier;
    }

    public void setSenderQualifier(String senderQualifier) {
        this.senderQualifier = senderQualifier;
    }

    public String getApplicationReceiverId() {
        return applicationReceiverId;
    }

    public void setApplicationReceiverId(String applicationReceiverId) {
        this.applicationReceiverId = applicationReceiverId;
    }

    public String getReceiverQualifier() {
        return receiverQualifier;
    }

    public void setReceiverQualifier(String receiverQualifier) {
        this.receiverQualifier = receiverQualifier;
    }

    public Date getDateTimeAssembled() {
        return dateTimeAssembled;
    }

    public void setDateTimeAssembled(Date dateTimeAssembled) {
        this.dateTimeAssembled = dateTimeAssembled;
    }

    public String getGroupReferenceNumber() {
        return groupReferenceNumber;
    }

    public void setGroupReferenceNumber(String groupReferenceNumber) {
        this.groupReferenceNumber = groupReferenceNumber;
    }

    public String getControllingAgencyCode() {
        return controllingAgencyCode;
    }

    public void setControllingAgencyCode(String controllingAgencyCode) {
        this.controllingAgencyCode = controllingAgencyCode;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }
}