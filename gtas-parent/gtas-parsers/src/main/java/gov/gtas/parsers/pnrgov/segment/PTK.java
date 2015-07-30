package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class PTK to hold Pricing/ticketing details for a passenger. Examples The
 * pricing/ticketing details: the ticket is non-refundable, the ticketing
 * deadline date and time are 10 pm on 6/15/10, the validating carrier is DL and
 * the sales/ticketing location city code is ATL.
 * (PTK+NR++150610:2200+DL+006+ATL')
 * 
 * 
 *
 */

public class PTK extends Segment {

    private String ticketMode;
    private String salesIndicator;// domestic or international
    private String statisticalIndicator;
    private String selfSaleIndicator;
    private String netReportingIndicator;
    private String taxCommissionIndicator;
    private String endorsableIndicator;
    private String nonRefundableIndicator;
    private String penaltyRestrictionIndicator;
    private String nonInterLineableIndicator;
    private String nonCommissionableIndicator;
    private String nonExchangeableIndicator;
    private String carrierFeeIndicator;
    private String refundCalculationIndicator;
    private String purchaseDeadLineDate;// ddmmyy
    private String purchaseDeadLineTime;// hhmm
    private String carrierAirlineCode;
    private String ticketingSystemCode;
    private String carrierAccountingCode;
    private String providerAccountingCode;
    private String ticketLocationCountryCode;

    public PTK(List<Composite> composites) {
        super(PTK.class.getSimpleName(), composites);
    }
}
