package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class FAR to hold Fare information for a passenger(s) Ex:The fare is a 20
 * percent discounted fare type for an 9 year old
 * child.(FAR+C+9+1:20:US+++YEE3M') The fare is an industry discounted passenger
 * traveling on business with space available(FAR+I++764:4::B2+++C')
 */
public class FAR extends Segment {
    private String passengerType;
    private String age;
    private String numberOfDiscounts;
    private String discount;
    private String countryCode;
    private String discountedFareClassificationType;
    private String fareType;
    private String fareBasisCode;

    public FAR(List<Composite> composites) {
        super(FAR.class.getSimpleName(), composites);

    }
}
