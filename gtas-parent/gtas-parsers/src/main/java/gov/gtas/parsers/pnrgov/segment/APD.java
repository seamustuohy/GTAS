package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Additional product details
 * @author GTAS4
 *
 * Ex:Equipment Type of Boeing 747
 * (APD+747')
 */
public class APD extends Segment{


	public APD(List<Composite> composites) {
		super(APD.class.getSimpleName(), composites);
		
	}
	
	private String typeOfAircraft;
	

	public String getTypeOfAircraft() {
		return typeOfAircraft;
	}


	public void setTypeOfAircraft(String typeOfAircraft) {
		this.typeOfAircraft = typeOfAircraft;
	}



}
