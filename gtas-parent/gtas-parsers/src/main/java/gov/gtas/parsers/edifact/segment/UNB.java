package gov.gtas.parsers.edifact.segment;

import java.util.Date;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

/**
 * <p>
 * UNB: INTERCHANGE HEADER
 * <p>
 * Function: To start, identify and specify an interchange.  Specifies
 * the sender and intended recipient of the message.
 * 
 * <p>
 * Example: UNB+UNOA:4+APIS*ABE+USADHS+070429:0900+000000001++USADHS'
 */
public class UNB extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";

    private String syntaxIdentifier;
    private String syntaxVersion;
    private String sender;
    private String recipient;
    private Date dateAndTimeOfPreparation;
    private String interchangeControlReference;
    private String applicationReference;

    public UNB(Composite[] composites) {
        super(UNB.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.syntaxIdentifier = e[0].getValue();
                this.syntaxVersion = e[1].getValue();
                break;
            case 1:
                if (c.getValue() != null) {
                    this.sender = c.getValue();
                } else if (e != null && e.length >= 1) {
                    this.sender = e[0].getValue();
                }
                break;
            case 2:
                if (c.getValue() != null) {
                    this.recipient = c.getValue();
                } else if (e != null && e.length >= 1) {
                    this.recipient = e[0].getValue();
                }
                break;
            case 3:
                String tmp = e[0].getValue() + e[1].getValue();
                this.dateAndTimeOfPreparation = ParseUtils.parseDateTime(tmp, DATE_TIME_FORMAT);
                break;
            case 4:
                this.interchangeControlReference = c.getValue();
                break;
            case 5:
                // blank
                break;
            case 6:
                this.applicationReference = c.getValue();
                break;
            }
        }
    }

    public String getSyntaxIdentifier() {
        return syntaxIdentifier;
    }

    public String getSyntaxVersion() {
        return syntaxVersion;
    }

    public String getSenderIdentification() {
        return sender;
    }

    public String getRecipientIdentification() {
        return recipient;
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
}
