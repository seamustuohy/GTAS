package gov.gtas.services;

import java.util.List;

import gov.gtas.vo.passenger.PassengerVo;

public class PassengersPage {
    private List<PassengerVo> passengers;
    private long totalPassengers;
    public PassengersPage(List<PassengerVo> passengers, long totalPassengers) {
        this.passengers = passengers;
        this.totalPassengers = totalPassengers;
    }
    public List<PassengerVo> getPassengers() {
        return passengers;
    }
    public long getTotalPassengers() {
        return totalPassengers;
    }
}
