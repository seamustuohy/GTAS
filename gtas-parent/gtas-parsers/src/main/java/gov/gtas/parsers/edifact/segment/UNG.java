package gov.gtas.parsers.edifact.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * UNG: FUNCTIONAL GROUP HEADER
 * <p>
 * Function: To head, identify and specify a Functional Group. This segment is
 * optional for this implementation. If a service bureau, GDS, or other
 * transmitting third party is transmitting the message on behalf an aircraft
 * operator, this segment should specify the identity of the aircraft operator
 * of record (not the transmitter of the message). ￼
 * <p>
 * Example: UNG+PAXLST+AIRLINE1+NZCS+130628:0900+000000001+UN+D:12B'
 */
public class UNG extends Segment {
    public UNG(Composite[] composites) {
        super(UNG.class.getSimpleName(), composites);
    }
}
