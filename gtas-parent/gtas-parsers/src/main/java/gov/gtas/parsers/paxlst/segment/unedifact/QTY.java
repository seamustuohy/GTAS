package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * QTY QUANTITY
 * <p>
 * Function: To specify a pertinent quantity.
 */
public class QTY extends Segment {
    public QTY(Composite[] composites) {
        super(QTY.class.getSimpleName(), composites);
    }
}
