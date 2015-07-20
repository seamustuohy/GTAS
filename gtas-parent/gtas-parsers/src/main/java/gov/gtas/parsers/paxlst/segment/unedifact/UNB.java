package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

import java.util.Date;

/**
 * <p>
 * UNB: INTERCHANGE HEADER
 * <p>
 * Function: To start, identify and specify an interchange.
 * <p>
 * The conditional Status (C) of elements within this segment is used to
 * indicate that Border Control Agencies may establish bilateral requirements
 * for these data elements.
 */
public class UNB extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";

    private String syntaxIdentifier;
    private String syntaxVersion;
    private String senderIdentification;
    private String c_partnerIdentificationCodeQualifier;
    private String recipientIdentification;
    private String c_partnerIdentificationCodeQualifier2;
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
                this.senderIdentification = e[0].getValue();
                if (e.length > 1) {
                    this.c_partnerIdentificationCodeQualifier = e[1].getValue();
                }
                break;
            case 2:
                this.recipientIdentification = e[0].getValue();
                if (e.length > 1) {
                    this.c_partnerIdentificationCodeQualifier2 = e[1].getValue();
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
        return senderIdentification;
    }

    public String getC_partnerIdentificationCodeQualifier() {
        return c_partnerIdentificationCodeQualifier;
    }

    public String getRecipientIdentification() {
        return recipientIdentification;
    }

    public String getC_partnerIdentificationCodeQualifier2() {
        return c_partnerIdentificationCodeQualifier2;
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
