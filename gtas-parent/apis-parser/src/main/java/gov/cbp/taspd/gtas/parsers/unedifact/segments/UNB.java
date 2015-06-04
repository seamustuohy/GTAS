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
    private String date;
    private String time;
    private String interchangeControlReference;
    private String applicationReference;
    
    public UNB(Composite[] composites) {
        super(UNB.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
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
                this.date = e[0].getValue();
                this.time = e[1].getValue();
                break;
            case 4:
                this.interchangeControlReference = c.getValue();
                break;
            case 5:
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public String getApplicationReference() {
        return applicationReference;
    }
}
