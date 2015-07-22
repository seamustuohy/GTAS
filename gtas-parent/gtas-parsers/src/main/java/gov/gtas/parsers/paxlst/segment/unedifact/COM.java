package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * COM: COMMUNICATION CONTACT
 * <p>
 * Function: To specify the communication number(s) of the person responsible
 * for the message content. Up to 3 communication numbers can be provided.
 * <p>
 * Example: COM+202 628 9292:TE+202 628 4998:FX+davidsonr.at.iata.org:EM’
 */
public class COM extends Segment {
    private String phoneNumber;
    private String faxNumber;
    private String email;

    public COM(Composite[] composites) {
        super(COM.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
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
                case "EM":
                    this.email = e[0].getValue();
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

    public String getEmail() {
        return email;
    }
}
