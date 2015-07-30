package gov.gtas.parsers.pnrgov.segment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrUtils;

/**
 * <p>
 * RCI: RESERVATION CONTROL INFORMATION
 * 
 * <p>
 * The composite will appear at least once and may be repeated up to eight more
 * times.
 * <p>
 * Examples: SAS passenger record reference.(RCI+SK:12DEF')
 * <p>
 * Galileo and SAS record references.(RCI+SK:123EF+1G:345ABC')
 * <p>
 * Delta is the operating carrier and the PNR was created on 24 February 2010 at
 * 2230 GMT. (RCI+DL:ABC456789::240210:2230')
 * <p>
 * CX is the operating carrier and no PNR was received from the reservation
 * system at a station handled by a ground handler; therefore the CX reservation
 * PNR locator is not available and “DCS reference” is the Reservation Control
 * Type.(RCI+CX:89QM3LABML:C’)
 */
public class RCI extends Segment {
    public class PnrReservation {
        private String airlineCode;
        private String reservationControlNumber;
        private String reservationControlType;
        private Date dateCreated;
        
        public String getAirlineCode() {
            return airlineCode;
        }
        public void setAirlineCode(String airlineCode) {
            this.airlineCode = airlineCode;
        }
        public String getReservationControlNumber() {
            return reservationControlNumber;
        }
        public void setReservationControlNumber(String reservationControlNumber) {
            this.reservationControlNumber = reservationControlNumber;
        }
        public String getReservationControlType() {
            return reservationControlType;
        }
        public void setReservationControlType(String reservationControlType) {
            this.reservationControlType = reservationControlType;
        }
        public Date getDateCreated() {
            return dateCreated;
        }
        public void setDateCreated(Date dateCreated) {
            this.dateCreated = dateCreated;
        }
    }

    private List<PnrReservation> reservations;
    
    public RCI(Composite[] composites) throws ParseException {
        super(RCI.class.getSimpleName(), composites);
        reservations = new ArrayList<>(composites.length);
        for (int i=0; i<composites.length; i++) {
            PnrReservation r = new PnrReservation();
            Composite c = composites[0];
            r.setAirlineCode(c.getElement(0));
            r.setReservationControlNumber(c.getElement(1));
            r.setReservationControlType(c.getElement(2));
            if (c.numElements() >= 4) {
                String dt = c.getElement(3) + c.getElement(4);
                r.setDateCreated(PnrUtils.parseDateTime(dt));
            }
            reservations.add(r);
        }
    }

    public List<PnrReservation> getReservations() {
        return reservations;
    }
}
