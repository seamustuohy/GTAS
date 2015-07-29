package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class EQN holds the total number of PNRs being sent for the flight push
 * @author GTAS4
 *
 * The EQN at level 0 is used to specify the total number of PNRs being sent for the flight push. 
 * In case of full PNR push, the total number of PNRs contained in the full PNR push regardless of
 *  the number of messages used for the full push. In the case of update PNR push, the total number
 *   of PNRs contained in the update PNR push regardless of the number of messages used for the 
 *   update push should be used.
 *   Example:
 *   Total number of PNRs(EQN+98')
 *   Four passengers split from this PNR.(EQN+4'MSG)
 *   
 */
public class EQN extends Segment{

	private Integer numberOfPnrRecords;
	

	public EQN(Composite[] composites) {
		super(EQN.class.getSimpleName(), composites);
	}
}
