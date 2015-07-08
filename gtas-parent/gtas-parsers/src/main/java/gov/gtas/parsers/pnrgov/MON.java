package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class MON to hold Monetary information
 * @author GTAS4
 *
 *Ex .Ticket/document amount is $0.00 due to an award certificate.(MON+T:AWARD')
 * Ticket/document amount is 297.50 EUR.(MON+T:297.50:EUR’)
 */
public class MON extends Segment{

	private String ticketAmountType;
	private String ticketAmount;
	private String isoCurrencyCode;
	private String allowenceOrChargeNumber;
	private String placeIdentification;
	
	public MON(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	
	public String getAllowenceOrChargeNumber() {
		return allowenceOrChargeNumber;
	}


	public void setAllowenceOrChargeNumber(String allowenceOrChargeNumber) {
		this.allowenceOrChargeNumber = allowenceOrChargeNumber;
	}


	public String getPlaceIdentification() {
		return placeIdentification;
	}


	public void setPlaceIdentification(String placeIdentification) {
		this.placeIdentification = placeIdentification;
	}


	public String getTicketAmountType() {
		return ticketAmountType;
	}

	public void setTicketAmountType(String ticketAmountType) {
		this.ticketAmountType = ticketAmountType;
	}

	public String getTicketAmount() {
		return ticketAmount;
	}

	public void setTicketAmount(String ticketAmount) {
		this.ticketAmount = ticketAmount;
	}

	public String getIsoCurrencyCode() {
		return isoCurrencyCode;
	}

	public void setIsoCurrencyCode(String isoCurrencyCode) {
		this.isoCurrencyCode = isoCurrencyCode;
	}

}
