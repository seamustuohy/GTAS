package gov.cbp.taspd.gtas.parsers.paxlst.unedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Element;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class COM extends Segment {
    private String phoneNumber;
    private String faxNumber;
    
    public COM(Composite[] composites) {
        super(COM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            if (e.length == 2) {
                String type = e[1].getValue();
                switch (type) {
                case "TE":
                    this.phoneNumber = e[0].getValue();
                    break;
                case "FX":
                    this.faxNumber = e[0].getValue();
                    break;
                }
            }
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }
}
