package gov.gtas.parsers.pnrgov.segment;

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
	public SAC(Composite[] composites) {
		super(SAC.class.getSimpleName(), composites);
	}
}
