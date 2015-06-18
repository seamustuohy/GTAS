package gov.gtas.parsers.paxlst.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class NAD extends Segment {
    public enum PartyCode {
        REPORTING_PARTY,
        PASSENGER,
        CREW_MEMBER,
        INTRANSIT_PASSENGER,
        INTRANSIT_CREW_MEMBER,
        INVOLVED_PARTY_GATE_PASS_REQUEST,
        CANCEL_RESERVATION_OR_FLIGHT_CLOSE_OUT
    }
    
    private PartyCode partyFunctionCodeQualifier;
    
    /** only used if isReportingParty == true */
    private String partyName;
    
    /** following only used if isReportingParty == false */
    private String lastName;
    private String firstName;
    private String middleName;
    private String numberAndStreetIdentifier;
    private String city;
    private String countrySubCode;
    private String postalCode;
    private String countryCode;
    
    /** to distinguish the two major kinds of NAD segments */
    private boolean isReportingParty = false;
    
    public NAD(Composite[] composites) {
        super(NAD.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            
            switch (i) {
            case 0:
                switch (c.getValue()) {
                case "MS":
                    this.isReportingParty = true;
                    this.partyFunctionCodeQualifier = PartyCode.REPORTING_PARTY;
                    break;
                case "FL":
                    this.partyFunctionCodeQualifier = PartyCode.PASSENGER;
                    break;
                case "FM":
                    this.partyFunctionCodeQualifier = PartyCode.CREW_MEMBER;
                    break;
                case "DDU":
                    this.partyFunctionCodeQualifier = PartyCode.INTRANSIT_PASSENGER;
                    break;
                case "DDT":
                    this.partyFunctionCodeQualifier = PartyCode.INTRANSIT_CREW_MEMBER;
                    break;
                case "COT":
                    this.partyFunctionCodeQualifier = PartyCode.INVOLVED_PARTY_GATE_PASS_REQUEST;
                    break;
                case "ZZZ":
                    this.partyFunctionCodeQualifier = PartyCode.CANCEL_RESERVATION_OR_FLIGHT_CLOSE_OUT;
                    break;
                default:
                    System.err.println("NAD: invalid party function code");
                }
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

    public PartyCode getPartyFunctionCodeQualifier() {
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
