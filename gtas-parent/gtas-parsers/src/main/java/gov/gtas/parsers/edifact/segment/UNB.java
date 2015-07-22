package gov.gtas.parsers.edifact.segment;

import java.text.ParseException;
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
 * <p>
 * Example: UNB+UNOA:4+AIRLINE1+NZCS+130628:0900+000000001’
 */
public class UNB extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";

    private String sender;
    private String recipient;
    private Date dateAndTimeOfPreparation;

    public UNB(Composite[] composites) throws ParseException {
        super(UNB.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                if (e == null || e.length < 2) {
                    throw new ParseException("UNB: missing syntax identifier and version", -1);
                }
                if (e[0].getValue().equals("UNOA")) {
                    // ok
                } else {
                    throw new ParseException("UNB: syntax identifier is not UNOA", -1);
                }
                
                if (e[1].getValue().equals("4")) {
                    // ok
                } else {
                    throw new ParseException("UNB: syntax identifier is not UNOA", -1);
                }
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
            }
        }
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
}
