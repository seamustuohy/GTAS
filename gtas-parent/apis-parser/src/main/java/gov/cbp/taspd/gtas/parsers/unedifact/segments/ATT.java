package gov.cbp.taspd.gtas.parsers.unedifact.segments;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class ATT extends Segment {    
    private String functionCode;
    private String attributeDescriptionCode;
    
    public ATT(Composite[] composites) {
        super(ATT.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.functionCode = c.getValue();
                break;
            case 2:
                this.attributeDescriptionCode = c.getValue();
                break;
            }
        }
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public String getAttributeDescriptionCode() {
        return attributeDescriptionCode;
    }
}
