package gov.gtas.parsers.pnrgov.segment;

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
	
	public ABI(String name, Composite[] composites) {
		super(name, composites);
		
	}
	public String getSourceIdentificationQualifier() {
		return sourceIdentificationQualifier;
	}

	public void setSourceIdentificationQualifier(
			String sourceIdentificationQualifier) {
		this.sourceIdentificationQualifier = sourceIdentificationQualifier;
	}

	public String getAgentIdentificationDetails() {
		return agentIdentificationDetails;
	}

	public void setAgentIdentificationDetails(String agentIdentificationDetails) {
		this.agentIdentificationDetails = agentIdentificationDetails;
	}

	public String getInhouseIdentification() {
		return inhouseIdentification;
	}

	public void setInhouseIdentification(String inhouseIdentification) {
		this.inhouseIdentification = inhouseIdentification;
	}

	public String getAgentLocation() {
		return agentLocation;
	}

	public void setAgentLocation(String agentLocation) {
		this.agentLocation = agentLocation;
	}

	public String getAirlineCrsCode() {
		return airlineCrsCode;
	}

	public void setAirlineCrsCode(String airlineCrsCode) {
		this.airlineCrsCode = airlineCrsCode;
	}


}
