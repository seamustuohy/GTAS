package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class BGM extends Segment {
    private String documentNameCode;
    private String c_documentIdentifier;
    
    public BGM(Composite[] composites) {
        super(BGM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.documentNameCode = c.getValue();
                break;
            case 1:
                this.c_documentIdentifier = c.getValue();
                break;
            }
        }
    }

    public String getDocumentNameCode() {
        return documentNameCode;
    }

    public String getC_documentIdentifier() {
        return c_documentIdentifier;
    }
}
