package gov.gtas.parsers.paxlst.segment.unedifact;

import java.util.LinkedHashMap;
import java.util.Map;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * NAD: NAME AND ADDRESS
 * <p>
 * To specify a contact responsible for the message content. This may either be
 * an assigned profile or the name of the contact person.
 * <p>
 * Example: NAD+MS+ABC9876'
 */
public class NAD extends Segment {
    public enum NadCode {
        REPORTING_PARTY("MS"),
        PASSENGER("FL"),
        CREW_MEMBER("FM"),
        INTRANSIT_PASSENGER("DDU"),
        INTRANSIT_CREW_MEMBER("DDT");
        
        private final String code;
        private NadCode(String code) { this.code = code; }        
        public String getCode() { return code; }
        
        private static final Map<String, NadCode> BY_CODE_MAP = new LinkedHashMap<>();
        static {
            for (NadCode rae : NadCode.values()) {
                BY_CODE_MAP.put(rae.code, rae);
            }
        }

        public static NadCode forCode(String code) {
            return BY_CODE_MAP.get(code);
        }        
    }
    
    private NadCode nadCode;
    
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
                this.nadCode = NadCode.forCode(c.getValue());
                this.isReportingParty = (this.nadCode == NadCode.REPORTING_PARTY);
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

    public NadCode getNadCode() {
        return nadCode;
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