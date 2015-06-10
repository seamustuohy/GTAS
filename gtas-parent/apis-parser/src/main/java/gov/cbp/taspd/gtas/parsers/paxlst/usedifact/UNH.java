package gov.cbp.taspd.gtas.parsers.paxlst.usedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Element;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class UNH extends Segment {
    private String messageReferenceNumber;
    private String messageType;
    private String versionNumber;
    private String releaseNumber;
    private String controllingAgency;
    private String commonAccessReference;
    
    public UNH(Composite[] composites) {
        super(UNH.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
            }
        }
    }
}