package gov.gtas.parsers.pnrgov.segment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * TBD: TRAVELER BAGGAGE DETAILS/Electronic Ticketing
 * <p>
 * To specify the baggage details, including number of bags and serial numbers.
 * This segment is for the checked in baggage and not for excess bag details
 */
public class TBD extends Segment {
    private String numBags;
    
    public class BagDetails {
        private String airline;
        private String tagNumber;
        private String destAirport;
        public String getAirline() {
            return airline;
        }
        public void setAirline(String airline) {
            this.airline = airline;
        }
        public String getTagNumber() {
            return tagNumber;
        }
        public void setTagNumber(String tagNumber) {
            this.tagNumber = tagNumber;
        }
        public String getDestAirport() {
            return destAirport;
        }
        public void setDestAirport(String destAirport) {
            this.destAirport = destAirport;
        }
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
        }
    }
    
    private List<BagDetails> bagDetails;

    public TBD(List<Composite> composites) {
        super(TBD.class.getSimpleName(), composites);
        Composite c = getComposite(1);
        if (c != null) {
            this.numBags = c.getElement(1);
        }
        
        this.bagDetails = new ArrayList<>();
        for (int i=2; i<numComposites(); i++) {
            c = getComposite(i);
            BagDetails bag = new BagDetails();
            bag.setAirline(c.getElement(0));
            bag.setTagNumber(c.getElement(1));
            bag.setDestAirport(c.getElement(2));
            this.bagDetails.add(bag);
        }
    }

    public String getNumBags() {
        return numBags;
    }

    public List<BagDetails> getBagDetails() {
        return bagDetails;
    }
}
