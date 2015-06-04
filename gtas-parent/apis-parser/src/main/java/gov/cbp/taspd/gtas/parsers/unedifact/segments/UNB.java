package gov.cbp.taspd.gtas.parsers.unedifact.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class UNB extends Segment {
    private String syntaxIdentifier;
    private String syntaxVersion;
    
    public UNB(Composite[] composites) {
        super("UNB", composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.syntaxIdentifier = e[0].getValue();
                this.syntaxVersion = e[1].getValue();
                break;
            default:
                // unknown 
            }
        }
    }
    
    public String getSyntaxIdentifier() {
        return syntaxIdentifier;
    }
    public void setSyntaxIdentifier(String syntaxIdentifier) {
        this.syntaxIdentifier = syntaxIdentifier;
    }
    public String getSyntaxVersion() {
        return syntaxVersion;
    }
    public void setSyntaxVersion(String syntaxVersion) {
        this.syntaxVersion = syntaxVersion;
    }

}
