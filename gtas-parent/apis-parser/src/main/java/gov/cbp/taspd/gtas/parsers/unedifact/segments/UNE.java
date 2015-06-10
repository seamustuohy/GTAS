package gov.cbp.taspd.gtas.parsers.unedifact.segments;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Element;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class UNE extends Segment {
    public UNE(Composite[] composites) {
        super(UNE.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
            }
        }
    }
}
