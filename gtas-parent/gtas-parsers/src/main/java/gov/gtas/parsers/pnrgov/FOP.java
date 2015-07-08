package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class FOP to hold Form of Payment details for a ticket
 * @author GTAS4
 * 
 * If payment is via credit card, then the provision of the cardholder name is via the IFT if different 
 * from the passenger.
 * 
 * Ex:Paid with an American Express card, with an expiration date of 12/11(FOP+CC::416.00:AX:373212341234123:1211’)
 * Form of payment is cash.(FOP+CA::731.00')
 * Form of payment is Government receipt(FOP+GR::200.00::AB123456')
 * Old form of payment was VISA card with an expiration date of August, 2013(FOP+CC:2:628.32:VI:4235792300387826:0813’)
 */
public class FOP extends Segment{
	
	private String paymentType;
	private String dataIndicator;//To indicate old, new or original form of payment
	private String paymentAmount;
	private String vendorCode;
	private String accountNumber;
	private String expiryDate;
	

	public FOP(String name, Composite[] composites) {
		super(name, composites);
		
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
