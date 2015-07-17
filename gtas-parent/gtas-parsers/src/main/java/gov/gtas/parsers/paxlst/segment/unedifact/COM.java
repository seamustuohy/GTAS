package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class COM extends Segment {
    private String phoneNumber;
    private String faxNumber;
    
    public COM(Composite[] composites) {
        super(COM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            if (e != null && e.length == 2) {
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
