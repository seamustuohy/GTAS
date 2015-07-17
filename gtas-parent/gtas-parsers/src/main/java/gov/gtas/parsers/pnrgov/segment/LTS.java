package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class LTS to hold Long Text String
 * @author GTAS4
 *
 * Ex:Unstructured PNR history.(LTS+ LAX GS WW D006217 2129Z/09DEC 02961B AS DL1314U 19FEB MCOATL NN/SS1
1130A 105P AS SEAT RS 29F TRAN/TRINH')
 */
public class LTS extends Segment{

	
	private String unstructuredHistory;
	public LTS(String name, Composite[] composites) {
		super(name, composites);
		
	}
	public String getUnstructuredHistory() {
		return unstructuredHistory;
	}
	public void setUnstructuredHistory(String unstructuredHistory) {
		this.unstructuredHistory = unstructuredHistory;
	}

}
