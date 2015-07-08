package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class SAC to hold Source And Action Information
 * @author GTAS4
 * Used in conjunction with other segments where the item was actioned. Eg Name Change, flight etc
 * Flown segments are to be included in history.
 * Examples: The history line contains a cancelled item(SAC+++X')
 * The history line contains an added item(SAC+++A’)
 *
 */
public class SAC extends Segment{

	private String statusIndicator;
	private String locationIndicator;
	private String statusCode;
	public SAC(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}
	public String getStatusIndicator() {
		return statusIndicator;
	}
	public void setStatusIndicator(String statusIndicator) {
		this.statusIndicator = statusIndicator;
	}
	public String getLocationIndicator() {
		return locationIndicator;
	}
	public void setLocationIndicator(String locationIndicator) {
		this.locationIndicator = locationIndicator;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
