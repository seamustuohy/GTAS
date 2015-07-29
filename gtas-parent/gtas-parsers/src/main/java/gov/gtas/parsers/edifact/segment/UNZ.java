package gov.gtas.parsers.edifact.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * UNZ Interchange Trailer
 */
public class UNZ extends Segment {
    private String interchangeControlCount;
    private String interchangeControlReference;

    public UNZ(Composite[] composites) {
        super(UNZ.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.interchangeControlCount = c.getElement(0);
                break;
            case 1:
                this.interchangeControlReference = c.getElement(0);
                break;
            }
        }
    }

    public String getInterchangeControlCount() {
        return interchangeControlCount;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }
}
