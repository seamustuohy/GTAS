package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.util.ParseUtils;

/**
 * <p>
 * FOP: FORM OF PAYMENT
 * <p>
 * Class FOP to hold Form of Payment details for a ticket
 * <p>
 * If payment is via credit card, then the provision of the cardholder name is
 * via the IFT if different from the passenger.
 * <p>
 * Ex:
 * <ul>
 * <li>Paid with an American Express card, with an expiration date of
 * 12/11(FOP+CC::416.00:AX:373212341234123:1211’)
 * <li>Form of payment is cash.(FOP+CA::731.00')
 * <li>Form of payment is Government receipt(FOP+GR::200.00::AB123456')
 * <li>Old form of payment was VISA card with an expiration date of August,
 * 2013(FOP+CC:2:628.32:VI:4235792300387826:0813’)
 * </ul>
 */
public class FOP extends Segment {
    private String paymentType;
    private String paymentAmount;
    private String vendorCode;
    private String accountNumber;
    private Date expirationDate;

    public FOP(List<Composite> composites) throws ParseException {
        super(FOP.class.getSimpleName(), composites);
        Composite c = getComposite(0);
        this.paymentType = c.getElement(0);
        this.paymentAmount = c.getElement(2);
        this.vendorCode = c.getElement(3);
        this.accountNumber = c.getElement(4);
        String d = c.getElement(5);
        if (d != null) {
            this.expirationDate = ParseUtils.parseDateTime(d, "mmyy"); 
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
