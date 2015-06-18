package gov.gtas.parsers.paxlst.usedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.util.ParseUtils;

import java.util.Date;

public class UNB extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";
    
    private String syntaxIdentifier;
    private String version;
    private String sender;
    private String senderQualifier;
    private String recipient;
    private String recipientQualifier;
    private Date dateAndTimeOfPreparation;
    private String interchangeControlReference;
    private String applicationReference;
    private String c_priorityCode;
    private String testIndicator;
    
    public UNB(Composite[] composites) {
        super(UNB.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.syntaxIdentifier = e[0].getValue();
                this.version = e[1].getValue();
            case 1:
                this.sender = e[0].getValue();
                this.senderQualifier = e[1].getValue();
                break;
            case 2:
                this.recipient = e[0].getValue();
                this.recipientQualifier = e[1].getValue();
                break;
            case 3:
                String tmp = e[0].getValue() + e[1].getValue();
                this.dateAndTimeOfPreparation = ParseUtils.parseDateTime(tmp, DATE_TIME_FORMAT);
                break;
            case 4:
                this.interchangeControlReference = c.getValue();
                break;
            case 6:
                this.applicationReference = c.getValue();
                break;
            case 7:
                this.c_priorityCode = c.getValue();
                break;
            case 10:
                this.testIndicator = c.getValue();
                break;
            }
        }
    }

    public String getSyntaxIdentifier() {
        return syntaxIdentifier;
    }

    public String getVersion() {
        return version;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderQualifier() {
        return senderQualifier;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getRecipientQualifier() {
        return recipientQualifier;
    }

    public Date getDateAndTimeOfPreparation() {
        return dateAndTimeOfPreparation;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public String getC_priorityCode() {
        return c_priorityCode;
    }

    public String getTestIndicator() {
        return testIndicator;
    }
}