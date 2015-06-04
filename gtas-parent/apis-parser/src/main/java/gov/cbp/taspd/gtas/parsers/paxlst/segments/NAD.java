package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class NAD extends Segment {
    private String partyFunctionCodeQualifier;
    
    /** only used if isReportingParty == true */
    private String partyName;
    
    private String lastName;
    private String firstName;
    private String middleName;
    private String numberAndStreetIdentifier;
    private String city;
    private String countrySubCode;
    private String postalCode;
    private String countryCode;
    
    /** to distinguish the two kinds of NAD segments */
    private boolean isReportingParty;
    
    public NAD(Composite[] composites) {
        super(NAD.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            
            switch (i) {
            case 0:
                this.partyFunctionCodeQualifier = c.getValue();
                this.isReportingParty = c.getValue().equals("MS");
                break;
            case 3:
                if (this.isReportingParty) {
                    this.partyName = c.getValue();
                } else {
                    if (e.length > 0) {
                        this.lastName = e[0].getValue();
                    }
                    if (e.length > 1) {
                        this.firstName = e[1].getValue();
                    }
                    if (e.length > 2) {
                        this.middleName = e[2].getValue();
                    }
                }
                break;
            case 4:
                this.numberAndStreetIdentifier = c.getValue();
                break;
            case 5:
                this.city = c.getValue();
                break;
            case 6:
                this.countrySubCode = c.getValue();
                break;
            case 7:
                this.postalCode = c.getValue();
                break;
            case 8:
                this.countryCode = c.getValue();
                break;
            }
        }
    }

    public String getPartyFunctionCodeQualifier() {
        return partyFunctionCodeQualifier;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNumberAndStreetIdentifier() {
        return numberAndStreetIdentifier;
    }

    public String getCity() {
        return city;
    }

    public String getCountrySubCode() {
        return countrySubCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public boolean isReportingParty() {
        return isReportingParty;
    }
}
