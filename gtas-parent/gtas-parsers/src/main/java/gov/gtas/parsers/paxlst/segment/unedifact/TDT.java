package gov.gtas.parsers.paxlst.segment.unedifact;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

/**
 * <p>
 * TDT: DETAILS OF TRANSPORT-
 * <p>
 * A segment to specify details of transport related to each leg, including
 * means of transport, mode of transport name and/or number of vessel and/or
 * vehicle and/or flight.
 * <p>
 * Examples:
 * <ul>
 * <li>TDT+20+DL123+++DL' Indicates flight identification DL123, Carrier Code DL
 * <li>TDT+20+EK456' Indicates flight identification EK456, Carrier Code not
 * required TDT+34+AF986+++AF' Indicates flight identification AF986, Carrier
 * Code AF, Over-flight.
 * </ul>
 */
public class TDT extends Segment {
    public enum TdtType {
        ARRIVING_OR_DEPARTING_FLIGHT, OVER_FLIGHT
    }

    private TdtType transportStageQualifier;
    private String c_journeyIdentifier;
    private String c_carrierIdentifier;
    private String flightNumber;
    private boolean isMasterCrewList;

    public TDT(List<Composite> composites) {
        super(TDT.class.getSimpleName(), composites);
        for (int i = 0; i < numComposites(); i++) {
            Composite c = getComposite(i);
            switch (i) {
            case 0:
                int code = Integer.valueOf(c.getElement(0));
                if (code == 20) {
                    this.transportStageQualifier = TdtType.ARRIVING_OR_DEPARTING_FLIGHT;
                } else if (code == 34) {
                    this.transportStageQualifier = TdtType.OVER_FLIGHT;
                } else {
                    logger.error("unknown TDT type: " + c.getElement(0));
                }

                break;
            case 1:
                this.isMasterCrewList = c.getElement(0).endsWith("MCL");
                this.c_journeyIdentifier = c.getElement(0).replace("MCL", "");
                break;
            case 4:
                this.c_carrierIdentifier = c.getElement(0);
                break;
            }
        }

        if (this.c_carrierIdentifier != null) {
            this.flightNumber = this.c_journeyIdentifier.replace(this.c_carrierIdentifier, "");
        } else {
            String[] tmp = ParseUtils.separateCarrierAndFlightNumber(this.c_journeyIdentifier);
            this.c_carrierIdentifier = tmp[0];
            this.flightNumber = ParseUtils.padFlightNumberWithZeroes(tmp[1]);
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public TdtType getTransportStageQualifier() {
        return transportStageQualifier;
    }

    public String getC_journeyIdentifier() {
        return c_journeyIdentifier;
    }

    public String getC_carrierIdentifier() {
        return c_carrierIdentifier;
    }

    public boolean isMasterCrewList() {
        return isMasterCrewList;
    }
}
