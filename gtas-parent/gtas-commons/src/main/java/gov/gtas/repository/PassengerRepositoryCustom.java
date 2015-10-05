package gov.gtas.repository;

import java.util.List;

import gov.gtas.model.Passenger;
import gov.gtas.services.dto.PassengersRequestDto;

public interface PassengerRepositoryCustom {
    public List<Object[]> getAllPassengersAndFlights(PassengersRequestDto request);
    
    public List<Passenger> getPassengersByFlightId(Long flightId, PassengersRequestDto request);
}
