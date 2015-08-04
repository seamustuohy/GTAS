package gov.gtas.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "itinerary")
public class Itinerary extends BaseEntity {
    @ManyToOne
    @JoinColumn
    private Flight flight;
    
    private Integer legNumber;
    
    @ManyToOne
    @JoinColumn
    private PnrData reservation;
}
