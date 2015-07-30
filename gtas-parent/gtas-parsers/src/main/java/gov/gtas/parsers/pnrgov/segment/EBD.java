package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * EBD: EXCESS BAGGAGE DETAILS
 * <p>
 * Excess Baggage Details of a passenger Used to send paid baggage information
 * Ex:One piece of baggage over the allowance USD 50 (EBD+USD:50.00+1::N’)
 */
public class EBD extends Segment {
    private String currencyCode;
    private String ratePerUnit;
    private String numberInExcess;
    private String pieceOrWeight;
    private String kgsOrPounds;

    public EBD(List<Composite> composites) {
        super(EBD.class.getSimpleName(), composites);
    }
}
