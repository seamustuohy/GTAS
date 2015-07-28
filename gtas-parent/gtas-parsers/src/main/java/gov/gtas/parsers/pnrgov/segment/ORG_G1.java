package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * ORG: ORIGINATOR OF REQUEST DETAILS
 * <p>
 * The ORG in GR.1 at level 2 is the originator of the booking. For “update”
 * pushes when the push flight/date is cancelled from a PNR or the complete PNR
 * is cancelled or not found, the ORG is sent as an empty segment,i.e., does not
 * contain data.
 * <p>
 * Examples:
 * <ul>
 * <li>The originator of the booking is an LH agent located in Amsterdam hosted
 * on Amadeus.(ORG+1A:MUC+12345678:111111+AMS+LH+A+NL:NLG:NL+0001AASU’)
 * <li>The originator of the booking is an Amadeus travel agent
 * request.(ORG+1A:NCE+12345678:DDGS+++T')
 * </ul>
 */
public class ORG_G1 extends Segment {

    private String senderCode;
    private String locationCode;
                                
    private String travelAgentIdentifier;
    private String reservationSystemCode;
    private String reservationSystemKey;
    private String agentLocationCode;
    private String companyIdentification;
    private String locationIdentification;
    private String originatorTypeCode;
    private String originatorCountryCode;
    private String originatorCurrencyCode;
    private String originatorLanguageCode;

    public ORG_G1(Composite[] composites) {
        super("ORG", composites);

    }
}
