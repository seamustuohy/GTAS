package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class NAD extends Segment {
    public NAD(Composite[] composites) {
        super("NAD", composites);
    }
}
