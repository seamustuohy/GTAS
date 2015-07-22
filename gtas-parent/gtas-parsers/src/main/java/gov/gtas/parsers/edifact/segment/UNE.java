package gov.gtas.parsers.edifact.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * UNE Functional Group Trailer
 */
public class UNE extends Segment {

    private int numberOfMessages;
    private String identificationNumber;

    public UNE(Composite[] composites) {
        super(UNE.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.numberOfMessages = Integer.valueOf(c.getValue());
                break;
            case 1:
                this.identificationNumber = c.getValue();
                break;
            }
        }
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }
}
