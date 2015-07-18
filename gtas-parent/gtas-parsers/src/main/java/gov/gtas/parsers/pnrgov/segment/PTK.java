package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class PTK to hold Pricing/ticketing details for a passenger.
 * @author GTAS4
 * 
 * Examples
 * The pricing/ticketing details: the ticket is non-refundable, the ticketing deadline date and time 
 * are 10 pm on 6/15/10, the validating carrier is DL and the sales/ticketing location city code is ATL.
 * (PTK+NR++150610:2200+DL+006+ATL')
 * 
 * 
 *
 */

public class PTK extends Segment{

	private String ticketMode;
	private String salesIndicator;//domestic or international
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
	private String purchaseDeadLineDate;//ddmmyy
	private String purchaseDeadLineTime;//hhmm
	private String carrierAirlineCode;
	private String ticketingSystemCode;
	private String carrierAccountingCode;
	private String providerAccountingCode;
	private String ticketLocationCountryCode;
	
	
	public PTK(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}


	public String getTicketMode() {
		return ticketMode;
	}


	public void setTicketMode(String ticketMode) {
		this.ticketMode = ticketMode;
	}


	public String getSalesIndicator() {
		return salesIndicator;
	}


	public void setSalesIndicator(String salesIndicator) {
		this.salesIndicator = salesIndicator;
	}


	public String getStatisticalIndicator() {
		return statisticalIndicator;
	}


	public void setStatisticalIndicator(String statisticalIndicator) {
		this.statisticalIndicator = statisticalIndicator;
	}


	public String getSelfSaleIndicator() {
		return selfSaleIndicator;
	}


	public void setSelfSaleIndicator(String selfSaleIndicator) {
		this.selfSaleIndicator = selfSaleIndicator;
	}


	public String getNetReportingIndicator() {
		return netReportingIndicator;
	}


	public void setNetReportingIndicator(String netReportingIndicator) {
		this.netReportingIndicator = netReportingIndicator;
	}


	public String getTaxCommissionIndicator() {
		return taxCommissionIndicator;
	}


	public void setTaxCommissionIndicator(String taxCommissionIndicator) {
		this.taxCommissionIndicator = taxCommissionIndicator;
	}


	public String getEndorsableIndicator() {
		return endorsableIndicator;
	}


	public void setEndorsableIndicator(String endorsableIndicator) {
		this.endorsableIndicator = endorsableIndicator;
	}


	public String getNonRefundableIndicator() {
		return nonRefundableIndicator;
	}


	public void setNonRefundableIndicator(String nonRefundableIndicator) {
		this.nonRefundableIndicator = nonRefundableIndicator;
	}


	public String getPenaltyRestrictionIndicator() {
		return penaltyRestrictionIndicator;
	}


	public void setPenaltyRestrictionIndicator(String penaltyRestrictionIndicator) {
		this.penaltyRestrictionIndicator = penaltyRestrictionIndicator;
	}


	public String getNonInterLineableIndicator() {
		return nonInterLineableIndicator;
	}


	public void setNonInterLineableIndicator(String nonInterLineableIndicator) {
		this.nonInterLineableIndicator = nonInterLineableIndicator;
	}


	public String getNonCommissionableIndicator() {
		return nonCommissionableIndicator;
	}


	public void setNonCommissionableIndicator(String nonCommissionableIndicator) {
		this.nonCommissionableIndicator = nonCommissionableIndicator;
	}


	public String getNonExchangeableIndicator() {
		return nonExchangeableIndicator;
	}


	public void setNonExchangeableIndicator(String nonExchangeableIndicator) {
		this.nonExchangeableIndicator = nonExchangeableIndicator;
	}


	public String getCarrierFeeIndicator() {
		return carrierFeeIndicator;
	}


	public void setCarrierFeeIndicator(String carrierFeeIndicator) {
		this.carrierFeeIndicator = carrierFeeIndicator;
	}


	public String getRefundCalculationIndicator() {
		return refundCalculationIndicator;
	}


	public void setRefundCalculationIndicator(String refundCalculationIndicator) {
		this.refundCalculationIndicator = refundCalculationIndicator;
	}


	public String getPurchaseDeadLineDate() {
		return purchaseDeadLineDate;
	}


	public void setPurchaseDeadLineDate(String purchaseDeadLineDate) {
		this.purchaseDeadLineDate = purchaseDeadLineDate;
	}


	public String getPurchaseDeadLineTime() {
		return purchaseDeadLineTime;
	}


	public void setPurchaseDeadLineTime(String purchaseDeadLineTime) {
		this.purchaseDeadLineTime = purchaseDeadLineTime;
	}


	public String getCarrierAirlineCode() {
		return carrierAirlineCode;
	}


	public void setCarrierAirlineCode(String carrierAirlineCode) {
		this.carrierAirlineCode = carrierAirlineCode;
	}


	public String getTicketingSystemCode() {
		return ticketingSystemCode;
	}


	public void setTicketingSystemCode(String ticketingSystemCode) {
		this.ticketingSystemCode = ticketingSystemCode;
	}


	public String getCarrierAccountingCode() {
		return carrierAccountingCode;
	}


	public void setCarrierAccountingCode(String carrierAccountingCode) {
		this.carrierAccountingCode = carrierAccountingCode;
	}


	public String getProviderAccountingCode() {
		return providerAccountingCode;
	}


	public void setProviderAccountingCode(String providerAccountingCode) {
		this.providerAccountingCode = providerAccountingCode;
	}


	public String getTicketLocationCountryCode() {
		return ticketLocationCountryCode;
	}


	public void setTicketLocationCountryCode(String ticketLocationCountryCode) {
		this.ticketLocationCountryCode = ticketLocationCountryCode;
	}

}
