package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class RFF extends Segment {
    private String referenceCodeQualifier;
    private String referenceIdentifier;
    private String revisionIdentifier;
    
    public RFF(Composite[] composites) {
        super(RFF.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.referenceCodeQualifier = e[0].getValue();
                this.referenceIdentifier = e[1].getValue();
                if (e.length >= 5) {
                    this.revisionIdentifier = e[4].getValue();
                }
            }
        }
    }

    public String getReferenceCodeQualifier() {
        return referenceCodeQualifier;
    }

    public String getReferenceIdentifier() {
        return referenceIdentifier;
    }

    public String getRevisionIdentifier() {
        return revisionIdentifier;
    }
}
