package gov.cbp.taspd.gtas.parsers.paxlst.usedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Element;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.util.ParseUtils;

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

    public void setSyntaxIdentifier(String syntaxIdentifier) {
        this.syntaxIdentifier = syntaxIdentifier;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderQualifier() {
        return senderQualifier;
    }

    public void setSenderQualifier(String senderQualifier) {
        this.senderQualifier = senderQualifier;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientQualifier() {
        return recipientQualifier;
    }

    public void setRecipientQualifier(String recipientQualifier) {
        this.recipientQualifier = recipientQualifier;
    }

    public Date getDateAndTimeOfPreparation() {
        return dateAndTimeOfPreparation;
    }

    public void setDateAndTimeOfPreparation(Date dateAndTimeOfPreparation) {
        this.dateAndTimeOfPreparation = dateAndTimeOfPreparation;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public void setInterchangeControlReference(String interchangeControlReference) {
        this.interchangeControlReference = interchangeControlReference;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public void setApplicationReference(String applicationReference) {
        this.applicationReference = applicationReference;
    }

    public String getC_priorityCode() {
        return c_priorityCode;
    }

    public void setC_priorityCode(String c_priorityCode) {
        this.c_priorityCode = c_priorityCode;
    }

    public String getTestIndicator() {
        return testIndicator;
    }

    public void setTestIndicator(String testIndicator) {
        this.testIndicator = testIndicator;
    }
}
