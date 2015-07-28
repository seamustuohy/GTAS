package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * SEGMENT REPETITION CONTROL
 * <p>
 * Used as trigger segment for PNRGOV GR.1 and will repeat for each PNR in the
 * message. This trigger segment is sent as an empty segment.(Ex: SRC')
 */
public class SRC extends Segment {
    public SRC(Composite[] composites) {
        super(SRC.class.getSimpleName(), composites);
    }
}
