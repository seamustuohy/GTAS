package gov.cbp.taspd.gtas.parsers.paxlst.usedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Element;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class DTM extends Segment {

    public DTM(Composite[] composites) {
        super(DTM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
            }
        }
    }
}