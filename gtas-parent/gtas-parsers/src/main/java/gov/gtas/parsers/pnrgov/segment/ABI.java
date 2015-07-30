package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class ABI to hold Additional business source information
 * ex:The creator of the history credit is a DL agent in Atlanta.
 * (ABI+4+05FD28:GS+ATL++DL’)
 * @author GTAS4
 *
 */
public class ABI extends Segment{

	//private String sourceType;
	private String sourceIdentificationQualifier;
	private String agentIdentificationDetails;
	private String inhouseIdentification;
	private String agentLocation;
	private String airlineCrsCode;
	
	public ABI(List<Composite> composites) {
		super(ABI.class.getSimpleName(), composites);
		
	}
}
