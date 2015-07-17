package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
/**
 * To end and check the completeness of an Interchange
 * ex:UNZ+1+000000001’
 * @author GTAS4
 *
 */
public class UNZ extends Segment{
	private String interchangeControlCount;
	private String interchangeControlReference;

	public UNZ(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

}
