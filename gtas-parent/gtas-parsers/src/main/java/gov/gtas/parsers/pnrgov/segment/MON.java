package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class MON to hold Monetary information
 *
 * Ex .Ticket/document amount is $0.00 due to an award
 * certificate.(MON+T:AWARD') Ticket/document amount is 297.50
 * EUR.(MON+T:297.50:EUR’)
 */
public class MON extends Segment {

    private String ticketAmountType;
    private String ticketAmount;
    private String isoCurrencyCode;
    private String allowenceOrChargeNumber;
    private String placeIdentification;

    public MON(Composite[] composites) {
        super(MON.class.getSimpleName(), composites);
    }
}
