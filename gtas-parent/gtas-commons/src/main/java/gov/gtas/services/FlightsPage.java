package gov.gtas.services;

import java.util.List;

import gov.gtas.vo.passenger.FlightVo;

public class FlightsPage {
    private List<FlightVo> flights;
    private long totalFlights;
    public FlightsPage(List<FlightVo> flights, long totalFlights) {
        this.flights = flights;
        this.totalFlights = totalFlights;
    }
    public List<FlightVo> getFlights() {
        return flights;
    }
    public long getTotalFlights() {
        return totalFlights;
    }
}