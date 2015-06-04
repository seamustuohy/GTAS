package gov.cbp.taspd.gtas.parsers.unedifact.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class UNG extends Segment {
    public UNG(Composite[] composites) {
        super(UNG.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
            }
        }
    }

}
