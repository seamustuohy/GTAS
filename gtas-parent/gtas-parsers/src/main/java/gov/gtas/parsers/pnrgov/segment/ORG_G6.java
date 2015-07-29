package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * ORG: ORIGINATOR OF REQUEST DETAILS
 * <p>
 * Specifies the sender of the message.(To specify the point of sale details.)
 * (Originator of request details)
 * <p>
 * The ORG in GR.6 at level4 is the agent id who checked in the passenger for
 * this flight segment.
 */
public class ORG_G6 extends Segment {

    private String senderCode;// 2-3 character airline/CRS code
    private String locationCode;// ATA/IATA airport/city code of delivering
                                // system
    private String travelAgentIdentifier;// ATA/IATA travel agency ID number
    private String reservationSystemCode;
    private String reservationSystemKey;
    private String agentLocationCode;
    private String companyIdentification;
    private String locationIdentification;
    private String originatorTypeCode;
    private String originatorCountryCode;
    private String originatorCurrencyCode;
    private String originatorLanguageCode;

    public ORG_G6(Composite[] composites) {
        super("ORG", composites);

    }
}
