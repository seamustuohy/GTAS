package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class TIF holds Traveler information
 * @author GTAS4
 *
 *Only one surname and given name should be sent in one occurrence of the TIF even if 
 *there are multiple names for a surname in the PNR.
 *The Traveller Reference Number (9944) is assigned by the sending system and this number 
 *in Gr.2 may be used to cross reference an SSR in Gr.1 or Gr.5 or a TRI in Gr.7.
 *
 *Examples:
 *Passenger Jones/John Mr is an adult.(TIF+JONES+JOHNMR:A')
 *Passenger has a single letter family name – Miss Moan Y – single letter is doubled 
 *where MoanMiss was considered the given name. This rule is as defined in AIRIMP 
 *rules and its examples.(TIF+YY+MOANMISS:A’)
 *
 *Adult passenger has a single letter family name – Miss Tuyetmai Van A – all given names are 
 *combined with the single letter surname where Miss was considered the given name. This rule is as 
 *defined in AIRIMP rules and its examples.(TIF+ATUYETMAIVAN+MISS:A’)
 *
 *The PNR is for a group booking with no individual names.(TIF+SEETHE WORLD:G’)
 *
 *Infant no seat Passenger(TIF+RUITER+MISTY:IN’)
 */
public class TIF extends Segment{

	private String travelerSurname;
	private String travelerNameQualifier;
	private String travelerGivenNameTitle;
	private String travelerType;
	private String travelerNumber;//Used as a cross reference between data segments. In GR2 must be unique per passenger
	private String accompaniedBy;
	
	public TIF(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}

	public String getTravelerSurname() {
		return travelerSurname;
	}

	public void setTravelerSurname(String travelerSurname) {
		this.travelerSurname = travelerSurname;
	}

	public String getTravelerNameQualifier() {
		return travelerNameQualifier;
	}

	public void setTravelerNameQualifier(String travelerNameQualifier) {
		this.travelerNameQualifier = travelerNameQualifier;
	}

	public String getTravelerGivenNameTitle() {
		return travelerGivenNameTitle;
	}

	public void setTravelerGivenNameTitle(String travelerGivenNameTitle) {
		this.travelerGivenNameTitle = travelerGivenNameTitle;
	}

	public String getTravelerType() {
		return travelerType;
	}

	public void setTravelerType(String travelerType) {
		this.travelerType = travelerType;
	}

	public String getTravelerNumber() {
		return travelerNumber;
	}

	public void setTravelerNumber(String travelerNumber) {
		this.travelerNumber = travelerNumber;
	}

	public String getAccompaniedBy() {
		return accompaniedBy;
	}

	public void setAccompaniedBy(String accompaniedBy) {
		this.accompaniedBy = accompaniedBy;
	}

}
