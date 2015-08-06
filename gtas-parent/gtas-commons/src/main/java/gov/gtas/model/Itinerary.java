package gov.gtas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "itinerary")
public class Itinerary extends BaseEntity {
    @ManyToOne
    private Flight flight;
    
    @ManyToOne
    private PnrData pnr;

    @Column(name = "leg_number")
    private Integer legNumber;

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public PnrData getPnr() {
        return pnr;
    }

    public void setPnr(PnrData pnr) {
        this.pnr = pnr;
    }

    public Integer getLegNumber() {
        return legNumber;
    }

    public void setLegNumber(Integer legNumber) {
        this.legNumber = legNumber;
    }
}
