package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * FOP: FORM OF PAYMENT
 * 
 * <p>
 * Class FOP to hold Form of Payment details for a ticket
 * 
 * If payment is via credit card, then the provision of the cardholder name is
 * via the IFT if different from the passenger.
 * 
 * Ex:Paid with an American Express card, with an expiration date of
 * 12/11(FOP+CC::416.00:AX:373212341234123:1211’) Form of payment is
 * cash.(FOP+CA::731.00') Form of payment is Government
 * receipt(FOP+GR::200.00::AB123456') Old form of payment was VISA card with an
 * expiration date of August, 2013(FOP+CC:2:628.32:VI:4235792300387826:0813’)
 */
public class FOP extends Segment {

    private String paymentType;
    private String dataIndicator;
    private String paymentAmount;
    private String vendorCode;
    private String accountNumber;
    private String expiryDate;

    public FOP(Composite[] composites) {
        super(FOP.class.getSimpleName(), composites);
        Element[] e = this.composites[0].getElements();
        this.paymentType = e[0].getValue();
        if (e.length >= 2) {
            
        }
        if (e.length >= 3) {
            this.paymentAmount = e[2].getValue();
        }
        if (e.length >= 4) {
            this.vendorCode = e[3].getValue();
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDataIndicator() {
        return dataIndicator;
    }

    public void setDataIndicator(String dataIndicator) {
        this.dataIndicator = dataIndicator;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}
