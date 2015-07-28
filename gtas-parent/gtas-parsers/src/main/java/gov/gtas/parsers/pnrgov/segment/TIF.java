package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * TIF: TRAVELLER INFORMATION
 * <p>
 * To specify a traveller(s) and personal details relating to the traveller(s).
 * <p>
 * Only one surname and given name should be sent in one occurrence of the TIF
 * even if there are multiple names for a surname in the PNR. The Traveller
 * Reference Number (9944) is assigned by the sending system and this number in
 * Gr.2 may be used to cross reference an SSR in Gr.1 or Gr.5 or a TRI in Gr.7.
 *
 * Examples: Passenger Jones/John Mr is an adult.(TIF+JONES+JOHNMR:A') Passenger
 * has a single letter family name – Miss Moan Y – single letter is doubled
 * where MoanMiss was considered the given name. This rule is as defined in
 * AIRIMP rules and its examples.(TIF+YY+MOANMISS:A’)
 *
 * Adult passenger has a single letter family name – Miss Tuyetmai Van A – all
 * given names are combined with the single letter surname where Miss was
 * considered the given name. This rule is as defined in AIRIMP rules and its
 * examples.(TIF+ATUYETMAIVAN+MISS:A’)
 *
 * The PNR is for a group booking with no individual names.(TIF+SEETHE WORLD:G’)
 *
 * Infant no seat Passenger(TIF+RUITER+MISTY:IN’)
 */
public class TIF extends Segment {

    private String travelerSurname;
    private String travelerNameQualifier;
    private String travelerGivenNameTitle;
    private String travelerType;

    // Used as a cross reference between data segments. In GR2 must be unique
    // per passenger
    private String travelerNumber;
    private String accompaniedBy;

    public TIF(Composite[] composites) {
        super(TIF.class.getSimpleName(), composites);
    }
}
