package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * EQN: NUMBER OF UNITS
 * 
 * <p>
 * The EQN at level 0 is used to specify the total number of PNRs being sent for
 * the flight push. In case of full PNR push, the total number of PNRs contained
 * in the full PNR push regardless of the number of messages used for the full
 * push. In the case of update PNR push, the total number of PNRs contained in
 * the update PNR push regardless of the number of messages used for the update
 * push should be used.
 * 
 * <p>
 * Example: Total number of PNRs(EQN+98')
 */
public class EQN_L0 extends Segment {
    private Integer numberOfPnrRecords;

    public EQN_L0(List<Composite> composites) {
        super("EQN", composites);
        Composite c = getComposite(0);
        this.numberOfPnrRecords = Integer.valueOf(c.getElement(0));
    }

    public Integer getNumberOfPnrRecords() {
        return numberOfPnrRecords;
    }
}
