package gov.cbp.taspd.gtas.parsers.unedifact.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class UNB extends Segment {
    private String syntaxIdentifier;
    private String syntaxVersion;
    
    private String senderIdentification;
    private String c_partnerIdentificationCodeQualifier;
    private String recipientIdentification;
    private String c_partnerIdentificationCodeQualifier2;
    private String dateOfPreparation;
    private String timeOfPreparation;
    private String interchangeControlReference;
    private String applicationReference;
    
    public UNB(Composite[] composites) {
        super(UNB.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                assert e.length == 2;
                this.syntaxIdentifier = e[0].getValue();
                this.syntaxVersion = e[1].getValue();
                break;
            case 1:
                assert e.length >= 1;
                this.senderIdentification = e[0].getValue();
                if (e.length > 1) {
                    this.c_partnerIdentificationCodeQualifier = e[1].getValue();
                }
            case 2:
                assert e.length >= 1;
                this.recipientIdentification = e[0].getValue();
                if (e.length > 1) {
                    this.c_partnerIdentificationCodeQualifier2 = e[1].getValue();
                }                
            case 3:
                assert e.length == 2;
                this.dateOfPreparation = e[0].getValue();
                this.timeOfPreparation = e[1].getValue();
            case 4:
                assert e.length == 1;
                this.interchangeControlReference = c.getValue();
            case 5:
                assert e.length == 1;
                this.applicationReference = c.getValue();
            default:
                // unknown 
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

    public String getDateOfPreparation() {
        return dateOfPreparation;
    }

    public String getTimeOfPreparation() {
        return timeOfPreparation;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    @Override
    public String toString() {
        return "UNB [syntaxIdentifier=" + syntaxIdentifier + ", syntaxVersion="
                + syntaxVersion + ", senderIdentification="
                + senderIdentification
                + ", c_partnerIdentificationCodeQualifier="
                + c_partnerIdentificationCodeQualifier
                + ", recipientIdentification=" + recipientIdentification
                + ", c_partnerIdentificationCodeQualifier2="
                + c_partnerIdentificationCodeQualifier2
                + ", dateOfPreparation=" + dateOfPreparation
                + ", timeOfPreparation=" + timeOfPreparation
                + ", interchangeControlReference="
                + interchangeControlReference + ", applicationReference="
                + applicationReference + "]";
    }
}
