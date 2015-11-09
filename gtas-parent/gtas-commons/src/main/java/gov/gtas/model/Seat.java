package gov.gtas.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "seat",
    uniqueConstraints={@UniqueConstraint(columnNames={"number", "passenger_id", "flight_id"})}
)
public class Seat extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public Seat() { }
    
    private String number;
    
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Flight flight;
    
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
    @Override
    public int hashCode() {
       return Objects.hash(this.number, this.passenger, this.flight);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final Seat other = (Seat)obj;
        return Objects.equals(this.number, other.number)
                && Objects.equals(this.passenger, other.passenger)
                && Objects.equals(this.flight, other.flight);
    }
}
