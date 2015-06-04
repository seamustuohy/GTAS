package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class GEI extends Segment {
    private String codeQualifier;
    private String descriptionCode;
    
    public GEI(Composite[] composites) {
        super(GEI.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.codeQualifier = c.getValue();
                break;
            case 1:
                this.descriptionCode = c.getValue();
                break;
            }
        }
    }

    public String getCodeQualifier() {
        return codeQualifier;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }
}
