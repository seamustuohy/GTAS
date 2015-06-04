package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class LOC extends Segment {
    private String functionCode;
    private String locationCode;
    
    public LOC(Composite[] composites) {
        super(LOC.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.functionCode = c.getValue();
                break;
            case 1:
                this.locationCode = c.getValue();
                break;
            }
        }
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public String getLocationCode() {
        return locationCode;
    }
}
