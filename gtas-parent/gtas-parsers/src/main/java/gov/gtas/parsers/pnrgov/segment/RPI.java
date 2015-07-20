package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class RPI to hold Related product information
 * To indicate quantity and action required in relation to a product.
 * @author GTAS4
 *
 *Example:Flight booking status is holds confirmed for 3 passengers(RPI+3+HK')
 */
public class RPI extends Segment{

	private String numberOfPassengersToTVL;
	private String statusCode;
	
	public RPI(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	public String getNumberOfPassengersToTVL() {
		return numberOfPassengersToTVL;
	}

	public void setNumberOfPassengersToTVL(String numberOfPassengersToTVL) {
		this.numberOfPassengersToTVL = numberOfPassengersToTVL;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
