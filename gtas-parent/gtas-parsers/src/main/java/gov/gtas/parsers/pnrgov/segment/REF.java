package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class REF to hold Reference information
 * @author GTAS4
 *
 *Example:The unique passenger reference identifier is 4928506894.
 *(REF+:4928506894')
 */
public class REF extends Segment{

	private String referenceNumber;
	
	public REF(Composite[] composites) {
		super(REF.class.getSimpleName(), composites);
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

}
