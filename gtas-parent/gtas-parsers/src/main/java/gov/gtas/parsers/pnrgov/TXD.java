package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
/**
 * Class TXD holds Tax details
 * @author GTAS4
 *
 *The tax code and country code should be in data elements 5153 and 3207 respectively.
 *
 *Examples:
 *Tax details for departure taxes for Great Britain.(TXD++5:GB::9')
 *Tax information related to the given fare.(TXD++6.27::USD')
 */
public class TXD extends Segment{

	private String specialTaxIndicator;
	private String taxAmount;
	private String countryIsoCode;
	private String currencyIsoCode;
	private String taxCode;
	private String conversionRate;
	private String taxQualifier;
	
	
	public TXD(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

}
