package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * ADD: Address Information
 * <p>
 * Address Information of a traveler in PNR
 * <p>
 * The ADD in GR.1 at level 2 may contain a contact address for the PNR.
 * <p>
 * The ADD in GR.2 at level 3 may contain emergency contact information and or/
 * UMNR delivery and collection addresses
 * <p>
 * The ADD in GR.4 at level 5 may contain the address of the payer of the
 * ticket.
 * <p>
 * If the address and/or telephone information cannot be broken down in separate
 * elements, the information may be found in OSIs and SSRs.
 * <p>
 * Ex:The contact address is 4532 Wilson Street, Philadelphia, zip code 34288
 * (ADD++700:4532 WILSON STREET:PHILADELPHIA:PA::US:34288’)
 */
public class ADD extends Segment {

    private String addressType;
    private String streetNumberAndName;
    private String city;
    private String stateOrProvinceCode;

    /** ISO 3166-1-alpha 2 code */
    private String countryCode;

    private String postalCode;

    private String telephone;

    public ADD(Composite[] composites) {
        super(ADD.class.getSimpleName(), composites);
        Element[] e = this.composites[1].getElements();
        if (e.length >= 1) {
            this.addressType = e[0].getValue();
        }
        if (e.length >= 2) {
            this.streetNumberAndName = e[1].getValue();
        }
        if (e.length >= 3) {
            this.city = e[2].getValue();
        }
        if (e.length >= 4) {
            this.stateOrProvinceCode = e[3].getValue();
        }
        if (e.length >= 5) {
            // Country sub-entity name
            // not recorded
        }
        if (e.length >= 6) {
            this.countryCode = e[5].getValue();
        }
        if (e.length >= 7) {
            this.postalCode = e[6].getValue();
        }
        if (e.length >= 8) {
            this.telephone = e[7].getValue();
        }
    }

    public String getAddressType() {
        return addressType;
    }

    public String getStreetNumberAndName() {
        return streetNumberAndName;
    }

    public String getCity() {
        return city;
    }

    public String getStateOrProvinceCode() {
        return stateOrProvinceCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTelephone() {
        return telephone;
    }
}
